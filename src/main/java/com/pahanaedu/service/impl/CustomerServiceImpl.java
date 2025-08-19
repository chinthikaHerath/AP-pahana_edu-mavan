package com.pahanaedu.service.impl;

import com.pahanaedu.service.interfaces.CustomerService;
import com.pahanaedu.dao.interfaces.CustomerDAO;
import com.pahanaedu.dao.impl.CustomerDAOImpl;
import com.pahanaedu.model.Customer;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.exception.DatabaseException;
import com.pahanaedu.exception.BusinessException;
import com.pahanaedu.util.ValidationUtil;

import java.util.*;

/**
 * Implementation of CustomerService interface
 * Handles business logic for customer management
 */
public class CustomerServiceImpl implements CustomerService {
    
    private CustomerDAO customerDAO;
    
    public CustomerServiceImpl() {
        this.customerDAO = new CustomerDAOImpl();
    }
    
    @Override
    public int addCustomer(Customer customer, int createdBy) 
            throws ValidationException, DatabaseException, BusinessException {
        
        // Validate customer data
        validateCustomer(customer, false);
        
        // Check if NIC already exists
        if (!isNICAvailable(customer.getNicNumber(), null)) {
            throw new BusinessException("NIC number already registered with another customer");
        }
        
        // Set created by
        customer.setCreatedBy(createdBy);
        
        // Set registration date if not provided
        if (customer.getRegistrationDate() == null) {
            customer.setRegistrationDate(new Date());
        }
        
        // Add customer
        return customerDAO.addCustomer(customer);
    }
    
    @Override
    public boolean updateCustomer(Customer customer) 
            throws ValidationException, DatabaseException, BusinessException {
        
        // Validate customer data
        validateCustomer(customer, true);
        
        // Get existing customer
        Customer existingCustomer = customerDAO.getCustomerById(customer.getCustomerId());
        if (existingCustomer == null) {
            throw new BusinessException("Customer not found");
        }
        
        // Check if NIC is being changed and if new NIC is available
        if (!existingCustomer.getNicNumber().equals(customer.getNicNumber())) {
            if (!isNICAvailable(customer.getNicNumber(), customer.getCustomerId())) {
                throw new BusinessException("NIC number already registered with another customer");
            }
        }
        
        // Update customer
        return customerDAO.updateCustomer(customer);
    }
    
    @Override
    public boolean activateCustomer(int customerId) 
            throws DatabaseException, BusinessException {
        
        // Check if customer exists
        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null) {
            throw new BusinessException("Customer not found");
        }
        
        // Check if already active
        if (customer.isActive()) {
            throw new BusinessException("Customer is already active");
        }
        
