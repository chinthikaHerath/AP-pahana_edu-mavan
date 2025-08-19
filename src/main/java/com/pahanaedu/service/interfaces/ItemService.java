package com.pahanaedu.service.interfaces;

import com.pahanaedu.model.Item;
import com.pahanaedu.model.Category;
import com.pahanaedu.model.StockMovement;
import com.pahanaedu.constant.MovementType;
import com.pahanaedu.exception.BusinessException;
import com.pahanaedu.exception.ValidationException;
import java.util.List;
import java.util.Map;

/**
 * Interface for Item Service
 * Handles business logic for item and stock management
 */
public interface ItemService {
    
    // ========== Item Management Methods ==========
    
    /**
     * Add a new item
     * @param item Item to add
     * @param createdBy User ID creating the item
     * @return Generated item ID
     * @throws ValidationException if validation fails
     * @throws BusinessException if business logic fails
     */
    int addItem(Item item, int createdBy) throws ValidationException, BusinessException;
    
    /**
     * Update an existing item
     * @param item Item with updated information
     * @return true if update successful
     * @throws ValidationException if validation fails
     * @throws BusinessException if business logic fails
     */
    boolean updateItem(Item item) throws ValidationException, BusinessException;
    
    /**
     * Deactivate an item
     * @param itemId Item ID to deactivate
     * @return true if deactivation successful
     * @throws BusinessException if business logic fails
     */
    boolean deactivateItem(int itemId) throws BusinessException;
    
    // ========== Item Retrieval Methods ==========
    
    /**
     * Get item by ID
     * @param itemId Item ID
     * @return Item object or null
     * @throws BusinessException if retrieval fails
     */
    Item getItemById(int itemId) throws BusinessException;
    
    /**
     * Get all active items
     * @return List of active items
     * @throws BusinessException if retrieval fails
     */
    List<Item> getAllActiveItems() throws BusinessException;
    
    /**
     * Get active items (alias for getAllActiveItems)
     * @return List of active items
     * @throws BusinessException if retrieval fails
     */
    List<Item> getActiveItems() throws BusinessException;
    
    /**
     * Search items by term
     * @param searchTerm Search term
     * @return List of matching items
     * @throws BusinessException if search fails
     */
    List<Item> searchItems(String searchTerm) throws BusinessException;
    
    /**
     * Get items by category
     * @param categoryId Category ID
     * @return List of items in category
     * @throws BusinessException if retrieval fails
     */
    List<Item> getItemsByCategory(int categoryId) throws BusinessException;
    
    /**
     * Get low stock items
     * @return List of low stock items
     * @throws BusinessException if retrieval fails
     */
    List<Item> getLowStockItems() throws BusinessException;
    
    /**
     * Get all categories
     * @return List of categories
     * @throws BusinessException if retrieval fails
     */
    List<Category> getAllCategories() throws BusinessException;
    
    
    /**
     * Activate an item
     * @param itemId Item ID to activate
     * @return true if activation successful
     * @throws BusinessException if business logic fails
     */
    boolean activateItem(int itemId) throws BusinessException;
    
    /**
     * Get all items including inactive ones
     * @param includeInactive true to include inactive items
     * @return List of items
     * @throws BusinessException if retrieval fails
     */
    List<Item> getAllItems(boolean includeInactive) throws BusinessException;
    
    /**
     * Get inactive items only
     * @return List of inactive items
     * @throws BusinessException if retrieval fails
     */
    List<Item> getInactiveItems() throws BusinessException;
    
    /**
     * Check if item can be safely deactivated
     * @param itemId Item ID to check
     * @return true if item can be deactivated
     * @throws BusinessException if check fails
     */
    boolean canDeactivateItem(int itemId) throws BusinessException;
    
    // ========== Basic Stock Management Methods ==========
    
    /**
     * Increase stock quantity
     * @param itemId Item ID
     * @param quantity Quantity to increase
     * @return true if successful
     * @throws BusinessException if operation fails
     */
    boolean increaseStock(int itemId, int quantity) throws BusinessException;
    
    /**
     * Decrease stock quantity
     * @param itemId Item ID
     * @param quantity Quantity to decrease
     * @return true if successful
     * @throws BusinessException if operation fails
     */
    boolean decreaseStock(int itemId, int quantity) throws BusinessException;
    
    /**
     * Adjust stock to specific quantity
     * @param itemId Item ID
     * @param newQuantity New stock quantity
     * @return true if successful
     * @throws BusinessException if operation fails
     */
    boolean adjustStock(int itemId, int newQuantity) throws BusinessException;
    
    // ========== NEW: Stock Movement Methods ==========
    
    /**
     * Adjust stock with movement record
     * Creates a stock movement record and updates item quantity in a transaction
     * @param itemId Item ID
     * @param movementType Type of stock movement
     * @param quantity Quantity to adjust
     * @param reason Reason for adjustment
     * @param userId User performing the adjustment
     * @return true if successful
     * @throws BusinessException if operation fails
     */
    boolean adjustStockWithMovement(int itemId, MovementType movementType, 
                                   int quantity, String reason, int userId) 
                                   throws BusinessException;
    
    /**
     * Get stock movement history for an item
     * @param itemId Item ID
     * @return List of stock movements
     * @throws BusinessException if retrieval fails
     */
    List<StockMovement> getStockHistory(int itemId) throws BusinessException;
    
    /**
     * Get recent stock movements across all items
     * @param limit Number of movements to retrieve
     * @return List of recent stock movements
     * @throws BusinessException if retrieval fails
     */
    List<StockMovement> getRecentStockMovements(int limit) throws BusinessException;
    
    /**
     * Get stock movements by date range for an item
     * @param itemId Item ID (0 for all items)
     * @param startDate Start date
     * @param endDate End date
     * @return List of stock movements
     * @throws BusinessException if retrieval fails
     */
    List<StockMovement> getStockMovementsByDateRange(int itemId, 
                                                    java.util.Date startDate, 
                                                    java.util.Date endDate) 
                                                    throws BusinessException;
    
    // ========== Reporting Methods ==========
    
    /**
     * Get out of stock items
     * @return List of out of stock items
     * @throws BusinessException if retrieval fails
     */
    List<Item> getOutOfStockItems() throws BusinessException;
    
    /**
     * Get item count by category
     * @return Map of category name to item count
     * @throws BusinessException if retrieval fails
     */
    Map<String, Integer> getItemCountByCategory() throws BusinessException;
    
    /**
     * Get total inventory value
     * @return Total value of all inventory
     * @throws BusinessException if calculation fails
     */
    double getTotalInventoryValue() throws BusinessException;
}