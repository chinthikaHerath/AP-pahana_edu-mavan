package com.pahanaedu.service.impl;

import com.pahanaedu.service.interfaces.BillingService;
import com.pahanaedu.service.interfaces.CustomerService;
import com.pahanaedu.service.interfaces.ItemService;
import com.pahanaedu.dao.interfaces.BillDAO;
import com.pahanaedu.dao.interfaces.BillItemDAO;
import com.pahanaedu.dao.interfaces.ItemDAO;
//import com.pahanaedu.dao.interfaces.StockMovementDAO;
import com.pahanaedu.dao.impl.BillDAOImpl;
import com.pahanaedu.dao.impl.BillItemDAOImpl;
import com.pahanaedu.dao.impl.ItemDAOImpl;
import com.pahanaedu.model.Bill;
import com.pahanaedu.model.BillItem;
import com.pahanaedu.model.Customer;
import com.pahanaedu.model.Item;
import com.pahanaedu.dto.BillDTO;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.exception.DatabaseException;
import com.pahanaedu.exception.BusinessException;
import com.pahanaedu.util.ValidationUtil;
import com.pahanaedu.constant.PaymentStatus;
import com.pahanaedu.constant.PaymentMethod;
import com.pahanaedu.dto.ItemDTO;
import com.pahanaedu.service.interfaces.StockService;
import com.pahanaedu.service.impl.StockServiceImpl;
import com.pahanaedu.constant.MovementType;

import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Implementation of BillingService interface
 * Handles business logic for billing operations
 */
public class BillingServiceImpl implements BillingService {
    
    private BillDAO billDAO;
    private BillItemDAO billItemDAO;
    private ItemDAO itemDAO;
    private CustomerService customerService;
    private StockService stockService;
    // Note: ItemService and StockService will be implemented in later phases
    // private ItemService itemService;
    
    
    public BillingServiceImpl() {
        this.billDAO = new BillDAOImpl();
        this.billItemDAO = new BillItemDAOImpl();
        this.itemDAO = new ItemDAOImpl();
        this.customerService = new CustomerServiceImpl();
        this.stockService = new StockServiceImpl();
        // Initialize other services when available
    }
    
    @Override
    public int createBill(Bill bill, int userId) 
            throws ValidationException, DatabaseException, BusinessException {
        
        // Validate bill
        validateBill(bill, false);
        
        // Check if customer can be billed
        if (!canCustomerBeBilled(bill.getCustomerId())) {
            throw new BusinessException("Customer cannot be billed. Please check customer status.");
        }
        
        // Validate and check item availability
        if (bill.getBillItems() == null || bill.getBillItems().isEmpty()) {
            throw new ValidationException("Bill must have at least one item");
        }
        
        for (BillItem item : bill.getBillItems()) {
            if (!isItemAvailable(item.getItemId(), item.getQuantity())) {
                Item itemDetails = itemDAO.getItemById(item.getItemId());
                throw new BusinessException("Insufficient stock for item: " + 
                    (itemDetails != null ? itemDetails.getItemName() : "Unknown"));
            }
        }
        
        // Set user ID and timestamps
        bill.setUserId(userId);
        bill.setBillDate(new Date());
        bill.setBillTime(new Date());
        
        // Calculate totals
        calculateBillTotals(bill);
        
        // Create bill (DAO handles transaction)
        int billId = billDAO.createBill(bill);
        
        // Update stock levels
        updateStockAfterSale(bill);
        
        return billId;
    }
    
