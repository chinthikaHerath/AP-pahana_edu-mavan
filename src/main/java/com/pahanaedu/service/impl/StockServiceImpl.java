package com.pahanaedu.service.impl;

import com.pahanaedu.service.interfaces.StockService;
import com.pahanaedu.dao.interfaces.StockMovementDAO;
import com.pahanaedu.dao.interfaces.ItemDAO;
import com.pahanaedu.dao.impl.StockMovementDAOImpl;
import com.pahanaedu.dao.impl.ItemDAOImpl;
import com.pahanaedu.model.StockMovement;
import com.pahanaedu.model.Item;
import com.pahanaedu.constant.MovementType;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.exception.DatabaseException;
import com.pahanaedu.exception.BusinessException;
import com.pahanaedu.util.ValidationUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of StockService interface
 * Handles business logic for stock management
 */
public class StockServiceImpl implements StockService {
    
    private StockMovementDAO stockMovementDAO;
    private ItemDAO itemDAO;
    
    public StockServiceImpl() {
        this.stockMovementDAO = new StockMovementDAOImpl();
        this.itemDAO = new ItemDAOImpl();
    }
    
    @Override
    public int recordStockIn(int itemId, int quantity, MovementType movementType,
                            String referenceType, int referenceId, String reason, int userId)
            throws ValidationException, DatabaseException, BusinessException {
        
        // Validate movement type is a stock IN type
        if (!movementType.isStockIncrease()) {
            throw new BusinessException("Invalid movement type for stock IN operation");
        }
        
        // Create stock movement
        StockMovement movement = new StockMovement(itemId, movementType, quantity, userId);
        movement.setReferenceType(referenceType);
        movement.setReferenceId(referenceId);
        movement.setReason(reason);
        
        // Validate movement
        validateStockMovement(movement, false);
        
        // Check if item exists
        Item item = itemDAO.getItemById(itemId);
        if (item == null) {
            throw new BusinessException("Item not found");
        }
        
        // Add stock movement
        int movementId = stockMovementDAO.addStockMovement(movement);
        
        // Update item stock
        int newStock = item.getQuantityInStock() + quantity;
        itemDAO.updateItemStock(itemId, newStock);
        
        return movementId;
    }
    
    @Override
    public int recordStockOut(int itemId, int quantity, MovementType movementType,
                             String referenceType, int referenceId, String reason, int userId)
            throws ValidationException, DatabaseException, BusinessException {
        
        // Validate movement type is a stock OUT type
        if (!movementType.isStockDecrease()) {
            throw new BusinessException("Invalid movement type for stock OUT operation");
        }
        
        // Check stock availability
        if (!isStockAvailable(itemId, quantity)) {
            Item item = itemDAO.getItemById(itemId);
            throw new BusinessException(
                String.format("Insufficient stock for item '%s'. Available: %d, Required: %d",
                    item.getItemName(), item.getQuantityInStock(), quantity)
            );
        }
        
        // Create stock movement (store as negative for OUT movements)
        StockMovement movement = new StockMovement(itemId, movementType, -quantity, userId);
        movement.setReferenceType(referenceType);
        movement.setReferenceId(referenceId);
        movement.setReason(reason);
        
        // Validate movement
        validateStockMovement(movement, true);
        
        // Add stock movement
        int movementId = stockMovementDAO.addStockMovement(movement);
        
        // Update item stock
        Item item = itemDAO.getItemById(itemId);
        int newStock = item.getQuantityInStock() - quantity;
        itemDAO.updateItemStock(itemId, newStock);
        
        return movementId;
    }
    
