package com.pahanaedu.dao.impl;

import com.pahanaedu.dao.interfaces.CustomerDAO;
import com.pahanaedu.model.Customer;
import com.pahanaedu.exception.DatabaseException;
import com.pahanaedu.util.DBConnection;
import com.pahanaedu.constant.SystemConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of CustomerDAO interface
 * Handles all database operations for Customer entity
 */
public class CustomerDAOImpl implements CustomerDAO {
    
    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }
    
    @Override
    public int addCustomer(Customer customer) throws DatabaseException {
        String sql = "INSERT INTO customers (account_number, customer_name, address, city, " +
                    "postal_code, telephone, mobile, email, nic_number, registration_date, " +
                    "is_active, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Generate account number if not provided
            if (customer.getAccountNumber() == null || customer.getAccountNumber().isEmpty()) {
                customer.setAccountNumber(generateAccountNumber());
            }
            
            pstmt.setString(1, customer.getAccountNumber());
            pstmt.setString(2, customer.getCustomerName());
            pstmt.setString(3, customer.getAddress());
            pstmt.setString(4, customer.getCity());
            pstmt.setString(5, customer.getPostalCode());
            pstmt.setString(6, customer.getTelephone());
            pstmt.setString(7, customer.getMobile());
            pstmt.setString(8, customer.getEmail());
            pstmt.setString(9, customer.getNicNumber());
            pstmt.setDate(10, new java.sql.Date(customer.getRegistrationDate().getTime()));
            pstmt.setBoolean(11, customer.isActive());
            pstmt.setInt(12, customer.getCreatedBy());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Creating customer failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new DatabaseException("Creating customer failed, no ID obtained.");
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error adding customer: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean updateCustomer(Customer customer) throws DatabaseException {
        String sql = "UPDATE customers SET customer_name = ?, address = ?, city = ?, " +
                    "postal_code = ?, telephone = ?, mobile = ?, email = ?, " +
                    "updated_at = CURRENT_TIMESTAMP WHERE customer_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customer.getCustomerName());
            pstmt.setString(2, customer.getAddress());
            pstmt.setString(3, customer.getCity());
            pstmt.setString(4, customer.getPostalCode());
            pstmt.setString(5, customer.getTelephone());
            pstmt.setString(6, customer.getMobile());
            pstmt.setString(7, customer.getEmail());
            pstmt.setInt(8, customer.getCustomerId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error updating customer: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deactivateCustomer(int customerId) throws DatabaseException {
        String sql = "UPDATE customers SET is_active = false WHERE customer_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error deactivating customer: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean activateCustomer(int customerId) throws DatabaseException {
        String sql = "UPDATE customers SET is_active = true WHERE customer_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error activating customer: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Customer getCustomerById(int customerId) throws DatabaseException {
        String sql = "SELECT c.*, u.full_name as created_by_name FROM customers c " +
                    "LEFT JOIN users u ON c.created_by = u.user_id " +
                    "WHERE c.customer_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractCustomerFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting customer by ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public Customer getCustomerByAccountNumber(String accountNumber) throws DatabaseException {
        String sql = "SELECT c.*, u.full_name as created_by_name FROM customers c " +
                    "LEFT JOIN users u ON c.created_by = u.user_id " +
                    "WHERE c.account_number = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractCustomerFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting customer by account number: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public List<Customer> getAllCustomers() throws DatabaseException {
        String sql = "SELECT c.*, u.full_name as created_by_name FROM customers c " +
                    "LEFT JOIN users u ON c.created_by = u.user_id " +
                    "ORDER BY c.customer_name";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                customers.add(extractCustomerFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting all customers: " + e.getMessage(), e);
        }
        
        return customers;
    }
    
    @Override
    public List<Customer> getActiveCustomers() throws DatabaseException {
        String sql = "SELECT c.*, u.full_name as created_by_name FROM customers c " +
                    "LEFT JOIN users u ON c.created_by = u.user_id " +
                    "WHERE c.is_active = true " +
                    "ORDER BY c.customer_name";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                customers.add(extractCustomerFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting active customers: " + e.getMessage(), e);
        }
        
        return customers;
    }
    
    @Override
    public List<Customer> searchCustomers(String searchTerm) throws DatabaseException {
        String sql = "SELECT c.*, u.full_name as created_by_name FROM customers c " +
                    "LEFT JOIN users u ON c.created_by = u.user_id " +
                    "WHERE c.account_number LIKE ? OR c.customer_name LIKE ? " +
                    "OR c.telephone LIKE ? OR c.mobile LIKE ? OR c.email LIKE ? " +
                    "ORDER BY c.customer_name";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            for (int i = 1; i <= 5; i++) {
                pstmt.setString(i, searchPattern);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(extractCustomerFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error searching customers: " + e.getMessage(), e);
        }
        
        return customers;
    }
    
    @Override
    public List<Customer> getCustomersByCity(String city) throws DatabaseException {
        String sql = "SELECT c.*, u.full_name as created_by_name FROM customers c " +
                    "LEFT JOIN users u ON c.created_by = u.user_id " +
                    "WHERE c.city = ? " +
                    "ORDER BY c.customer_name";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, city);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(extractCustomerFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting customers by city: " + e.getMessage(), e);
        }
        
        return customers;
    }
    
    @Override
    public boolean isAccountNumberExists(String accountNumber) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM customers WHERE account_number = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, accountNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error checking account number existence: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public boolean isNICExists(String nicNumber) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM customers WHERE nic_number = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nicNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error checking NIC existence: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public String generateAccountNumber() throws DatabaseException {
        String sql = "SELECT MAX(CAST(SUBSTRING(account_number, 5) AS UNSIGNED)) FROM customers " +
                    "WHERE account_number LIKE 'CUST%'";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int nextNumber = 1;
            if (rs.next() && rs.getInt(1) > 0) {
                nextNumber = rs.getInt(1) + 1;
            }
            
            return String.format(SystemConstants.ACCOUNT_NUMBER_FORMAT, nextNumber);
            
        } catch (SQLException e) {
            throw new DatabaseException("Error generating account number: " + e.getMessage(), e);
        }
    }
    
    @Override
    public int getTotalCustomerCount() throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM customers";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting total customer count: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public int getActiveCustomerCount() throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM customers WHERE is_active = true";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting active customer count: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public List<Customer> getCustomersWithPagination(int offset, int limit) throws DatabaseException {
        String sql = "SELECT c.*, u.full_name as created_by_name FROM customers c " +
                    "LEFT JOIN users u ON c.created_by = u.user_id " +
                    "ORDER BY c.customer_id DESC " +
                    "LIMIT ? OFFSET ?";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(extractCustomerFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting customers with pagination: " + e.getMessage(), e);
        }
        
        return customers;
    }
    
    @Override
    public Customer getCustomerWithPurchaseSummary(int customerId) throws DatabaseException {
        String sql = "SELECT c.*, u.full_name as created_by_name, " +
                    "COUNT(DISTINCT b.bill_id) as purchase_count, " +
                    "COALESCE(SUM(b.total_amount), 0) as total_purchases " +
                    "FROM customers c " +
                    "LEFT JOIN users u ON c.created_by = u.user_id " +
                    "LEFT JOIN bills b ON c.customer_id = b.customer_id " +
                    "WHERE c.customer_id = ? " +
                    "GROUP BY c.customer_id";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Customer customer = extractCustomerFromResultSet(rs);
                    customer.setPurchaseCount(rs.getInt("purchase_count"));
                    customer.setTotalPurchases(rs.getDouble("total_purchases"));
                    return customer;
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting customer with purchase summary: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public List<Customer> getRecentCustomers(int limit) throws DatabaseException {
        String sql = "SELECT c.*, u.full_name as created_by_name FROM customers c " +
                    "LEFT JOIN users u ON c.created_by = u.user_id " +
                    "ORDER BY c.created_at DESC " +
                    "LIMIT ?";
        List<Customer> customers = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(extractCustomerFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting recent customers: " + e.getMessage(), e);
        }
        
        return customers;
    }
    
    /**
     * Extract Customer object from ResultSet
     */
    private Customer extractCustomerFromResultSet(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setAccountNumber(rs.getString("account_number"));
        customer.setCustomerName(rs.getString("customer_name"));
        customer.setAddress(rs.getString("address"));
        customer.setCity(rs.getString("city"));
        customer.setPostalCode(rs.getString("postal_code"));
        customer.setTelephone(rs.getString("telephone"));
        customer.setMobile(rs.getString("mobile"));
        customer.setEmail(rs.getString("email"));
        customer.setNicNumber(rs.getString("nic_number"));
        customer.setRegistrationDate(rs.getDate("registration_date"));
        customer.setActive(rs.getBoolean("is_active"));
        customer.setCreatedBy(rs.getInt("created_by"));
        customer.setCreatedAt(rs.getTimestamp("created_at"));
        customer.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Set created by username if available
        try {
            customer.setCreatedByUsername(rs.getString("created_by_name"));
        } catch (SQLException e) {
            // Column might not be present in all queries
        }
        
        return customer;
    }
}