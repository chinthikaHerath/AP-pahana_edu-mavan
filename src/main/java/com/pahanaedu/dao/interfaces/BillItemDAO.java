package com.pahanaedu.dao.interfaces;

import com.pahanaedu.model.BillItem;
import com.pahanaedu.exception.DatabaseException;
import java.util.List;
import java.util.Map;

/**
 * Interface for BillItem Data Access Object
 * Defines all database operations for BillItem entity
 */
public interface BillItemDAO {
    
    /**
     * Add a bill item to the database
     * @param billItem BillItem object to be added
     * @return Generated bill item ID
     * @throws DatabaseException if database operation fails
     */
    int addBillItem(BillItem billItem) throws DatabaseException;
    
    /**
     * Add multiple bill items in a batch
     * @param billItems List of bill items to add
     * @return Number of items added
     * @throws DatabaseException if database operation fails
     */
    int addBillItemsBatch(List<BillItem> billItems) throws DatabaseException;
    
    /**
     * Update a bill item
     * @param billItem BillItem object with updated information
     * @return true if update successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean updateBillItem(BillItem billItem) throws DatabaseException;
    
    /**
     * Delete a bill item by ID
     * @param billItemId ID of the bill item to delete
     * @return true if deletion successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean deleteBillItem(int billItemId) throws DatabaseException;
    
    /**
     * Delete all bill items for a specific bill
     * @param billId Bill ID
     * @return Number of items deleted
     * @throws DatabaseException if database operation fails
     */
    int deleteBillItemsByBillId(int billId) throws DatabaseException;
    
    /**
     * Get a bill item by ID
     * @param billItemId ID of the bill item to retrieve
     * @return BillItem object if found, null otherwise
     * @throws DatabaseException if database operation fails
     */
    BillItem getBillItemById(int billItemId) throws DatabaseException;
    
    /**
     * Get all bill items for a specific bill
     * @param billId Bill ID
     * @return List of bill items
     * @throws DatabaseException if database operation fails
     */
    List<BillItem> getBillItemsByBillId(int billId) throws DatabaseException;
    
    /**
     * Get all bill items for a specific item
     * @param itemId Item ID
     * @return List of bill items
     * @throws DatabaseException if database operation fails
     */
    List<BillItem> getBillItemsByItemId(int itemId) throws DatabaseException;
    
    /**
     * Get total quantity sold for an item
     * @param itemId Item ID
     * @return Total quantity sold
     * @throws DatabaseException if database operation fails
     */
    int getTotalQuantitySold(int itemId) throws DatabaseException;
    
    /**
     * Get total revenue for an item
     * @param itemId Item ID
     * @return Total revenue
     * @throws DatabaseException if database operation fails
     */
    double getTotalRevenue(int itemId) throws DatabaseException;
    
    /**
     * Check if an item exists in a bill
     * @param billId Bill ID
     * @param itemId Item ID
     * @return true if item exists in bill, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean isItemInBill(int billId, int itemId) throws DatabaseException;
    
    /**
     * Get item count for a bill
     * @param billId Bill ID
     * @return Number of different items in the bill
     * @throws DatabaseException if database operation fails
     */
    int getItemCountForBill(int billId) throws DatabaseException;
    
    /**
     * Get total quantity for a bill
     * @param billId Bill ID
     * @return Total quantity of all items in the bill
     * @throws DatabaseException if database operation fails
     */
    int getTotalQuantityForBill(int billId) throws DatabaseException;
    
    /**
     * Update bill item quantity
     * @param billItemId Bill item ID
     * @param newQuantity New quantity
     * @return true if update successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean updateBillItemQuantity(int billItemId, int newQuantity) throws DatabaseException;
    
    /**
     * Update bill item discount
     * @param billItemId Bill item ID
     * @param discountPercentage Discount percentage
     * @return true if update successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean updateBillItemDiscount(int billItemId, double discountPercentage) throws DatabaseException;
    
    /**
     * Get sales summary by item for a date range
     * @param startDate Start date (as string YYYY-MM-DD)
     * @param endDate End date (as string YYYY-MM-DD)
     * @return List of maps containing item sales data
     * @throws DatabaseException if database operation fails
     */
    List<Map<String, Object>> getItemSalesSummary(String startDate, String endDate) throws DatabaseException;
    
    /**
     * Get most sold items
     * @param limit Number of items to retrieve
     * @return List of maps containing item data with quantities
     * @throws DatabaseException if database operation fails
     */
    List<Map<String, Object>> getMostSoldItems(int limit) throws DatabaseException;
    
    /**
     * Get least sold items
     * @param limit Number of items to retrieve
     * @return List of maps containing item data with quantities
     * @throws DatabaseException if database operation fails
     */
    List<Map<String, Object>> getLeastSoldItems(int limit) throws DatabaseException;
    
    /**
     * Get items never sold
     * @return List of items that have never been sold
     * @throws DatabaseException if database operation fails
     */
    List<Map<String, Object>> getItemsNeverSold() throws DatabaseException;
    
    /**
     * Get revenue by category
     * @param startDate Start date (as string YYYY-MM-DD)
     * @param endDate End date (as string YYYY-MM-DD)
     * @return List of maps containing category revenue data
     * @throws DatabaseException if database operation fails
     */
    List<Map<String, Object>> getRevenueByCategory(String startDate, String endDate) throws DatabaseException;
    
    /**
     * Replace all bill items for a bill
     * This will delete existing items and add new ones
     * @param billId Bill ID
     * @param newItems New list of bill items
     * @return true if replacement successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean replaceBillItems(int billId, List<BillItem> newItems) throws DatabaseException;
    
    /**
     * Get bill items with full item details
     * @param billId Bill ID
     * @return List of bill items with item information populated
     * @throws DatabaseException if database operation fails
     */
    List<BillItem> getBillItemsWithDetails(int billId) throws DatabaseException;
    
    /**
     * Calculate and update total price for a bill item
     * @param billItemId Bill item ID
     * @return Updated total price
     * @throws DatabaseException if database operation fails
     */
    double recalculateBillItemTotal(int billItemId) throws DatabaseException;
    
    /**
     * Get discounted items in a bill
     * @param billId Bill ID
     * @return List of bill items that have discounts
     * @throws DatabaseException if database operation fails
     */
    List<BillItem> getDiscountedItems(int billId) throws DatabaseException;
    
    /**
     * Get total discount amount for a bill
     * @param billId Bill ID
     * @return Total discount amount across all items
     * @throws DatabaseException if database operation fails
     */
    double getTotalDiscountForBill(int billId) throws DatabaseException;
}