    @Override
    public int recordStockAdjustment(int itemId, int adjustmentQuantity, String reason, int userId)
            throws ValidationException, DatabaseException, BusinessException {
        
        if (ValidationUtil.isNullOrEmpty(reason)) {
            throw new ValidationException("reason", "Reason is required for stock adjustment");
        }
        
        // Check if item exists
        Item item = itemDAO.getItemById(itemId);
        if (item == null) {
            throw new BusinessException("Item not found");
        }
        
        // Check if adjustment would result in negative stock
        int currentStock = item.getQuantityInStock();
        int newStock = currentStock + adjustmentQuantity;
        if (newStock < 0) {
            throw new BusinessException(
                String.format("Adjustment would result in negative stock. Current: %d, Adjustment: %d",
                    currentStock, adjustmentQuantity)
            );
        }
        
        // Create stock movement
        StockMovement movement = new StockMovement(itemId, MovementType.ADJUSTMENT, 
                                                  adjustmentQuantity, userId);
        movement.setReason(reason);
        
        // Add stock movement
        int movementId = stockMovementDAO.addStockMovement(movement);
        
        // Update item stock
        itemDAO.updateItemStock(itemId, newStock);
        
        return movementId;
    }
    
    @Override
    public void processSale(int billId, Map<Integer, Integer> itemQuantities, int userId)
            throws DatabaseException, BusinessException {
        
        // Validate all items have sufficient stock
        for (Map.Entry<Integer, Integer> entry : itemQuantities.entrySet()) {
            int itemId = entry.getKey();
            int quantity = entry.getValue();
            
            if (!isStockAvailable(itemId, quantity)) {
                Item item = itemDAO.getItemById(itemId);
                throw new BusinessException(
                    String.format("Insufficient stock for item '%s'", item.getItemName())
                );
            }
        }
        
        // Process each item
        for (Map.Entry<Integer, Integer> entry : itemQuantities.entrySet()) {
            int itemId = entry.getKey();
            int quantity = entry.getValue();
            
            try {
                recordStockOut(itemId, quantity, MovementType.OUT, "BILL", billId, 
                              "Sale - Bill#" + billId, userId);
            } catch (ValidationException e) {
                throw new BusinessException("SALE_VALIDATION_ERROR", 
                    "Validation error during sale processing: " + e.getMessage(), e);
            }
        }
    }
    
    @Override
    public void processReturn(int originalBillId, Map<Integer, Integer> itemQuantities, 
                             String reason, int userId)
            throws DatabaseException, BusinessException {
        
        // Get original sale movements
        List<StockMovement> originalMovements = 
            stockMovementDAO.getMovementsByReference("BILL", originalBillId);
        
        if (originalMovements.isEmpty()) {
            throw new BusinessException("Original bill movements not found");
        }
        
        // Validate return quantities don't exceed original sale
        Map<Integer, Integer> originalQuantities = new HashMap<>();
        for (StockMovement movement : originalMovements) {
            if (movement.getMovementType() == MovementType.OUT) {
                originalQuantities.put(movement.getItemId(), 
                    Math.abs(movement.getQuantity()));
            }
        }
        
        for (Map.Entry<Integer, Integer> entry : itemQuantities.entrySet()) {
            int itemId = entry.getKey();
            int returnQuantity = entry.getValue();
            Integer originalQuantity = originalQuantities.get(itemId);
            
            if (originalQuantity == null || returnQuantity > originalQuantity) {
                Item item = itemDAO.getItemById(itemId);
                throw new BusinessException(
                    String.format("Return quantity exceeds original sale for item '%s'",
                        item.getItemName())
                );
            }
        }
        
     // Process returns
        for (Map.Entry<Integer, Integer> entry : itemQuantities.entrySet()) {
            int itemId = entry.getKey();
            int quantity = entry.getValue();
            
            try {
                recordStockIn(itemId, quantity, MovementType.RETURN, "BILL", originalBillId,
                             reason != null ? reason : "Return - Original Bill#" + originalBillId, 
                             userId);
            } catch (ValidationException e) {
                throw new BusinessException("RETURN_VALIDATION_ERROR",
                    "Validation error during return processing: " + e.getMessage(), e);
            }
        }
    }
    