    @Override
    public Bill createBillFromDTO(BillDTO billDTO, int userId) 
            throws ValidationException, DatabaseException, BusinessException {
        
        // Create Bill from DTO
        Bill bill = new Bill();
        bill.setCustomerId(billDTO.getCustomerId());
        bill.setPaymentMethod(billDTO.getPaymentMethod());
        bill.setPaymentStatus(billDTO.getPaymentStatus());
        bill.setDiscountPercentage(billDTO.getDiscountPercentage());
        bill.setTaxPercentage(billDTO.getTaxPercentage());
        bill.setNotes(billDTO.getNotes());
        
        // Convert DTO items to BillItems
        List<BillItem> billItems = new ArrayList<>();
        if (billDTO.getItemDTOs() != null) {
            for (ItemDTO itemDTO : billDTO.getItemDTOs()) {
                Item item = itemDAO.getItemById(itemDTO.getItemId());
                if (item == null) {
                    throw new BusinessException("Item not found: " + itemDTO.getItemId());
                }
                
                BillItem billItem = new BillItem();
                billItem.setItemId(item.getItemId());
                billItem.setItem(item);
                billItem.setQuantity(itemDTO.getQuantity());
                billItem.setUnitPrice(itemDTO.getUnitPrice() > 0 ? itemDTO.getUnitPrice() : item.getSellingPrice());
                billItem.setDiscountPercentage(itemDTO.getDiscountPercentage());
                billItems.add(billItem);
            }
        }
        bill.setBillItems(billItems);
        
        // Create bill
        int billId = createBill(bill, userId);
        bill.setBillId(billId);
        
        // Load complete bill data
        return getBillById(billId);
    }
    
    @Override
    public boolean updateBill(Bill bill) 
            throws ValidationException, DatabaseException, BusinessException {
        
        // Check if bill exists and is editable
        Bill existingBill = billDAO.getBillById(bill.getBillId());
        if (existingBill == null) {
            throw new BusinessException("Bill not found");
        }
        
        if (!isBillEditable(bill.getBillId())) {
            throw new BusinessException("Bill cannot be edited. Only pending bills can be modified.");
        }
        
        // Validate bill
        validateBill(bill, true);
        
        // Recalculate totals
        calculateBillTotals(bill);
        
        // Update bill
        boolean success = billDAO.updateBill(bill);
        
        if (success && bill.getBillItems() != null) {
            // Replace bill items
            billItemDAO.replaceBillItems(bill.getBillId(), bill.getBillItems());
        }
        
        return success;
    }
    
    @Override
    public boolean cancelBill(int billId, String reason, int userId) 
            throws DatabaseException, BusinessException {
        
        // Check if bill can be cancelled
        if (!isBillCancellable(billId)) {
            throw new BusinessException("Bill cannot be cancelled. Only pending or partial payment bills can be cancelled.");
        }
        
        // Get bill details for stock reversal
        Bill bill = getBillById(billId);
        if (bill == null) {
            throw new BusinessException("Bill not found");
        }
        
        // Cancel bill
        boolean success = billDAO.cancelBill(billId, reason + " (Cancelled by user: " + userId + ")");
        
        if (success) {
            // Reverse stock levels
            reverseStockAfterCancellation(bill);
        }
        
        return success;
    }
    
    @Override
    public Bill getBillById(int billId) throws DatabaseException {
        Bill bill = billDAO.getBillById(billId);
        if (bill != null) {
            // Load bill items with complete details
            List<BillItem> items = billItemDAO.getBillItemsWithDetails(billId);
            
            // If getBillItemsWithDetails doesn't exist, use the basic method and enhance
            if (items == null || items.isEmpty()) {
                items = billItemDAO.getBillItemsByBillId(billId);
                
                // Ensure each BillItem has a complete Item object
                for (BillItem billItem : items) {
                    if (billItem.getItem() == null) {
                        try {
                            Item item = itemDAO.getItemById(billItem.getItemId());
                            billItem.setItem(item);
                        } catch (DatabaseException e) {
                            // Log error but continue with other items
                            System.err.println("Could not load item " + billItem.getItemId() + ": " + e.getMessage());
                        }
                    }
                }
            }
            
            bill.setBillItems(items);
            
            // Load customer details
            try {
                Customer customer = customerService.getCustomerById(bill.getCustomerId());
                bill.setCustomer(customer);
            } catch (DatabaseException e) {
                System.err.println("Could not load customer " + bill.getCustomerId() + ": " + e.getMessage());
            }
        }
        return bill;
    }
    
