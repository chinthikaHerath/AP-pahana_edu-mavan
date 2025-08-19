package com.pahanaedu.dao.interfaces;

import com.pahanaedu.model.Bill;
import com.pahanaedu.model.BillItem;
import com.pahanaedu.exception.DatabaseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Interface for Bill Data Access Object
 * Defines all database operations for Bill entity
 */
public interface BillDAO {
    
    /**
     * Create a new bill in the database
     * @param bill Bill object to be created
     * @return Generated bill ID
     * @throws DatabaseException if database operation fails
     */
    int createBill(Bill bill) throws DatabaseException;
    
    /**
     * Update an existing bill
     * @param bill Bill object with updated information
     * @return true if update successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean updateBill(Bill bill) throws DatabaseException;
    
    /**
     * Cancel a bill by ID
     * @param billId ID of the bill to cancel
     * @param reason Cancellation reason
     * @return true if cancellation successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean cancelBill(int billId, String reason) throws DatabaseException;
    
    /**
     * Get a bill by ID
     * @param billId ID of the bill to retrieve
     * @return Bill object if found, null otherwise
     * @throws DatabaseException if database operation fails
     */
    Bill getBillById(int billId) throws DatabaseException;
    
    /**
     * Get a bill by bill number
     * @param billNumber Bill number to search for
     * @return Bill object if found, null otherwise
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
     * Get bills by customer ID
     * @param customerId Customer ID
     * @return List of bills for the customer
     * @throws DatabaseException if database operation fails
     */
    List<Bill> getBillsByCustomer(int customerId) throws DatabaseException;
    
    /**
     * Get bills by date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of bills within date range
     * @throws DatabaseException if database operation fails
     */
    List<Bill> getBillsByDateRange(Date startDate, Date endDate) throws DatabaseException;
    
    /**
     * Get bills by payment status
     * @param paymentStatus Payment status (PAID, PENDING, PARTIAL, CANCELLED)
     * @return List of bills with specified status
     * @throws DatabaseException if database operation fails
     */
    List<Bill> getBillsByPaymentStatus(String paymentStatus) throws DatabaseException;
    
    /**
     * Get bills by payment method
     * @param paymentMethod Payment method (CASH, CARD, CHEQUE, BANK_TRANSFER)
     * @return List of bills with specified payment method
     * @throws DatabaseException if database operation fails
     */
    List<Bill> getBillsByPaymentMethod(String paymentMethod) throws DatabaseException;
    
    /**
     * Get today's bills
     * @return List of bills created today
     * @throws DatabaseException if database operation fails
     */
    List<Bill> getTodaysBills() throws DatabaseException;
    
    /**
     * Get bills created by a specific user
     * @param userId User ID
     * @return List of bills created by the user
     * @throws DatabaseException if database operation fails
     */
    List<Bill> getBillsByUser(int userId) throws DatabaseException;
    
    /**
     * Search bills by bill number or customer name
     * @param searchTerm Search term
     * @return List of matching bills
     * @throws DatabaseException if database operation fails
     */
    List<Bill> searchBills(String searchTerm) throws DatabaseException;
    
    /**
     * Get bills with pagination
     * @param offset Starting position
     * @param limit Number of records to retrieve
     * @return List of bills
     * @throws DatabaseException if database operation fails
     */
    List<Bill> getBillsWithPagination(int offset, int limit) throws DatabaseException;
    
    /**
     * Generate next bill number
     * @return Generated bill number
     * @throws DatabaseException if database operation fails
     */
    String generateBillNumber() throws DatabaseException;
    
    /**
     * Check if bill number exists
     * @param billNumber Bill number to check
     * @return true if bill number exists, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean isBillNumberExists(String billNumber) throws DatabaseException;
    
    /**
     * Get total bill count
     * @return Total number of bills
     * @throws DatabaseException if database operation fails
     */
    int getTotalBillCount() throws DatabaseException;
    
    /**
     * Get total bill count by status
     * @param paymentStatus Payment status
     * @return Number of bills with specified status
     * @throws DatabaseException if database operation fails
     */
    int getBillCountByStatus(String paymentStatus) throws DatabaseException;
    
    /**
     * Get the next sequence number for a given prefix
     * @param prefix The prefix to get sequence for
     * @return Next sequence number
     * @throws DatabaseException if database operation fails
     */
    int getNextSequenceForPrefix(String prefix) throws DatabaseException;
    
    /**
     * Get today's sales total
     * @return Total sales amount for today
     * @throws DatabaseException if database operation fails
     */
    double getTodaysSalesTotal() throws DatabaseException;
    
    /**
     * Get sales total by date range
     * @param startDate Start date
     * @param endDate End date
     * @return Total sales amount for date range
     * @throws DatabaseException if database operation fails
     */
    double getSalesTotalByDateRange(Date startDate, Date endDate) throws DatabaseException;
    
    /**
     * Get monthly sales total
     * @param year Year
     * @param month Month (1-12)
     * @return Total sales amount for the month
     * @throws DatabaseException if database operation fails
     */
    double getMonthlySalesTotal(int year, int month) throws DatabaseException;
    
    /**
     * Get recent bills
     * @param limit Number of recent bills to retrieve
     * @return List of recent bills
     * @throws DatabaseException if database operation fails
     */
    List<Bill> getRecentBills(int limit) throws DatabaseException;
    
    /**
     * Get pending bills
     * @return List of pending bills
     * @throws DatabaseException if database operation fails
     */
    List<Bill> getPendingBills() throws DatabaseException;
    
    /**
     * Update payment status
     * @param billId Bill ID
     * @param paymentStatus New payment status
     * @param notes Additional notes
     * @return true if update successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean updatePaymentStatus(int billId, String paymentStatus, String notes) throws DatabaseException;
    
    /**
     * Get bill with items
     * @param billId Bill ID
     * @return Bill object with associated items
     * @throws DatabaseException if database operation fails
     */
    Bill getBillWithItems(int billId) throws DatabaseException;
    
    /**
     * Get sales statistics
     * @return Map containing various sales statistics
     * @throws DatabaseException if database operation fails
     */
    Map<String, Object> getSalesStatistics() throws DatabaseException;
    
    /**
     * Get daily sales summary
     * @param date Date for summary
     * @return Map containing daily sales summary
     * @throws DatabaseException if database operation fails
     */
    Map<String, Object> getDailySalesSummary(Date date) throws DatabaseException;
    
    /**
     * Get top selling items for a period
     * @param startDate Start date
     * @param endDate End date
     * @param limit Number of items to retrieve
     * @return List of top selling items with quantities
     * @throws DatabaseException if database operation fails
     */
    List<Map<String, Object>> getTopSellingItems(Date startDate, Date endDate, int limit) throws DatabaseException;
    
    /**
     * Get customer purchase summary
     * @param customerId Customer ID
     * @return Map containing customer purchase statistics
     * @throws DatabaseException if database operation fails
     */
    Map<String, Object> getCustomerPurchaseSummary(int customerId) throws DatabaseException;
    
    /**
     * Check if customer has pending bills
     * @param customerId Customer ID
     * @return true if customer has pending bills, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean hasCustomerPendingBills(int customerId) throws DatabaseException;
    
    /**
     * Get overdue bills
     * @param daysOverdue Number of days overdue
     * @return List of overdue bills
     * @throws DatabaseException if database operation fails
     */
    List<Bill> getOverdueBills(int daysOverdue) throws DatabaseException;
}