    @Override
    public void cancelSale(int billId, String reason, int userId)
            throws DatabaseException, BusinessException {
        
        // Get all movements for this bill
        List<StockMovement> movements = 
            stockMovementDAO.getMovementsByReference("BILL", billId);
        
        if (movements.isEmpty()) {
            throw new BusinessException("No movements found for bill");
        }
        
        // Check if already canceled
        boolean hasReturn = movements.stream()
            .anyMatch(m -> m.getMovementType() == MovementType.RETURN);
        if (hasReturn) {
            throw new BusinessException("Bill already has returns/cancellations");
        }
        
     // Reverse each OUT movement
        for (StockMovement movement : movements) {
            if (movement.getMovementType() == MovementType.OUT) {
                try {
                    recordStockIn(movement.getItemId(), Math.abs(movement.getQuantity()),
                                MovementType.RETURN, "BILL_CANCEL", billId,
                                reason != null ? reason : "Canceled Bill#" + billId,
                                userId);
                } catch (ValidationException e) {
                    throw new BusinessException("CANCEL_VALIDATION_ERROR",
                        "Validation error during sale cancellation: " + e.getMessage(), e);
                }
            }
        }
    }
    
    @Override
    public int getCurrentStock(int itemId) throws DatabaseException {
        Item item = itemDAO.getItemById(itemId);
        if (item != null) {
            return item.getQuantityInStock();
        }
        return 0;
    }
    
    @Override
    public double getStockValue(int itemId) throws DatabaseException {
        return stockMovementDAO.getStockValue(itemId);
    }
    
    @Override
    public boolean isStockAvailable(int itemId, int requiredQuantity) throws DatabaseException {
        int currentStock = getCurrentStock(itemId);
        return currentStock >= requiredQuantity;
    }
    
    @Override
    public List<StockMovement> getItemStockMovements(int itemId) throws DatabaseException {
        return stockMovementDAO.getMovementsByItemId(itemId);
    }
    
