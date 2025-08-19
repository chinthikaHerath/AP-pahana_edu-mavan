package com.pahanaedu.service.interfaces;

import com.pahanaedu.model.Customer;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.exception.DatabaseException;
import com.pahanaedu.exception.BusinessException;
import java.util.List;
import java.util.Map;

/**
 * Interface for Customer Service
 * Handles business logic for customer management
 */
public interface CustomerService {
    
    /**
     * Add a new customer
     * @param customer Customer object to add
     * @param createdBy User ID who is creating the customer
     * @return Created customer ID
     * @throws ValidationException if validation fails
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if business rule violation occurs
     */
    int addCustomer(Customer customer, int createdBy) 
            throws ValidationException, DatabaseException, BusinessException;
    
    /**
     * Update an existing customer
     * @param customer Customer object with updated information
     * @return true if update successful
     * @throws ValidationException if validation fails
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if business rule violation occurs
     */
    boolean updateCustomer(Customer customer) 
            throws ValidationException, DatabaseException, BusinessException;
    
    /**
     * Activate a customer
     * @param customerId Customer ID to activate
     * @return true if activation successful
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if business rule violation occurs
     */
    boolean activateCustomer(int customerId) throws DatabaseException, BusinessException;

    /**
     * Deactivate a customer (soft delete)
     * @param customerId Customer ID to deactivate
     * @return true if deactivation successful
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if customer has pending bills
     */
    boolean deactivateCustomer(int customerId) throws DatabaseException, BusinessException;
    
    /**
     * Get customer by ID
     * @param customerId Customer ID
     * @return Customer object or null if not found
     * @throws DatabaseException if database operation fails
     */
    Customer getCustomerById(int customerId) throws DatabaseException;
    
    /**
     * Get customer by account number
     * @param accountNumber Customer account number
     * @return Customer object or null if not found
     * @throws DatabaseException if database operation fails
     */
    Customer getCustomerByAccountNumber(String accountNumber) throws DatabaseException;
    
    /**
     * Get all customers
     * @return List of all customers
     * @throws DatabaseException if database operation fails
     */
    List<Customer> getAllCustomers() throws DatabaseException;
    
    /**
     * Get all active customers
     * @return List of active customers
     * @throws DatabaseException if database operation fails
     */
    List<Customer> getActiveCustomers() throws DatabaseException;
    
    /**
     * Search customers
     * @param searchTerm Search term (name, phone, account number)
     * @return List of matching customers
     * @throws DatabaseException if database operation fails
     */
    List<Customer> searchCustomers(String searchTerm) throws DatabaseException;
    
    /**
     * Get customers by city
     * @param city City name
     * @return List of customers in the city
     * @throws DatabaseException if database operation fails
     */
    List<Customer> getCustomersByCity(String city) throws DatabaseException;
    
    /**
     * Validate customer data
     * @param customer Customer object to validate
     * @param isUpdate true if updating existing customer
     * @throws ValidationException if validation fails
     */
    void validateCustomer(Customer customer, boolean isUpdate) throws ValidationException;
    
    /**
     * Check if account number is available
     * @param accountNumber Account number to check
     * @return true if available
     * @throws DatabaseException if database operation fails
     */
    boolean isAccountNumberAvailable(String accountNumber) throws DatabaseException;
    
    /**
     * Check if NIC is available
     * @param nicNumber NIC to check
     * @param excludeCustomerId Customer ID to exclude (for updates)
     * @return true if available
     * @throws DatabaseException if database operation fails
     */
    boolean isNICAvailable(String nicNumber, Integer excludeCustomerId) throws DatabaseException;
    
    /**
     * Get customer statistics
     * @return Map containing total count, active count, etc.
     * @throws DatabaseException if database operation fails
     */
    Map<String, Object> getCustomerStatistics() throws DatabaseException;
    
    /**
     * Get customers with pagination
     * @param page Page number (starts from 1)
     * @param pageSize Number of records per page
     * @return List of customers
     * @throws DatabaseException if database operation fails
     */
    List<Customer> getCustomersWithPagination(int page, int pageSize) throws DatabaseException;
    
    /**
     * Get customer with purchase summary
     * @param customerId Customer ID
     * @return Customer with purchase data
     * @throws DatabaseException if database operation fails
     */
    Customer getCustomerWithPurchaseSummary(int customerId) throws DatabaseException;
    
    /**
     * Get recent customers
     * @param limit Number of customers to retrieve
     * @return List of recent customers
     * @throws DatabaseException if database operation fails
     */
    List<Customer> getRecentCustomers(int limit) throws DatabaseException;
    
    /**
     * Export customers to CSV format
     * @param customers List of customers to export
     * @return CSV string
     */
    String exportCustomersToCSV(List<Customer> customers);
    
    /**
     * Get all cities from customers
     * @return List of unique cities
     * @throws DatabaseException if database operation fails
     */
    List<String> getAllCities() throws DatabaseException;
}