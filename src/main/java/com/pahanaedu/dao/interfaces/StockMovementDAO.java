package com.pahanaedu.dao.interfaces;

import com.pahanaedu.model.StockMovement;
import com.pahanaedu.constant.MovementType;
import com.pahanaedu.exception.DatabaseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Interface for Stock Movement Data Access Object
 * Defines all database operations for StockMovement entity
 */
public interface StockMovementDAO {
    
    /**
     * Add a new stock movement record
     * @param movement StockMovement object to be added
     * @return Generated movement ID
     * @throws DatabaseException if database operation fails
     */
    int addStockMovement(StockMovement movement) throws DatabaseException;
    
    /**
     * Get stock movement by ID
     * @param movementId Movement ID
     * @return StockMovement object if found, null otherwise
     * @throws DatabaseException if database operation fails
     */
    StockMovement getMovementById(int movementId) throws DatabaseException;
    
    /**
     * Get all stock movements for an item
     * @param itemId Item ID
     * @return List of stock movements
     * @throws DatabaseException if database operation fails
     */
    List<StockMovement> getMovementsByItemId(int itemId) throws DatabaseException;
    
    /**
     * Get stock movements by type
     * @param movementType Type of movement
     * @return List of stock movements
     * @throws DatabaseException if database operation fails
     */
    List<StockMovement> getMovementsByType(MovementType movementType) throws DatabaseException;
    
    /**
     * Get stock movements by reference
     * @param referenceType Reference type (e.g., "BILL", "RETURN")
     * @param referenceId Reference ID
     * @return List of stock movements
     * @throws DatabaseException if database operation fails
     */
    List<StockMovement> getMovementsByReference(String referenceType, int referenceId) 
            throws DatabaseException;
    
    /**
     * Get stock movements by date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of stock movements
     * @throws DatabaseException if database operation fails
     */
    List<StockMovement> getMovementsByDateRange(Date startDate, Date endDate) 
            throws DatabaseException;
    
    /**
     * Get stock movements by user
     * @param userId User ID who performed the movement
     * @return List of stock movements
     * @throws DatabaseException if database operation fails
     */
    List<StockMovement> getMovementsByUserId(int userId) throws DatabaseException;
    
    /**
     * Get recent stock movements
     * @param limit Number of movements to retrieve
     * @return List of recent stock movements
     * @throws DatabaseException if database operation fails
     */
    List<StockMovement> getRecentMovements(int limit) throws DatabaseException;
    
    /**
     * Get stock movements with pagination
     * @param offset Starting position
     * @param limit Number of records to retrieve
     * @return List of stock movements
     * @throws DatabaseException if database operation fails
     */
    List<StockMovement> getMovementsWithPagination(int offset, int limit) 
            throws DatabaseException;
    
    /**
     * Get current stock level for an item
     * @param itemId Item ID
     * @return Current stock quantity
     * @throws DatabaseException if database operation fails
     */
    int getCurrentStock(int itemId) throws DatabaseException;
    
    /**
     * Get stock value for an item (quantity * unit price)
     * @param itemId Item ID
     * @return Stock value
     * @throws DatabaseException if database operation fails
     */
    double getStockValue(int itemId) throws DatabaseException;
    
    /**
     * Get total stock movements count
     * @return Total number of movements
     * @throws DatabaseException if database operation fails
     */
    int getTotalMovementCount() throws DatabaseException;
    
    /**
     * Get stock movement summary by type
     * @return Map of movement type to count
     * @throws DatabaseException if database operation fails
     */
    Map<MovementType, Integer> getMovementSummaryByType() throws DatabaseException;
    
    /**
     * Get stock movement summary by date
     * @param startDate Start date
     * @param endDate End date
     * @return Map of date to movement count
     * @throws DatabaseException if database operation fails
     */
    Map<Date, Integer> getMovementSummaryByDate(Date startDate, Date endDate) 
            throws DatabaseException;
    
    /**
     * Calculate stock balance up to a specific date
     * @param itemId Item ID
     * @param upToDate Calculate balance up to this date
     * @return Stock balance
     * @throws DatabaseException if database operation fails
     */
    int getStockBalanceAsOfDate(int itemId, Date upToDate) throws DatabaseException;
    
    /**
     * Get items with stock movements in date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of item IDs with movements
     * @throws DatabaseException if database operation fails
     */
    List<Integer> getItemsWithMovements(Date startDate, Date endDate) 
            throws DatabaseException;
    
    /**
     * Delete stock movements by reference (used when canceling bills)
     * @param referenceType Reference type
     * @param referenceId Reference ID
     * @return Number of movements deleted
     * @throws DatabaseException if database operation fails
     */
    int deleteMovementsByReference(String referenceType, int referenceId) 
            throws DatabaseException;
    
    /**
     * Get stock valuation report data
     * @return List of items with current stock and value
     * @throws DatabaseException if database operation fails
     */
    List<Map<String, Object>> getStockValuationReport() throws DatabaseException;
    
    /**
     * Get stock movement history for audit
     * @param itemId Item ID (optional, pass 0 for all items)
     * @param startDate Start date
     * @param endDate End date
     * @return List of movements with full details
     * @throws DatabaseException if database operation fails
     */
    List<StockMovement> getAuditTrail(int itemId, Date startDate, Date endDate) 
            throws DatabaseException;
}