    @Override
    public List<StockMovement> getStockMovementsByDateRange(Date startDate, Date endDate)
            throws DatabaseException {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }
        return stockMovementDAO.getMovementsByDateRange(startDate, endDate);
    }
    
    @Override
    public List<StockMovement> getRecentMovements(int limit) throws DatabaseException {
        if (limit < 1) limit = 10;
        return stockMovementDAO.getRecentMovements(limit);
    }
    
    @Override
    public List<Item> getLowStockItems() throws DatabaseException {
        return itemDAO.getLowStockItems();
    }
    
    @Override
    public List<Item> getOutOfStockItems() throws DatabaseException {
        return itemDAO.getOutOfStockItems();
    }
    
    @Override
    public List<Map<String, Object>> getStockValuationReport() throws DatabaseException {
        return stockMovementDAO.getStockValuationReport();
    }
    
    @Override
    public Map<String, Object> getStockMovementSummary(Date startDate, Date endDate)
            throws DatabaseException {
        Map<String, Object> summary = new HashMap<>();
        
        // Get movement counts by type
        Map<MovementType, Integer> typeSummary = stockMovementDAO.getMovementSummaryByType();
        summary.put("byType", typeSummary);
        
        // Calculate totals
        int totalIn = 0;
        int totalOut = 0;
        int totalAdjustments = 0;
        
        for (Map.Entry<MovementType, Integer> entry : typeSummary.entrySet()) {
            MovementType type = entry.getKey();
            int count = entry.getValue();
            
            if (type.isStockIncrease()) {
                totalIn += count;
            } else if (type.isStockDecrease()) {
                totalOut += count;
            }
            
            if (type == MovementType.ADJUSTMENT) {
                totalAdjustments = count;
            }
        }
        
        summary.put("totalIn", totalIn);
        summary.put("totalOut", totalOut);
        summary.put("totalAdjustments", totalAdjustments);
        summary.put("totalMovements", stockMovementDAO.getTotalMovementCount());
        
        // If date range provided, get date-wise summary
        if (startDate != null && endDate != null) {
            Map<Date, Integer> dateSummary = 
                stockMovementDAO.getMovementSummaryByDate(startDate, endDate);
            summary.put("byDate", dateSummary);
        }
        
        return summary;
    }
    
    @Override
    public List<Map<String, Object>> getStockTurnoverReport(Date startDate, Date endDate)
            throws DatabaseException {
        List<Map<String, Object>> report = new ArrayList<>();
        
        // Get all items
        List<Item> items = itemDAO.getActiveItems();
        
        for (Item item : items) {
            Map<String, Object> itemReport = new HashMap<>();
            itemReport.put("itemId", item.getItemId());
            itemReport.put("itemCode", item.getItemCode());
            itemReport.put("itemName", item.getItemName());
            itemReport.put("currentStock", item.getQuantityInStock());
            
            // Get movements for the period
            List<StockMovement> movements = stockMovementDAO.getMovementsByItemId(item.getItemId());
            List<StockMovement> periodMovements = movements.stream()
                .filter(m -> !m.getMovementDate().before(startDate) && 
                           !m.getMovementDate().after(endDate))
                .collect(Collectors.toList());
            
            // Calculate sales quantity
            int salesQuantity = periodMovements.stream()
                .filter(m -> m.getMovementType() == MovementType.OUT)
                .mapToInt(m -> Math.abs(m.getQuantity()))
                .sum();
            
            // Calculate average stock (simplified - using current stock)
            int averageStock = item.getQuantityInStock();
            
            // Calculate turnover ratio
            double turnoverRatio = averageStock > 0 ? 
                (double) salesQuantity / averageStock : 0;
            
            itemReport.put("salesQuantity", salesQuantity);
            itemReport.put("averageStock", averageStock);
            itemReport.put("turnoverRatio", turnoverRatio);
            
            report.add(itemReport);
        }
        
        // Sort by turnover ratio descending
        report.sort((a, b) -> 
            Double.compare((Double) b.get("turnoverRatio"), (Double) a.get("turnoverRatio")));
        
        return report;
    }
    
    @Override
    public List<Map<String, Object>> getStockHistory(int itemId, Date startDate, Date endDate)
            throws DatabaseException {
        List<Map<String, Object>> history = new ArrayList<>();
        
        // Get opening balance
        int openingBalance = stockMovementDAO.getStockBalanceAsOfDate(itemId, startDate);
        
        // Get movements in the period
        List<StockMovement> movements = stockMovementDAO.getMovementsByItemId(itemId);
        List<StockMovement> periodMovements = movements.stream()
            .filter(m -> !m.getMovementDate().before(startDate) && 
                       !m.getMovementDate().after(endDate))
            .sorted(Comparator.comparing(StockMovement::getMovementDate))
            .collect(Collectors.toList());
        
        int runningBalance = openingBalance;
        
        // Add opening balance row
        Map<String, Object> openingRow = new HashMap<>();
        openingRow.put("date", startDate);
        openingRow.put("description", "Opening Balance");
        openingRow.put("quantity", 0);
        openingRow.put("balance", openingBalance);
        openingRow.put("movementType", "OPENING");
        history.add(openingRow);
        
        // Add each movement
        for (StockMovement movement : periodMovements) {
            Map<String, Object> row = new HashMap<>();
            row.put("date", movement.getMovementDate());
            row.put("movementId", movement.getMovementId());
            row.put("movementType", movement.getMovementType().toString());
            row.put("quantity", movement.getQuantity());
            row.put("reference", movement.getReferenceDisplay());
            row.put("reason", movement.getReason());
            row.put("userName", movement.getUserName());
            
            // Update running balance
            runningBalance += movement.getQuantity();
            row.put("balance", runningBalance);
            
            history.add(row);
        }
        
        return history;
    }
    
    @Override
    public void validateStockMovement(StockMovement movement, boolean isStockOut)
            throws ValidationException {
        
        ValidationException validationException = new ValidationException("Stock movement validation failed");
        
        // Validate item ID
        if (movement.getItemId() <= 0) {
            validationException.addFieldError("itemId", "Invalid item ID");
        }
        
        // Validate quantity
        if (movement.getQuantity() == 0) {
            validationException.addFieldError("quantity", "Quantity cannot be zero");
        }
        
        if (isStockOut && movement.getQuantity() > 0) {
            validationException.addFieldError("quantity", 
                "Quantity should be negative for stock out movements");
        }
        
        // Validate movement type
        if (movement.getMovementType() == null) {
            validationException.addFieldError("movementType", "Movement type is required");
        }
        
        // Validate user ID
        if (movement.getUserId() <= 0) {
            validationException.addFieldError("userId", "Invalid user ID");
        }
        
        // Validate reason for adjustments
        if (movement.getMovementType() == MovementType.ADJUSTMENT && 
            ValidationUtil.isNullOrEmpty(movement.getReason())) {
            validationException.addFieldError("reason", 
                "Reason is required for stock adjustments");
        }
        
        // Throw exception if there are validation errors
        if (validationException.hasFieldErrors()) {
            throw validationException;
        }
    }
    
    @Override
    public void updateItemStock(int itemId, int newQuantity, String reason, int userId)
            throws DatabaseException, BusinessException {
        if (newQuantity < 0) {
            throw new BusinessException("Stock quantity cannot be negative");
        }
        
        if (ValidationUtil.isNullOrEmpty(reason)) {
            throw new BusinessException("Reason is required for direct stock update");
        }
        
        // Get current stock
        int currentStock = getCurrentStock(itemId);
        int difference = newQuantity - currentStock;
        
        if (difference != 0) {
            // Record as adjustment
            try {
                recordStockAdjustment(itemId, difference, reason, userId);
            } catch (ValidationException e) {
                throw new BusinessException("STOCK_UPDATE_VALIDATION_ERROR",
                    "Validation error during stock update: " + e.getMessage(), e);
            }
        }
    }
    
    @Override
    public Map<String, Integer> getStockAlertSummary() throws DatabaseException {
        Map<String, Integer> summary = new HashMap<>();
        
        List<Item> lowStockItems = getLowStockItems();
        List<Item> outOfStockItems = getOutOfStockItems();
        
        summary.put("lowStock", lowStockItems.size());
        summary.put("outOfStock", outOfStockItems.size());
        summary.put("totalAlerts", lowStockItems.size() + outOfStockItems.size());
        
        return summary;
    }
    
    @Override
    public String exportMovementsToCSV(List<StockMovement> movements) {
        StringBuilder csv = new StringBuilder();
        
        // Header
        csv.append("Movement ID,Date,Item Code,Item Name,Movement Type,");
        csv.append("Quantity,Reference,Reason,User,Total Value\n");
        
        // Data
        for (StockMovement movement : movements) {
            csv.append(movement.getMovementId()).append(",");
            csv.append(movement.getMovementDate()).append(",");
            csv.append(escapeCSV(movement.getItemCode())).append(",");
            csv.append(escapeCSV(movement.getItemName())).append(",");
            csv.append(movement.getMovementType().getDisplayName()).append(",");
            csv.append(movement.getQuantity()).append(",");
            csv.append(escapeCSV(movement.getReferenceDisplay())).append(",");
            csv.append(escapeCSV(movement.getReason())).append(",");
            csv.append(escapeCSV(movement.getUserName())).append(",");
            csv.append(movement.getTotalValue()).append("\n");
        }
        
        return csv.toString();
    }
    
    @Override
    public List<StockMovement> getStockAuditTrail(int itemId, Date startDate, Date endDate)
            throws DatabaseException {
        return stockMovementDAO.getAuditTrail(itemId, startDate, endDate);
    }
    
    /**
     * Escape CSV field if it contains special characters
     */
    private String escapeCSV(String field) {
        if (field == null) return "";
        
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}