    @Override
    public Bill getBillByNumber(String billNumber) throws DatabaseException {
        if (ValidationUtil.isNullOrEmpty(billNumber)) {
            return null;
        }
        
        Bill bill = billDAO.getBillByNumber(billNumber);
        if (bill != null) {
            // Load bill items
            List<BillItem> items = billItemDAO.getBillItemsByBillId(bill.getBillId());
            bill.setBillItems(items);
            
            // Load customer details
            Customer customer = customerService.getCustomerById(bill.getCustomerId());
            bill.setCustomer(customer);
        }
        return bill;
    }
    
    @Override
    public List<Bill> getAllBills() throws DatabaseException {
        return billDAO.getAllBills();
    }
    
    @Override
    public List<Bill> getCustomerBills(int customerId) throws DatabaseException {
        return billDAO.getBillsByCustomer(customerId);
    }
    
    @Override
    public List<Bill> getBillsByDateRange(Date startDate, Date endDate) throws DatabaseException {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }
        
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        
        return billDAO.getBillsByDateRange(startDate, endDate);
    }
    
    @Override
    public List<Bill> getTodaysBills() throws DatabaseException {
        return billDAO.getTodaysBills();
    }
    
    @Override
    public List<Bill> getPendingBills() throws DatabaseException {
        return billDAO.getPendingBills();
    }
    
    @Override
    public List<Bill> searchBills(String searchTerm) throws DatabaseException {
        if (ValidationUtil.isNullOrEmpty(searchTerm)) {
            return new ArrayList<>();
        }
        return billDAO.searchBills(searchTerm.trim());
    }
    
    @Override
    public BillItem addItemToBill(int billId, Item item, int quantity, double discountPercentage)
            throws ValidationException, DatabaseException, BusinessException {
        
        // Check if bill exists and is editable
        Bill bill = getBillById(billId);
        if (bill == null) {
            throw new BusinessException("Bill not found");
        }
        
        if (!isBillEditable(billId)) {
            throw new BusinessException("Bill cannot be edited");
        }
        
        // Validate quantity
        if (quantity <= 0) {
            throw new ValidationException("Quantity must be greater than zero");
        }
        
        // Check item availability
        if (!isItemAvailable(item.getItemId(), quantity)) {
            throw new BusinessException("Insufficient stock for item: " + item.getItemName());
        }
        
        // Check if item already exists in bill
        if (billItemDAO.isItemInBill(billId, item.getItemId())) {
            throw new BusinessException("Item already exists in bill. Update quantity instead.");
        }
        
        // Create bill item
        BillItem billItem = new BillItem(item, quantity);
        billItem.setBillId(billId);
        billItem.setDiscountPercentage(discountPercentage);
        
        // Add to database
        int billItemId = billItemDAO.addBillItem(billItem);
        billItem.setBillItemId(billItemId);
        
        // Update bill totals
        recalculateBillTotals(billId);
        
        return billItem;
    }
    
    @Override
    public boolean updateBillItemQuantity(int billItemId, int newQuantity)
            throws ValidationException, DatabaseException, BusinessException {
        
        // Get bill item
        BillItem billItem = billItemDAO.getBillItemById(billItemId);
        if (billItem == null) {
            throw new BusinessException("Bill item not found");
        }
        
        // Check if bill is editable
        if (!isBillEditable(billItem.getBillId())) {
            throw new BusinessException("Bill cannot be edited");
        }
        
        // Validate quantity
        if (newQuantity <= 0) {
            throw new ValidationException("Quantity must be greater than zero");
        }
        
        // Check item availability if quantity is increasing
        if (newQuantity > billItem.getQuantity()) {
            int additionalQuantity = newQuantity - billItem.getQuantity();
            if (!isItemAvailable(billItem.getItemId(), additionalQuantity)) {
                throw new BusinessException("Insufficient stock for additional quantity");
            }
        }
        
        // Update quantity
        boolean success = billItemDAO.updateBillItemQuantity(billItemId, newQuantity);
        
        if (success) {
            // Update bill totals
            recalculateBillTotals(billItem.getBillId());
        }
        
        return success;
    }
    
    @Override
    public boolean removeItemFromBill(int billId, int billItemId)
            throws DatabaseException, BusinessException {
        
        // Check if bill is editable
        if (!isBillEditable(billId)) {
            throw new BusinessException("Bill cannot be edited");
        }
        
        // Check if this is the last item
        int itemCount = billItemDAO.getItemCountForBill(billId);
        if (itemCount <= 1) {
            throw new BusinessException("Cannot remove last item from bill. Cancel the bill instead.");
        }
        
        // Remove item
        boolean success = billItemDAO.deleteBillItem(billItemId);
        
        if (success) {
            // Update bill totals
            recalculateBillTotals(billId);
        }
        
        return success;
    }
    
    @Override
    public Bill applyBillDiscount(int billId, double discountPercentage)
            throws ValidationException, DatabaseException, BusinessException {
        
        // Validate discount
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new ValidationException("Discount percentage must be between 0 and 100");
        }
        
        // Get bill
        Bill bill = getBillById(billId);
        if (bill == null) {
            throw new BusinessException("Bill not found");
        }
        
        if (!isBillEditable(billId)) {
            throw new BusinessException("Bill cannot be edited");
        }
        
        // Apply discount
        bill.setDiscountPercentage(discountPercentage);
        calculateBillTotals(bill);
        
        // Update bill
        billDAO.updateBill(bill);
        
        return bill;
    }
    
    @Override
    public boolean updatePaymentStatus(int billId, String newStatus, String notes)
            throws ValidationException, DatabaseException, BusinessException {
        
        // Validate status
        PaymentStatus status = PaymentStatus.fromString(newStatus);
        if (status == null) {
            throw new ValidationException("Invalid payment status: " + newStatus);
        }
        
        // Get current bill
        Bill bill = billDAO.getBillById(billId);
        if (bill == null) {
            throw new BusinessException("Bill not found");
        }
        
        // Check if status change is allowed
        PaymentStatus currentStatus = PaymentStatus.fromString(bill.getPaymentStatus());
        if (currentStatus != null && currentStatus.isFinal()) {
            throw new BusinessException("Cannot change status of finalized bill");
        }
        
        // Update status
        return billDAO.updatePaymentStatus(billId, newStatus, notes);
    }
    
    @Override
    public boolean processPayment(int billId, String paymentMethod, String paymentReference)
            throws ValidationException, DatabaseException, BusinessException {
        
        // Validate payment method
        PaymentMethod method = PaymentMethod.fromString(paymentMethod);
        if (method == null) {
            throw new ValidationException("Invalid payment method: " + paymentMethod);
        }
        
        // Check if additional info is required
        if (method.requiresAdditionalInfo() && ValidationUtil.isNullOrEmpty(paymentReference)) {
            throw new ValidationException(method.getAdditionalInfoPlaceholder() + " is required");
        }
        
        // Get bill
        Bill bill = billDAO.getBillById(billId);
        if (bill == null) {
            throw new BusinessException("Bill not found");
        }
        
        // Check if already paid
        if ("PAID".equals(bill.getPaymentStatus())) {
            throw new BusinessException("Bill is already paid");
        }
        
        // Update payment info
        bill.setPaymentMethod(paymentMethod);
        String notes = "Payment processed";
        if (!ValidationUtil.isNullOrEmpty(paymentReference)) {
            notes += " - Reference: " + paymentReference;
        }
        
        // Update status to PAID
        return billDAO.updatePaymentStatus(billId, "PAID", notes);
    }
    
    @Override
    public void validateBill(Bill bill, boolean isUpdate) throws ValidationException {
        ValidationException validationException = new ValidationException("Bill validation failed");
        
        // Validate customer
        if (!isUpdate && bill.getCustomerId() <= 0) {
            validationException.addFieldError("customerId", "Customer is required");
        }
        
        // Validate payment method
        if (ValidationUtil.isNullOrEmpty(bill.getPaymentMethod())) {
            validationException.addFieldError("paymentMethod", "Payment method is required");
        } else {
            PaymentMethod method = PaymentMethod.fromString(bill.getPaymentMethod());
            if (method == null) {
                validationException.addFieldError("paymentMethod", "Invalid payment method");
            }
        }
        
        // Validate payment status
        if (ValidationUtil.isNullOrEmpty(bill.getPaymentStatus())) {
            validationException.addFieldError("paymentStatus", "Payment status is required");
        } else {
            PaymentStatus status = PaymentStatus.fromString(bill.getPaymentStatus());
            if (status == null) {
                validationException.addFieldError("paymentStatus", "Invalid payment status");
            }
        }
        
        // Validate items (only for new bills)
        if (!isUpdate) {
            if (bill.getBillItems() == null || bill.getBillItems().isEmpty()) {
                validationException.addFieldError("items", "At least one item is required");
            } else {
                // Validate each item
                for (int i = 0; i < bill.getBillItems().size(); i++) {
                    BillItem item = bill.getBillItems().get(i);
                    if (item.getItemId() <= 0) {
                        validationException.addFieldError("items[" + i + "].itemId", "Invalid item");
                    }
                    if (item.getQuantity() <= 0) {
                        validationException.addFieldError("items[" + i + "].quantity", "Quantity must be positive");
                    }
                    if (item.getUnitPrice() < 0) {
                        validationException.addFieldError("items[" + i + "].unitPrice", "Price cannot be negative");
                    }
                    if (item.getDiscountPercentage() < 0 || item.getDiscountPercentage() > 100) {
                        validationException.addFieldError("items[" + i + "].discount", "Discount must be 0-100%");
                    }
                }
            }
        }
        
        // Validate discount
        if (bill.getDiscountPercentage() < 0 || bill.getDiscountPercentage() > 100) {
            validationException.addFieldError("discountPercentage", "Discount must be between 0 and 100");
        }
        
        // Validate tax
        if (bill.getTaxPercentage() < 0 || bill.getTaxPercentage() > 100) {
            validationException.addFieldError("taxPercentage", "Tax must be between 0 and 100");
        }
        
        // Throw exception if there are validation errors
        if (validationException.hasFieldErrors()) {
            throw validationException;
        }
    }
    
    @Override
    public boolean canCustomerBeBilled(int customerId) throws DatabaseException {
        Customer customer = customerService.getCustomerById(customerId);
        
        if (customer == null) {
            return false;
        }
        
        // Check if customer is active
        if (!customer.isActive()) {
            return false;
        }
        
        // Check if customer has too many pending bills (business rule)
        boolean hasPendingBills = billDAO.hasCustomerPendingBills(customerId);
        if (hasPendingBills) {
            List<Bill> pendingBills = billDAO.getBillsByCustomer(customerId);
            int pendingCount = 0;
            for (Bill bill : pendingBills) {
                if ("PENDING".equals(bill.getPaymentStatus()) || "PARTIAL".equals(bill.getPaymentStatus())) {
                    pendingCount++;
                }
            }
            // Allow maximum 3 pending bills
            if (pendingCount >= 3) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public boolean isItemAvailable(int itemId, int quantity) throws DatabaseException {
        try {
            return stockService.isStockAvailable(itemId, quantity);
        } catch (DatabaseException e) {
            throw e;
        }
    }
    
    @Override
    public Map<String, Object> getBillStatistics() throws DatabaseException {
        return billDAO.getSalesStatistics();
    }
    
    @Override
    public Map<String, Object> getDailySalesSummary(Date date) throws DatabaseException {
        if (date == null) {
            date = new Date();
        }
        return billDAO.getDailySalesSummary(date);
    }
    
    @Override
    public Map<String, Object> getMonthlySalesSummary(int year, int month) throws DatabaseException {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("year", year);
        summary.put("month", month);
        summary.put("totalSales", billDAO.getMonthlySalesTotal(year, month));
        
        // Get daily breakdown
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        Date startDate = cal.getTime();
        
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date endDate = cal.getTime();
        
        List<Bill> monthlyBills = billDAO.getBillsByDateRange(startDate, endDate);
        summary.put("totalBills", monthlyBills.size());
        
        return summary;
    }
    
    @Override
    public List<Map<String, Object>> getTopSellingItems(Date startDate, Date endDate, int limit)
            throws DatabaseException {
        
        if (limit <= 0) {
            limit = 10;
        }
        
        return billDAO.getTopSellingItems(startDate, endDate, limit);
    }
    
    @Override
    public List<Map<String, Object>> getSalesByCategory(Date startDate, Date endDate)
            throws DatabaseException {
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String start = dateFormat.format(startDate);
        String end = dateFormat.format(endDate);
        
        return billItemDAO.getRevenueByCategory(start, end);
    }
    
    @Override
    public Map<String, Object> getCustomerPurchaseSummary(int customerId) throws DatabaseException {
        return billDAO.getCustomerPurchaseSummary(customerId);
    }
    
    @Override
    public List<Bill> getOverdueBills(int daysOverdue) throws DatabaseException {
        if (daysOverdue < 0) {
            daysOverdue = 30; // Default to 30 days
        }
        return billDAO.getOverdueBills(daysOverdue);
    }
    
    @Override
    public byte[] exportBillToPDF(int billId) throws DatabaseException, BusinessException {
        // This will be implemented when PDF generation utility is available
        throw new BusinessException("PDF export not yet implemented");
    }
    
    @Override
    public String exportBillsToCSV(List<Bill> bills) {
        StringBuilder csv = new StringBuilder();
        
        // Header
        csv.append("Bill Number,Date,Time,Customer Name,Account Number,");
        csv.append("Subtotal,Discount %,Discount Amount,Tax %,Tax Amount,");
        csv.append("Total Amount,Payment Method,Payment Status\n");
        
        // Data
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        
        for (Bill bill : bills) {
            csv.append(escapeCSV(bill.getBillNumber())).append(",");
            csv.append(dateFormat.format(bill.getBillDate())).append(",");
            csv.append(timeFormat.format(bill.getBillTime())).append(",");
            csv.append(escapeCSV(bill.getCustomerName())).append(",");
            csv.append(escapeCSV(bill.getCustomerAccountNumber())).append(",");
            csv.append(bill.getSubtotal()).append(",");
            csv.append(bill.getDiscountPercentage()).append(",");
            csv.append(bill.getDiscountAmount()).append(",");
            csv.append(bill.getTaxPercentage()).append(",");
            csv.append(bill.getTaxAmount()).append(",");
            csv.append(bill.getTotalAmount()).append(",");
            csv.append(bill.getPaymentMethod()).append(",");
            csv.append(bill.getPaymentStatus()).append("\n");
        }
        
        return csv.toString();
    }
    
    @Override
    public Bill calculateBillTotals(Bill bill) {
        if (bill.getBillItems() == null || bill.getBillItems().isEmpty()) {
            bill.setSubtotal(0);
            bill.setTotalAmount(0);
            return bill;
        }
        
        // Calculate subtotal from items
        double subtotal = 0;
        for (BillItem item : bill.getBillItems()) {
            item.calculateTotalPrice();
            subtotal += item.getTotalPrice();
        }
        bill.setSubtotal(subtotal);
        
        // Apply bill-level discount
        if (bill.getDiscountPercentage() > 0) {
            bill.setDiscountAmount(subtotal * (bill.getDiscountPercentage() / 100));
        } else {
            bill.setDiscountAmount(0);
        }
        
        // Calculate tax on discounted amount
        double afterDiscount = subtotal - bill.getDiscountAmount();
        if (bill.getTaxPercentage() > 0) {
            bill.setTaxAmount(afterDiscount * (bill.getTaxPercentage() / 100));
        } else {
            bill.setTaxAmount(0);
        }
        
        // Calculate total
        bill.setTotalAmount(afterDiscount + bill.getTaxAmount());
        
        return bill;
    }
    
    @Override
    public List<Bill> getBillsWithPagination(int page, int pageSize) throws DatabaseException {
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;
        if (pageSize > 100) pageSize = 100; // Maximum page size
        
        int offset = (page - 1) * pageSize;
        return billDAO.getBillsWithPagination(offset, pageSize);
    }
    
    @Override
    public List<Bill> getRecentBills(int limit) throws DatabaseException {
        if (limit <= 0) limit = 5;
        if (limit > 50) limit = 50; // Maximum limit
        
        return billDAO.getRecentBills(limit);
    }
    
    @Override
    public boolean isBillEditable(int billId) throws DatabaseException {
        Bill bill = billDAO.getBillById(billId);
        if (bill == null) {
            return false;
        }
        
        PaymentStatus status = PaymentStatus.fromString(bill.getPaymentStatus());
        return status != null && status.isEditable();
    }
    
    @Override
    public boolean isBillCancellable(int billId) throws DatabaseException {
        Bill bill = billDAO.getBillById(billId);
        if (bill == null) {
            return false;
        }
        
        PaymentStatus status = PaymentStatus.fromString(bill.getPaymentStatus());
        return status != null && status.isCancellable();
    }
    
    @Override
    public String getNextBillNumber() throws DatabaseException {
        return billDAO.generateBillNumber();
    }
    
    @Override
    public boolean sendBillByEmail(int billId, String recipientEmail) 
            throws DatabaseException, BusinessException {
        // This will be implemented when email service is available
        throw new BusinessException("Email functionality not yet implemented");
    }
    
    /**
     * Helper method to update stock after sale
     */
    /**
     * Helper method to update stock after sale
     */
    private void updateStockAfterSale(Bill bill) throws DatabaseException {
        try {
            // Use StockService to properly record stock movements
            Map<Integer, Integer> itemQuantities = new HashMap<>();
            for (BillItem billItem : bill.getBillItems()) {
                itemQuantities.put(billItem.getItemId(), billItem.getQuantity());
            }
            
            // Process the sale through stock service
            stockService.processSale(bill.getBillId(), itemQuantities, bill.getUserId());
        } catch (BusinessException e) {
            throw new DatabaseException("Error updating stock after sale: " + e.getMessage(), e);
        }
    }
    /**
     * Helper method to reverse stock after cancellation
     */
    /**
     * Helper method to reverse stock after cancellation
     */
    private void reverseStockAfterCancellation(Bill bill) throws DatabaseException {
        try {
            // Use StockService to properly reverse the sale
            stockService.cancelSale(bill.getBillId(), "Bill cancelled", bill.getUserId());
        } catch (BusinessException e) {
            throw new DatabaseException("Error reversing stock after cancellation: " + e.getMessage(), e);
        }
    }
    
    /**
     * Helper method to recalculate bill totals after item changes
     */
    private void recalculateBillTotals(int billId) throws DatabaseException {
        Bill bill = getBillById(billId);
        if (bill != null) {
            calculateBillTotals(bill);
            billDAO.updateBill(bill);
        }
    }
    
    /**
     * Helper method to escape CSV field
     */
    private String escapeCSV(String field) {
        if (field == null) return "";
        
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}