        // Activate the customer
        return customerDAO.activateCustomer(customerId);
    }

    @Override
    public boolean deactivateCustomer(int customerId) 
            throws DatabaseException, BusinessException {
        
        // Check if customer exists
        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null) {
            throw new BusinessException("Customer not found");
        }
        
        // Check if already inactive
        if (!customer.isActive()) {
            throw new BusinessException("Customer is already inactive");
        }
        
        // TODO: Check if customer has pending bills before deactivation
        // This will be implemented when BillDAO is available
        // For now, we'll allow deactivation
        
        // Deactivate the customer
        return customerDAO.deactivateCustomer(customerId);
    }
    
    
    @Override
    public Customer getCustomerById(int customerId) throws DatabaseException {
        return customerDAO.getCustomerById(customerId);
    }
    
    @Override
    public Customer getCustomerByAccountNumber(String accountNumber) throws DatabaseException {
        if (ValidationUtil.isNullOrEmpty(accountNumber)) {
            return null;
        }
        return customerDAO.getCustomerByAccountNumber(accountNumber);
    }
    
    @Override
    public List<Customer> getAllCustomers() throws DatabaseException {
        return customerDAO.getAllCustomers();
    }
    
    @Override
    public List<Customer> getActiveCustomers() throws DatabaseException {
        return customerDAO.getActiveCustomers();
    }
    
    @Override
    public List<Customer> searchCustomers(String searchTerm) throws DatabaseException {
        if (ValidationUtil.isNullOrEmpty(searchTerm)) {
            return new ArrayList<>();
        }
        return customerDAO.searchCustomers(searchTerm.trim());
    }
    
    @Override
    public List<Customer> getCustomersByCity(String city) throws DatabaseException {
        if (ValidationUtil.isNullOrEmpty(city)) {
            return new ArrayList<>();
        }
        return customerDAO.getCustomersByCity(city);
    }
    
    @Override
    public void validateCustomer(Customer customer, boolean isUpdate) throws ValidationException {
        ValidationException validationException = new ValidationException("Customer validation failed");
        
        // Validate customer name
        if (ValidationUtil.isNullOrEmpty(customer.getCustomerName())) {
            validationException.addFieldError("customerName", "Customer name is required");
        } else if (!ValidationUtil.isValidLength(customer.getCustomerName(), 3, 100)) {
            validationException.addFieldError("customerName", "Customer name must be 3-100 characters");
        }
        
        // Validate address
        if (ValidationUtil.isNullOrEmpty(customer.getAddress())) {
            validationException.addFieldError("address", "Address is required");
        } else if (!ValidationUtil.isValidLength(customer.getAddress(), 5, 255)) {
            validationException.addFieldError("address", "Address must be 5-255 characters");
        }
        
        // Validate telephone
        if (ValidationUtil.isNullOrEmpty(customer.getTelephone())) {
            validationException.addFieldError("telephone", "Telephone is required");
        } else if (!ValidationUtil.isValidPhone(customer.getTelephone())) {
            validationException.addFieldError("telephone", "Invalid telephone format (10 digits required)");
        }
        
        // Validate mobile (optional)
        if (!ValidationUtil.isNullOrEmpty(customer.getMobile()) && 
            !ValidationUtil.isValidPhone(customer.getMobile())) {
            validationException.addFieldError("mobile", "Invalid mobile format (10 digits required)");
        }
        
        // Validate email (optional)
        if (!ValidationUtil.isNullOrEmpty(customer.getEmail()) && 
            !ValidationUtil.isValidEmail(customer.getEmail())) {
            validationException.addFieldError("email", "Invalid email format");
        }
        
        // Validate NIC
        if (!isUpdate) {
            if (ValidationUtil.isNullOrEmpty(customer.getNicNumber())) {
                validationException.addFieldError("nicNumber", "NIC number is required");
            } else if (!ValidationUtil.isValidNIC(customer.getNicNumber())) {
                validationException.addFieldError("nicNumber", "Invalid NIC format");
            }
        }
        
        // Validate city (optional but recommended)
        if (!ValidationUtil.isNullOrEmpty(customer.getCity()) && 
            !ValidationUtil.isValidLength(customer.getCity(), 2, 50)) {
            validationException.addFieldError("city", "City must be 2-50 characters");
        }
        
        // Validate postal code (optional)
        if (!ValidationUtil.isNullOrEmpty(customer.getPostalCode()) && 
            !ValidationUtil.isValidLength(customer.getPostalCode(), 4, 10)) {
            validationException.addFieldError("postalCode", "Postal code must be 4-10 characters");
        }
        
        // Throw exception if there are validation errors
        if (validationException.hasFieldErrors()) {
            throw validationException;
        }
    }
    
    @Override
    public boolean isAccountNumberAvailable(String accountNumber) throws DatabaseException {
        return !customerDAO.isAccountNumberExists(accountNumber);
    }
    
    @Override
    public boolean isNICAvailable(String nicNumber, Integer excludeCustomerId) throws DatabaseException {
        if (ValidationUtil.isNullOrEmpty(nicNumber)) {
            return true;
        }
        
        // If updating, check if NIC belongs to another customer
        if (excludeCustomerId != null) {
            Customer existingCustomer = customerDAO.getCustomerById(excludeCustomerId);
            if (existingCustomer != null && nicNumber.equals(existingCustomer.getNicNumber())) {
                return true; // Same NIC for same customer is okay
            }
        }
        
        return !customerDAO.isNICExists(nicNumber);
    }
    
    @Override
    public Map<String, Object> getCustomerStatistics() throws DatabaseException {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCustomers", customerDAO.getTotalCustomerCount());
        stats.put("activeCustomers", customerDAO.getActiveCustomerCount());
        stats.put("inactiveCustomers", 
            customerDAO.getTotalCustomerCount() - customerDAO.getActiveCustomerCount());
        return stats;
    }
    
    @Override
    public List<Customer> getCustomersWithPagination(int page, int pageSize) throws DatabaseException {
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;
        
        int offset = (page - 1) * pageSize;
        return customerDAO.getCustomersWithPagination(offset, pageSize);
    }
    
    @Override
    public Customer getCustomerWithPurchaseSummary(int customerId) throws DatabaseException {
        return customerDAO.getCustomerWithPurchaseSummary(customerId);
    }
    
    @Override
    public List<Customer> getRecentCustomers(int limit) throws DatabaseException {
        if (limit < 1) limit = 5;
        return customerDAO.getRecentCustomers(limit);
    }
    
    @Override
    public String exportCustomersToCSV(List<Customer> customers) {
        StringBuilder csv = new StringBuilder();
        
        // Header
        csv.append("Account Number,Customer Name,Address,City,Postal Code,");
        csv.append("Telephone,Mobile,Email,NIC Number,Registration Date,Status\n");
        
        // Data
        for (Customer customer : customers) {
            csv.append(escapeCSV(customer.getAccountNumber())).append(",");
            csv.append(escapeCSV(customer.getCustomerName())).append(",");
            csv.append(escapeCSV(customer.getAddress())).append(",");
            csv.append(escapeCSV(customer.getCity())).append(",");
            csv.append(escapeCSV(customer.getPostalCode())).append(",");
            csv.append(escapeCSV(customer.getTelephone())).append(",");
            csv.append(escapeCSV(customer.getMobile())).append(",");
            csv.append(escapeCSV(customer.getEmail())).append(",");
            csv.append(escapeCSV(customer.getNicNumber())).append(",");
            csv.append(customer.getRegistrationDate()).append(",");
            csv.append(customer.isActive() ? "Active" : "Inactive").append("\n");
        }
        
        return csv.toString();
    }
    
    @Override
    public List<String> getAllCities() throws DatabaseException {
        List<Customer> allCustomers = customerDAO.getAllCustomers();
        Set<String> cities = new TreeSet<>(); // TreeSet for automatic sorting
        
        for (Customer customer : allCustomers) {
            if (!ValidationUtil.isNullOrEmpty(customer.getCity())) {
                cities.add(customer.getCity());
            }
        }
        
        return new ArrayList<>(cities);
    }
    
    /**
     * Escape CSV field if it contains special characters
     */
    private String escapeCSV(String field) {
        if (field == null) return "";
        
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}