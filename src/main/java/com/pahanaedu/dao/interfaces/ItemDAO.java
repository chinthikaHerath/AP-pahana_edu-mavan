package com.pahanaedu.dao.interfaces;

import com.pahanaedu.model.Item;
import com.pahanaedu.exception.DatabaseException;
import java.util.List;

/**
 * Interface for Item Data Access Object
 * Defines all database operations for Item entity
 */
public interface ItemDAO {
    
    /**
     * Add a new item
     * @param item Item to add
     * @return Generated item ID
     * @throws DatabaseException if database operation fails
     */
    int addItem(Item item) throws DatabaseException;
    
    /**
     * Update an existing item
     * @param item Item with updated information
     * @return true if update successful
     * @throws DatabaseException if database operation fails
     */
    boolean updateItem(Item item) throws DatabaseException;
    
    /**
     * Deactivate an item (soft delete)
     * @param itemId Item ID to deactivate
     * @return true if deactivation successful
     * @throws DatabaseException if database operation fails
     */
    boolean deactivateItem(int itemId) throws DatabaseException;
    
    /**
     * Get item by ID
     * @param itemId Item ID
     * @return Item object or null if not found
     * @throws DatabaseException if database operation fails
     */
    Item getItemById(int itemId) throws DatabaseException;
    
    /**
     * Get all active items
     * @return List of active items
     * @throws DatabaseException if database operation fails
     */
    List<Item> getAllActiveItems() throws DatabaseException;
    
    /**
     * Get all active items (alias for getAllActiveItems)
     * @return List of active items
     * @throws DatabaseException if database operation fails
     */
    List<Item> getActiveItems() throws DatabaseException;
    
    /**
     * Search items by code, name, or ISBN
     * @param searchTerm Search term
     * @return List of matching items
     * @throws DatabaseException if database operation fails
     */
    List<Item> searchItems(String searchTerm) throws DatabaseException;
    
    /**
     * Get items by category
     * @param categoryId Category ID
     * @return List of items in the category
     * @throws DatabaseException if database operation fails
     */
    List<Item> getItemsByCategory(int categoryId) throws DatabaseException;
    
    /**
     * Get low stock items (quantity <= reorder level)
     * @return List of low stock items
     * @throws DatabaseException if database operation fails
     */
    List<Item> getLowStockItems() throws DatabaseException;
    
    /**
     * Get out of stock items (quantity = 0)
     * @return List of out of stock items
     * @throws DatabaseException if database operation fails
     */
    List<Item> getOutOfStockItems() throws DatabaseException;
    
    /**
     * Get total item count
     * @return Total number of items
     * @throws DatabaseException if database operation fails
     */
    int getTotalItemCount() throws DatabaseException;
    
    /**
     * Get active item count
     * @return Number of active items
     * @throws DatabaseException if database operation fails
     */
    int getActiveItemCount() throws DatabaseException;
    
    /**
     * Update item stock quantity
     * @param itemId Item ID
     * @param newQuantity New stock quantity
     * @return true if update successful
     * @throws DatabaseException if database operation fails
     */
    boolean updateItemStock(int itemId, int newQuantity) throws DatabaseException;
    
    /**
     * Check if item code exists
     * @param itemCode Item code to check
     * @return true if item code exists
     * @throws DatabaseException if database operation fails
     */
    boolean isItemCodeExists(String itemCode) throws DatabaseException;
    
    /**
     * Check if ISBN exists
     * @param isbn ISBN to check
     * @return true if ISBN exists
     * @throws DatabaseException if database operation fails
     */
    boolean isISBNExists(String isbn) throws DatabaseException;
    
    /**
     * Activate an item (set is_active = true)
     * @param itemId Item ID to activate
     * @return true if activation successful
     * @throws DatabaseException if database operation fails
     */
    boolean activateItem(int itemId) throws DatabaseException;
    
    /**
     * Get all items (both active and inactive)
     * @return List of all items
     * @throws DatabaseException if database operation fails
     */
    List<Item> getAllItems() throws DatabaseException;
    
    /**
     * Get inactive items only
     * @return List of inactive items
     * @throws DatabaseException if database operation fails
     */
    List<Item> getInactiveItems() throws DatabaseException;
    
    /**
     * Generate next item code
     * @return Generated item code
     * @throws DatabaseException if database operation fails
     */
    String generateItemCode() throws DatabaseException;
}