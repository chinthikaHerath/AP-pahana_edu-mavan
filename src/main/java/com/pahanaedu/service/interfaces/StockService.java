package com.pahanaedu.service.interfaces;

import com.pahanaedu.model.StockMovement;
import com.pahanaedu.model.Item;
import com.pahanaedu.constant.MovementType;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.exception.DatabaseException;
import com.pahanaedu.exception.BusinessException;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Interface for Stock Service
 * Handles business logic for stock management
 */
public interface StockService {
    
    /**
     * Record a stock IN movement (purchase, return, etc.)
     * @param itemId Item ID
     * @param quantity Quantity to add
     * @param movementType Type of IN movement
     * @param referenceType Reference type (optional)
     * @param referenceId Reference ID (optional)
     * @param reason Reason for movement (optional)
     * @param userId User performing the movement
     * @return Created movement ID
     * @throws ValidationException if validation fails
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if business rule violation occurs
     */
    int recordStockIn(int itemId, int quantity, MovementType movementType,
                      String referenceType, int referenceId, String reason, int userId)
            throws ValidationException, DatabaseException, BusinessException;
    
    /**
     * Record a stock OUT movement (sale, damage, etc.)
     * @param itemId Item ID
     * @param quantity Quantity to remove
     * @param movementType Type of OUT movement
     * @param referenceType Reference type (optional)
     * @param referenceId Reference ID (optional)
     * @param reason Reason for movement (optional)
     * @param userId User performing the movement
     * @return Created movement ID
     * @throws ValidationException if validation fails
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if insufficient stock
     */
    int recordStockOut(int itemId, int quantity, MovementType movementType,
                       String referenceType, int referenceId, String reason, int userId)
            throws ValidationException, DatabaseException, BusinessException;
    
    /**
     * Record a stock adjustment (positive or negative)
     * @param itemId Item ID
     * @param adjustmentQuantity Adjustment quantity (positive or negative)
     * @param reason Reason for adjustment
     * @param userId User performing the adjustment
     * @return Created movement ID
     * @throws ValidationException if validation fails
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if business rule violation occurs
     */
    int recordStockAdjustment(int itemId, int adjustmentQuantity, String reason, int userId)
            throws ValidationException, DatabaseException, BusinessException;
    
    /**
     * Process sale - update stock for multiple items
     * @param billId Bill ID
     * @param itemQuantities Map of item ID to quantity sold
     * @param userId User processing the sale
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if insufficient stock for any item
     */
    void processSale(int billId, Map<Integer, Integer> itemQuantities, int userId)
            throws DatabaseException, BusinessException;
    
    /**
     * Process return - add items back to stock
     * @param originalBillId Original bill ID
     * @param itemQuantities Map of item ID to quantity returned
     * @param reason Return reason
     * @param userId User processing the return
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if return quantity exceeds original sale
     */
    void processReturn(int originalBillId, Map<Integer, Integer> itemQuantities, 
                      String reason, int userId)
            throws DatabaseException, BusinessException;
    
    /**
     * Cancel sale - reverse all stock movements for a bill
     * @param billId Bill ID to cancel
     * @param reason Cancellation reason
     * @param userId User canceling the sale
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if bill already canceled
     */
    void cancelSale(int billId, String reason, int userId)
            throws DatabaseException, BusinessException;
    
    /**
     * Get current stock level for an item
     * @param itemId Item ID
     * @return Current stock quantity
     * @throws DatabaseException if database operation fails
     */
    int getCurrentStock(int itemId) throws DatabaseException;
    
    /**
     * Get stock value for an item
     * @param itemId Item ID
     * @return Stock value (quantity * unit price)
     * @throws DatabaseException if database operation fails
     */
    double getStockValue(int itemId) throws DatabaseException;
    
    /**
     * Check if sufficient stock is available
     * @param itemId Item ID
     * @param requiredQuantity Required quantity
     * @return true if sufficient stock available
     * @throws DatabaseException if database operation fails
     */
    boolean isStockAvailable(int itemId, int requiredQuantity) throws DatabaseException;
    
    /**
     * Get stock movements for an item
     * @param itemId Item ID
     * @return List of stock movements
     * @throws DatabaseException if database operation fails
     */
    List<StockMovement> getItemStockMovements(int itemId) throws DatabaseException;
    
    /**
     * Get stock movements by date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of stock movements
     * @throws DatabaseException if database operation fails
     */
    List<StockMovement> getStockMovementsByDateRange(Date startDate, Date endDate)
            throws DatabaseException;
    
    /**
     * Get recent stock movements
     * @param limit Number of movements to retrieve
     * @return List of recent movements
     * @throws DatabaseException if database operation fails
     */
    List<StockMovement> getRecentMovements(int limit) throws DatabaseException;
    
    /**
     * Get low stock items
     * @return List of items below reorder level
     * @throws DatabaseException if database operation fails
     */
    List<Item> getLowStockItems() throws DatabaseException;
    
    /**
     * Get out of stock items
     * @return List of items with zero stock
     * @throws DatabaseException if database operation fails
     */
    List<Item> getOutOfStockItems() throws DatabaseException;
    
    /**
     * Get stock valuation report
     * @return List of items with stock value
     * @throws DatabaseException if database operation fails
     */
    List<Map<String, Object>> getStockValuationReport() throws DatabaseException;
    
    /**
     * Get stock movement summary by type
     * @param startDate Start date (optional)
     * @param endDate End date (optional)
     * @return Map of movement type to count/quantity
     * @throws DatabaseException if database operation fails
     */
    Map<String, Object> getStockMovementSummary(Date startDate, Date endDate)
            throws DatabaseException;
    
    /**
     * Get stock turnover report
     * @param startDate Start date
     * @param endDate End date
     * @return List of items with turnover data
     * @throws DatabaseException if database operation fails
     */
    List<Map<String, Object>> getStockTurnoverReport(Date startDate, Date endDate)
            throws DatabaseException;
    
    /**
     * Get stock history for an item
     * @param itemId Item ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of movements with running balance
     * @throws DatabaseException if database operation fails
     */
    List<Map<String, Object>> getStockHistory(int itemId, Date startDate, Date endDate)
            throws DatabaseException;
    
    /**
     * Validate stock movement
     * @param movement Stock movement to validate
     * @param isStockOut true if stock out movement
     * @throws ValidationException if validation fails
     */
    void validateStockMovement(StockMovement movement, boolean isStockOut)
            throws ValidationException;
    
    /**
     * Update item stock directly (used during migration or initialization)
     * @param itemId Item ID
     * @param newQuantity New stock quantity
     * @param reason Reason for direct update
     * @param userId User performing update
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if business rule violation occurs
     */
    void updateItemStock(int itemId, int newQuantity, String reason, int userId)
            throws DatabaseException, BusinessException;
    
    /**
     * Get stock alert summary
     * @return Map containing low stock count, out of stock count, etc.
     * @throws DatabaseException if database operation fails
     */
    Map<String, Integer> getStockAlertSummary() throws DatabaseException;
    
    /**
     * Export stock movements to CSV
     * @param movements List of movements to export
     * @return CSV string
     */
    String exportMovementsToCSV(List<StockMovement> movements);
    
    /**
     * Get audit trail for stock movements
     * @param itemId Item ID (0 for all items)
     * @param startDate Start date
     * @param endDate End date
     * @return List of movements for audit
     * @throws DatabaseException if database operation fails
     */
    List<StockMovement> getStockAuditTrail(int itemId, Date startDate, Date endDate)
            throws DatabaseException;
}