package com.pahanaedu.service.interfaces;

import com.pahanaedu.model.Bill;
import com.pahanaedu.model.BillItem;
import com.pahanaedu.model.Customer;
import com.pahanaedu.model.Item;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.exception.DatabaseException;
import com.pahanaedu.exception.BusinessException;
import com.pahanaedu.dto.BillDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Interface for Billing Service
 * Handles business logic for billing operations
 */
public interface BillingService {
    
    /**
     * Create a new bill
     * @param bill Bill object with items
     * @param userId User creating the bill
     * @return Created bill ID
     * @throws ValidationException if validation fails
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if business rule violation occurs
     */
    int createBill(Bill bill, int userId) throws ValidationException, DatabaseException, BusinessException;
    
    /**
     * Create a bill from DTO
     * @param billDTO Bill data transfer object
     * @param userId User creating the bill
     * @return Created bill with generated number
     * @throws ValidationException if validation fails
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if business rule violation occurs
     */
    Bill createBillFromDTO(BillDTO billDTO, int userId) 
            throws ValidationException, DatabaseException, BusinessException;
    
    /**
     * Update an existing bill
     * @param bill Bill with updated information
     * @return true if update successful
     * @throws ValidationException if validation fails
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if business rule violation occurs
     */
    boolean updateBill(Bill bill) throws ValidationException, DatabaseException, BusinessException;
    
    /**
     * Cancel a bill
     * @param billId Bill ID to cancel
     * @param reason Cancellation reason
     * @param userId User cancelling the bill
     * @return true if cancellation successful
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if bill cannot be cancelled
     */
    boolean cancelBill(int billId, String reason, int userId) 
            throws DatabaseException, BusinessException;
    
    /**
     * Get bill by ID
     * @param billId Bill ID
     * @return Bill object with items or null if not found
     * @throws DatabaseException if database operation fails
     */
    Bill getBillById(int billId) throws DatabaseException;
    
    /**
     * Get bill by bill number
     * @param billNumber Bill number
     * @return Bill object with items or null if not found
     * @throws DatabaseException if database operation fails
     */
    Bill getBillByNumber(String billNumber) throws DatabaseException;
    
    /**
     * Get all bills
     * @return List of all bills
     * @throws DatabaseException if database operation fails
     */
    List<Bill> getAllBills() throws DatabaseException;
    
    /**
     * Get bills for a customer
     * @param customerId Customer ID
     * @return List of customer's bills
     * @throws DatabaseException if database operation fails
     */
    List<Bill> getCustomerBills(int customerId) throws DatabaseException;
    
    /**
     * Get bills by date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of bills in date range
     * @throws DatabaseException if database operation fails
     */
    List<Bill> getBillsByDateRange(Date startDate, Date endDate) throws DatabaseException;
    
    /**
     * Get today's bills
     * @return List of bills created today
     * @throws DatabaseException if database operation fails
     */
    List<Bill> getTodaysBills() throws DatabaseException;
    
    /**
     * Get pending bills
     * @return List of pending or partial payment bills
     * @throws DatabaseException if database operation fails
     */
    List<Bill> getPendingBills() throws DatabaseException;
    
    /**
     * Search bills
     * @param searchTerm Search term (bill number, customer name)
     * @return List of matching bills
     * @throws DatabaseException if database operation fails
     */
    List<Bill> searchBills(String searchTerm) throws DatabaseException;
    
    /**
     * Add item to bill
     * @param billId Bill ID
     * @param item Item to add
     * @param quantity Quantity
     * @param discountPercentage Discount percentage (optional)
     * @return Added bill item
     * @throws ValidationException if validation fails
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if item cannot be added
     */
    BillItem addItemToBill(int billId, Item item, int quantity, double discountPercentage)
            throws ValidationException, DatabaseException, BusinessException;
    
    /**
     * Update bill item quantity
     * @param billItemId Bill item ID
     * @param newQuantity New quantity
     * @return true if update successful
     * @throws ValidationException if validation fails
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if update not allowed
     */
    boolean updateBillItemQuantity(int billItemId, int newQuantity)
            throws ValidationException, DatabaseException, BusinessException;
    
    /**
     * Remove item from bill
     * @param billId Bill ID
     * @param billItemId Bill item ID
     * @return true if removal successful
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if removal not allowed
     */
    boolean removeItemFromBill(int billId, int billItemId)
            throws DatabaseException, BusinessException;
    
    /**
     * Apply discount to bill
     * @param billId Bill ID
     * @param discountPercentage Discount percentage
     * @return Updated bill
     * @throws ValidationException if validation fails
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if discount cannot be applied
     */
    Bill applyBillDiscount(int billId, double discountPercentage)
            throws ValidationException, DatabaseException, BusinessException;
    
    /**
     * Update payment status
     * @param billId Bill ID
     * @param newStatus New payment status
     * @param notes Additional notes
     * @return true if update successful
     * @throws ValidationException if validation fails
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if status change not allowed
     */
    boolean updatePaymentStatus(int billId, String newStatus, String notes)
            throws ValidationException, DatabaseException, BusinessException;
    
