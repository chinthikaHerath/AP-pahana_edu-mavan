package com.pahanaedu.dao.interfaces;

import com.pahanaedu.model.Customer;
import com.pahanaedu.exception.DatabaseException;
import java.util.List;

/**
 * Interface for Customer Data Access Object
 * Defines all database operations for Customer entity
 */
public interface CustomerDAO {
    
    /**
     * Add a new customer to the database
     * @param customer Customer object to be added
     * @return Generated customer ID
     * @throws DatabaseException if database operation fails
     */
    int addCustomer(Customer customer) throws DatabaseException;
    
    /**
     * Update an existing customer
     * @param customer Customer object with updated information
     * @return true if update successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean updateCustomer(Customer customer) throws DatabaseException;
    
    /**
     * Deactivate a customer by ID (sets isActive to false)
     * @param customerId ID of the customer to deactivate
     * @return true if deactivation successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean deactivateCustomer(int customerId) throws DatabaseException;

    /**
     * Activate a customer by ID (sets isActive to true)
     * @param customerId ID of the customer to activate
     * @return true if activation successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean activateCustomer(int customerId) throws DatabaseException;
    
    /**
     * Get a customer by ID
     * @param customerId ID of the customer to retrieve
     * @return Customer object if found, null otherwise
     * @throws DatabaseException if database operation fails
     */
    Customer getCustomerById(int customerId) throws DatabaseException;
    
    /**
     * Get a customer by account number
     * @param accountNumber Account number to search for
     * @return Customer object if found, null otherwise
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
     * Search customers by name, phone, or account number
     * @param searchTerm Search term
     * @return List of matching customers
     * @throws DatabaseException if database operation fails
     */
    List<Customer> searchCustomers(String searchTerm) throws DatabaseException;
    
    /**
     * Get customers by city
     * @param city City name
     * @return List of customers in the specified city
     * @throws DatabaseException if database operation fails
     */
    List<Customer> getCustomersByCity(String city) throws DatabaseException;
    
    /**
     * Check if account number exists
     * @param accountNumber Account number to check
     * @return true if account number exists, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean isAccountNumberExists(String accountNumber) throws DatabaseException;
    
    /**
     * Check if NIC number exists
     * @param nicNumber NIC number to check
     * @return true if NIC exists, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean isNICExists(String nicNumber) throws DatabaseException;
    
    /**
     * Generate next account number
     * @return Generated account number
     * @throws DatabaseException if database operation fails
     */
    String generateAccountNumber() throws DatabaseException;
    
    /**
     * Get total customer count
     * @return Total number of customers
     * @throws DatabaseException if database operation fails
     */
    int getTotalCustomerCount() throws DatabaseException;
    
    /**
     * Get active customer count
     * @return Number of active customers
     * @throws DatabaseException if database operation fails
     */
    int getActiveCustomerCount() throws DatabaseException;
    
    /**
     * Get customers with pagination
     * @param offset Starting position
     * @param limit Number of records to retrieve
     * @return List of customers
     * @throws DatabaseException if database operation fails
     */
    List<Customer> getCustomersWithPagination(int offset, int limit) throws DatabaseException;
    
    /**
     * Get customer purchase summary
     * @param customerId Customer ID
     * @return Customer object with purchase summary data
     * @throws DatabaseException if database operation fails
     */
    Customer getCustomerWithPurchaseSummary(int customerId) throws DatabaseException;
    
    /**
     * Get recent customers
     * @param limit Number of recent customers to retrieve
     * @return List of recent customers
     * @throws DatabaseException if database operation fails
     */
    List<Customer> getRecentCustomers(int limit) throws DatabaseException;
}