    /**
     * Process payment for bill
     * @param billId Bill ID
     * @param paymentMethod Payment method
     * @param paymentReference Payment reference (cheque number, transaction ID)
     * @return true if payment successful
     * @throws ValidationException if validation fails
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if payment cannot be processed
     */
    boolean processPayment(int billId, String paymentMethod, String paymentReference)
            throws ValidationException, DatabaseException, BusinessException;
    
    /**
     * Validate bill before creation/update
     * @param bill Bill to validate
     * @param isUpdate true if updating existing bill
     * @throws ValidationException if validation fails
     */
    void validateBill(Bill bill, boolean isUpdate) throws ValidationException;
    
    /**
     * Check if customer can be billed
     * @param customerId Customer ID
     * @return true if customer can be billed
     * @throws DatabaseException if database operation fails
     */
    boolean canCustomerBeBilled(int customerId) throws DatabaseException;
    
    /**
     * Check item availability for billing
     * @param itemId Item ID
     * @param quantity Required quantity
     * @return true if item is available
     * @throws DatabaseException if database operation fails
     */
    boolean isItemAvailable(int itemId, int quantity) throws DatabaseException;
    
    /**
     * Get bill statistics
     * @return Map containing various bill statistics
     * @throws DatabaseException if database operation fails
     */
    Map<String, Object> getBillStatistics() throws DatabaseException;
    
    /**
     * Get daily sales summary
     * @param date Date for summary
     * @return Map containing daily sales data
     * @throws DatabaseException if database operation fails
     */
    Map<String, Object> getDailySalesSummary(Date date) throws DatabaseException;
    
    /**
     * Get monthly sales summary
     * @param year Year
     * @param month Month (1-12)
     * @return Map containing monthly sales data
     * @throws DatabaseException if database operation fails
     */
    Map<String, Object> getMonthlySalesSummary(int year, int month) throws DatabaseException;
    
    /**
     * Get top selling items
     * @param startDate Start date
     * @param endDate End date
     * @param limit Number of items to retrieve
     * @return List of top selling items with sales data
     * @throws DatabaseException if database operation fails
     */
    List<Map<String, Object>> getTopSellingItems(Date startDate, Date endDate, int limit)
            throws DatabaseException;
    
    /**
     * Get sales by category
     * @param startDate Start date
     * @param endDate End date
     * @return List of categories with sales data
     * @throws DatabaseException if database operation fails
     */
    List<Map<String, Object>> getSalesByCategory(Date startDate, Date endDate)
            throws DatabaseException;
    
    /**
     * Get customer purchase summary
     * @param customerId Customer ID
     * @return Map containing customer purchase statistics
     * @throws DatabaseException if database operation fails
     */
    Map<String, Object> getCustomerPurchaseSummary(int customerId) throws DatabaseException;
    
    /**
     * Get overdue bills
     * @param daysOverdue Number of days overdue
     * @return List of overdue bills
     * @throws DatabaseException if database operation fails
     */
    List<Bill> getOverdueBills(int daysOverdue) throws DatabaseException;
    
    /**
     * Export bill to PDF
     * @param billId Bill ID
     * @return PDF byte array
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if export fails
     */
    byte[] exportBillToPDF(int billId) throws DatabaseException, BusinessException;
    
    /**
     * Export bills to CSV
     * @param bills List of bills to export
     * @return CSV string
     */
    String exportBillsToCSV(List<Bill> bills);
    
    /**
     * Calculate bill totals
     * @param bill Bill to calculate
     * @return Bill with updated totals
     */
    Bill calculateBillTotals(Bill bill);
    
    /**
     * Get bills with pagination
     * @param page Page number (starts from 1)
     * @param pageSize Number of records per page
     * @return List of bills
     * @throws DatabaseException if database operation fails
     */
    List<Bill> getBillsWithPagination(int page, int pageSize) throws DatabaseException;
    
    /**
     * Get recent bills
     * @param limit Number of bills to retrieve
     * @return List of recent bills
     * @throws DatabaseException if database operation fails
     */
    List<Bill> getRecentBills(int limit) throws DatabaseException;
    
    /**
     * Check if bill can be edited
     * @param billId Bill ID
     * @return true if bill can be edited
     * @throws DatabaseException if database operation fails
     */
    boolean isBillEditable(int billId) throws DatabaseException;
    
    /**
     * Check if bill can be cancelled
     * @param billId Bill ID
     * @return true if bill can be cancelled
     * @throws DatabaseException if database operation fails
     */
    boolean isBillCancellable(int billId) throws DatabaseException;
    
    /**
     * Get next bill number
     * @return Generated bill number
     * @throws DatabaseException if database operation fails
     */
    String getNextBillNumber() throws DatabaseException;
    
    /**
     * Send bill by email
     * @param billId Bill ID
     * @param recipientEmail Recipient email address
     * @return true if email sent successfully
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if email cannot be sent
     */
    boolean sendBillByEmail(int billId, String recipientEmail) 
            throws DatabaseException, BusinessException;
    
}