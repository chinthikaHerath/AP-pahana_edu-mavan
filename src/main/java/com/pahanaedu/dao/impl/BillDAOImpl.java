package com.pahanaedu.dao.impl;

import com.pahanaedu.dao.interfaces.BillDAO;
import com.pahanaedu.dao.interfaces.BillItemDAO;
import com.pahanaedu.model.Bill;
import com.pahanaedu.model.BillItem;
import com.pahanaedu.model.Customer;
import com.pahanaedu.exception.DatabaseException;
import com.pahanaedu.util.DBConnection;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Implementation of BillDAO interface
 * Handles all database operations for Bill entity
 */
public class BillDAOImpl implements BillDAO {
    
    private BillItemDAO billItemDAO;
    
    public BillDAOImpl() {
        this.billItemDAO = new BillItemDAOImpl();
    }
    
    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }
    
    @Override
    public int createBill(Bill bill) throws DatabaseException {
        String sql = "INSERT INTO bills (bill_number, customer_id, user_id, bill_date, bill_time, " +
                    "subtotal, discount_percentage, discount_amount, tax_percentage, tax_amount, " +
                    "total_amount, payment_method, payment_status, notes) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // Generate bill number if not provided
            if (bill.getBillNumber() == null || bill.getBillNumber().isEmpty()) {
                bill.setBillNumber(generateBillNumber());
            }
            
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, bill.getBillNumber());
            pstmt.setInt(2, bill.getCustomerId());
            pstmt.setInt(3, bill.getUserId());
            pstmt.setDate(4, new java.sql.Date(bill.getBillDate().getTime()));
            pstmt.setTime(5, new java.sql.Time(bill.getBillTime().getTime()));
            pstmt.setDouble(6, bill.getSubtotal());
            pstmt.setDouble(7, bill.getDiscountPercentage());
            pstmt.setDouble(8, bill.getDiscountAmount());
            pstmt.setDouble(9, bill.getTaxPercentage());
            pstmt.setDouble(10, bill.getTaxAmount());
            pstmt.setDouble(11, bill.getTotalAmount());
            pstmt.setString(12, bill.getPaymentMethod());
            pstmt.setString(13, bill.getPaymentStatus());
            pstmt.setString(14, bill.getNotes());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Creating bill failed, no rows affected.");
            }
            
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int billId = generatedKeys.getInt(1);
                bill.setBillId(billId);
                
                // Save bill items if present - USE THE SAME CONNECTION
                if (bill.getBillItems() != null && !bill.getBillItems().isEmpty()) {
                    String itemSql = "INSERT INTO bill_items (bill_id, item_id, quantity, unit_price, " +
                                    "discount_percentage, discount_amount, total_price) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    
                    try (PreparedStatement itemPstmt = conn.prepareStatement(itemSql)) {
                        for (BillItem item : bill.getBillItems()) {
                            item.setBillId(billId);
                            
                            // Calculate total price if not set
                            if (item.getTotalPrice() == 0) {
                                item.calculateTotalPrice();
                            }
                            
                            itemPstmt.setInt(1, billId);
                            itemPstmt.setInt(2, item.getItemId());
                            itemPstmt.setInt(3, item.getQuantity());
                            itemPstmt.setDouble(4, item.getUnitPrice());
                            itemPstmt.setDouble(5, item.getDiscountPercentage());
                            itemPstmt.setDouble(6, item.getDiscountAmount());
                            itemPstmt.setDouble(7, item.getTotalPrice());
                            
                            itemPstmt.executeUpdate();
                        }
                    }
                }
                
                conn.commit();
                return billId;
            } else {
                throw new DatabaseException("Creating bill failed, no ID obtained.");
            }
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DatabaseException("Error rolling back transaction: " + ex.getMessage(), ex);
                }
            }
            throw new DatabaseException("Error creating bill: " + e.getMessage(), e);
        } finally {
            closeResources(conn, pstmt, generatedKeys);
        }
    }
    
    @Override
    public boolean updateBill(Bill bill) throws DatabaseException {
        String sql = "UPDATE bills SET customer_id = ?, subtotal = ?, discount_percentage = ?, " +
                    "discount_amount = ?, tax_percentage = ?, tax_amount = ?, total_amount = ?, " +
                    "payment_method = ?, payment_status = ?, notes = ? WHERE bill_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, bill.getCustomerId());
            pstmt.setDouble(2, bill.getSubtotal());
            pstmt.setDouble(3, bill.getDiscountPercentage());
            pstmt.setDouble(4, bill.getDiscountAmount());
            pstmt.setDouble(5, bill.getTaxPercentage());
            pstmt.setDouble(6, bill.getTaxAmount());
            pstmt.setDouble(7, bill.getTotalAmount());
            pstmt.setString(8, bill.getPaymentMethod());
            pstmt.setString(9, bill.getPaymentStatus());
            pstmt.setString(10, bill.getNotes());
            pstmt.setInt(11, bill.getBillId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error updating bill: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean cancelBill(int billId, String reason) throws DatabaseException {
        String sql = "UPDATE bills SET payment_status = 'CANCELLED', notes = CONCAT(IFNULL(notes, ''), ' | Cancelled: ', ?) " +
                    "WHERE bill_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, reason);
            pstmt.setInt(2, billId);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error cancelling bill: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Bill getBillById(int billId) throws DatabaseException {
        String sql = "SELECT b.*, c.customer_name, c.account_number, u.full_name as user_name " +
                    "FROM bills b " +
                    "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
                    "LEFT JOIN users u ON b.user_id = u.user_id " +
                    "WHERE b.bill_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, billId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractBillFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting bill by ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public Bill getBillByNumber(String billNumber) throws DatabaseException {
        String sql = "SELECT b.*, c.customer_name, c.account_number, u.full_name as user_name " +
                    "FROM bills b " +
                    "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
                    "LEFT JOIN users u ON b.user_id = u.user_id " +
                    "WHERE b.bill_number = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, billNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractBillFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting bill by number: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public List<Bill> getAllBills() throws DatabaseException {
        String sql = "SELECT b.*, c.customer_name, c.account_number, u.full_name as user_name " +
                    "FROM bills b " +
                    "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
                    "LEFT JOIN users u ON b.user_id = u.user_id " +
                    "ORDER BY b.bill_date DESC, b.bill_time DESC";
        
        List<Bill> bills = new ArrayList<>();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Bill bill = extractBillFromResultSet(rs);
                
                // Get item count with separate query
                String countSql = "SELECT COUNT(*) FROM bill_items WHERE bill_id = ?";
                try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
                    countStmt.setInt(1, bill.getBillId());
                    try (ResultSet countRs = countStmt.executeQuery()) {
                        if (countRs.next()) {
                            bill.setItemCount(countRs.getInt(1));
                        }
                    }
                }
                
                bills.add(bill);
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting all bills: " + e.getMessage(), e);
        }
        
        return bills;
    }
    
    
    @Override
    public int getNextSequenceForPrefix(String prefix) throws DatabaseException {
        String sql = "SELECT MAX(CAST(SUBSTRING(bill_number, LENGTH(?) + 1) AS UNSIGNED)) " +
                     "FROM bills WHERE bill_number LIKE ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, prefix);
            pstmt.setString(2, prefix + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int maxSequence = rs.getInt(1);
                    // If no bills exist with this prefix, rs.getInt(1) returns 0
                    return maxSequence + 1;
                }
            }
            
            return 1; // Start from 1 if no bills with this prefix exist
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting next sequence for prefix: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Bill> getBillsByCustomer(int customerId) throws DatabaseException {
        String sql = "SELECT b.*, c.customer_name, c.account_number, u.full_name as user_name, " +
                    "(SELECT COUNT(*) FROM bill_items bi WHERE bi.bill_id = b.bill_id) as item_count " +
                    "FROM bills b " +
                    "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
                    "LEFT JOIN users u ON b.user_id = u.user_id " +
                    "WHERE b.customer_id = ? " +
                    "ORDER BY b.bill_date DESC, b.bill_time DESC";
        
        List<Bill> bills = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Bill bill = extractBillFromResultSet(rs);
                    try {
                        bill.setItemCount(rs.getInt("item_count"));
                    } catch (SQLException e) {
                        bill.setItemCount(0);
                    }
                    bills.add(bill);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting bills by customer: " + e.getMessage(), e);
        }
        
        return bills;
    }
    
    @Override
    public List<Bill> getBillsByDateRange(Date startDate, Date endDate) throws DatabaseException {
        String sql = "SELECT b.*, c.customer_name, c.account_number, u.full_name as user_name " +
                    "FROM bills b " +
                    "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
                    "LEFT JOIN users u ON b.user_id = u.user_id " +
                    "WHERE b.bill_date BETWEEN ? AND ? " +
                    "ORDER BY b.bill_date DESC, b.bill_time DESC";
        
        List<Bill> bills = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bills.add(extractBillFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting bills by date range: " + e.getMessage(), e);
        }
        
        return bills;
    }
    
    @Override
    public List<Bill> getBillsByPaymentStatus(String paymentStatus) throws DatabaseException {
        String sql = "SELECT b.*, c.customer_name, c.account_number, u.full_name as user_name " +
                    "FROM bills b " +
                    "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
                    "LEFT JOIN users u ON b.user_id = u.user_id " +
                    "WHERE b.payment_status = ? " +
                    "ORDER BY b.bill_date DESC, b.bill_time DESC";
        
        List<Bill> bills = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, paymentStatus);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bills.add(extractBillFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting bills by payment status: " + e.getMessage(), e);
        }
        
        return bills;
    }
    
    @Override
    public List<Bill> getBillsByPaymentMethod(String paymentMethod) throws DatabaseException {
        String sql = "SELECT b.*, c.customer_name, c.account_number, u.full_name as user_name " +
                    "FROM bills b " +
                    "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
                    "LEFT JOIN users u ON b.user_id = u.user_id " +
                    "WHERE b.payment_method = ? " +
                    "ORDER BY b.bill_date DESC, b.bill_time DESC";
        
        List<Bill> bills = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, paymentMethod);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bills.add(extractBillFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting bills by payment method: " + e.getMessage(), e);
        }
        
        return bills;
    }
    
    @Override
    public List<Bill> getTodaysBills() throws DatabaseException {
        String sql = "SELECT b.*, c.customer_name, c.account_number, u.full_name as user_name, " +
                    "(SELECT COUNT(*) FROM bill_items bi WHERE bi.bill_id = b.bill_id) as item_count " +
                    "FROM bills b " +
                    "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
                    "LEFT JOIN users u ON b.user_id = u.user_id " +
                    "WHERE DATE(b.bill_date) = CURDATE() " +
                    "ORDER BY b.bill_time DESC";
        
        List<Bill> bills = new ArrayList<>();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Bill bill = extractBillFromResultSet(rs);
                try {
                    bill.setItemCount(rs.getInt("item_count"));
                } catch (SQLException e) {
                    bill.setItemCount(0);
                }
                bills.add(bill);
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting today's bills: " + e.getMessage(), e);
        }
        
        return bills;
    }
    
    @Override
    public List<Bill> getBillsByUser(int userId) throws DatabaseException {
        String sql = "SELECT b.*, c.customer_name, c.account_number, u.full_name as user_name " +
                    "FROM bills b " +
                    "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
                    "LEFT JOIN users u ON b.user_id = u.user_id " +
                    "WHERE b.user_id = ? " +
                    "ORDER BY b.bill_date DESC, b.bill_time DESC";
        
        List<Bill> bills = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bills.add(extractBillFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting bills by user: " + e.getMessage(), e);
        }
        
        return bills;
    }
    
    @Override
    public List<Bill> searchBills(String searchTerm) throws DatabaseException {
        String sql = "SELECT b.*, c.customer_name, c.account_number, u.full_name as user_name, " +
                    "(SELECT COUNT(*) FROM bill_items bi WHERE bi.bill_id = b.bill_id) as item_count " +
                    "FROM bills b " +
                    "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
                    "LEFT JOIN users u ON b.user_id = u.user_id " +
                    "WHERE b.bill_number LIKE ? OR c.customer_name LIKE ? " +
                    "OR c.account_number LIKE ? " +
                    "ORDER BY b.bill_date DESC, b.bill_time DESC";
        
        List<Bill> bills = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Bill bill = extractBillFromResultSet(rs);
                    try {
                        bill.setItemCount(rs.getInt("item_count"));
                    } catch (SQLException e) {
                        bill.setItemCount(0);
                    }
                    bills.add(bill);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error searching bills: " + e.getMessage(), e);
        }
        
        return bills;
    }
    
    @Override
    public List<Bill> getBillsWithPagination(int offset, int limit) throws DatabaseException {
        String sql = "SELECT b.*, c.customer_name, c.account_number, u.full_name as user_name " +
                    "FROM bills b " +
                    "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
                    "LEFT JOIN users u ON b.user_id = u.user_id " +
                    "ORDER BY b.bill_date DESC, b.bill_time DESC " +
                    "LIMIT ? OFFSET ?";
        
        List<Bill> bills = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bills.add(extractBillFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting bills with pagination: " + e.getMessage(), e);
        }
        
        return bills;
    }
    
    @Override
    public String generateBillNumber() throws DatabaseException {
        // Simple and works perfectly for your needs
        String prefix = "INV";
        String sql = "SELECT COUNT(*) + 1 FROM bills";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                int nextNumber = rs.getInt(1);
                return prefix + String.format("%06d", nextNumber);
            }
            return prefix + "000001";
            
        } catch (SQLException e) {
            throw new DatabaseException("Error generating bill number: " + e.getMessage(), e);
        }
    }
    
    private String generateBillNumberFallback() throws DatabaseException {
        String sql = "SELECT MAX(CAST(SUBSTRING(bill_number, 4) AS UNSIGNED)) FROM bills " +
                    "WHERE bill_number LIKE 'INV%'";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            
            // Lock the table to prevent concurrent bill number generation
            conn.createStatement().execute("LOCK TABLES bills WRITE");
            
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            int nextNumber = 1;
            if (rs.next()) {
                Integer maxNumber = rs.getObject(1, Integer.class);
                if (maxNumber != null) {
                    nextNumber = maxNumber + 1;
                }
            }
            
            String billNumber = String.format("INV%06d", nextNumber);
            
            // Verify the bill number doesn't exist (double-check)
            String checkSql = "SELECT COUNT(*) FROM bills WHERE bill_number = ?";
            try (PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {
                checkPstmt.setString(1, billNumber);
                try (ResultSet checkRs = checkPstmt.executeQuery()) {
                    if (checkRs.next() && checkRs.getInt(1) > 0) {
                        // If it exists, increment and try again
                        nextNumber++;
                        billNumber = String.format("INV%06d", nextNumber);
                    }
                }
            }
            
            return billNumber;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error generating bill number: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.createStatement().execute("UNLOCK TABLES");
                } catch (SQLException e) {
                    // Log error
                }
            }
            closeResources(conn, pstmt, rs);
        }
    }
    
    @Override
    public boolean isBillNumberExists(String billNumber) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM bills WHERE bill_number = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, billNumber);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error checking bill number existence: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public int getTotalBillCount() throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM bills";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting total bill count: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public int getBillCountByStatus(String paymentStatus) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM bills WHERE payment_status = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, paymentStatus);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting bill count by status: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public double getTodaysSalesTotal() throws DatabaseException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM bills " +
                    "WHERE DATE(bill_date) = CURDATE() AND payment_status = 'PAID'";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting today's sales total: " + e.getMessage(), e);
        }
        
        return 0.0;
    }
    
    @Override
    public double getSalesTotalByDateRange(Date startDate, Date endDate) throws DatabaseException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM bills " +
                    "WHERE bill_date BETWEEN ? AND ? AND payment_status = 'PAID'";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting sales total by date range: " + e.getMessage(), e);
        }
        
        return 0.0;
    }
    
    @Override
    public double getMonthlySalesTotal(int year, int month) throws DatabaseException {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM bills " +
                    "WHERE YEAR(bill_date) = ? AND MONTH(bill_date) = ? AND payment_status = 'PAID'";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting monthly sales total: " + e.getMessage(), e);
        }
        
        return 0.0;
    }
    
    @Override
    public List<Bill> getRecentBills(int limit) throws DatabaseException {
        String sql = "SELECT b.*, c.customer_name, c.account_number, u.full_name as user_name " +
                    "FROM bills b " +
                    "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
                    "LEFT JOIN users u ON b.user_id = u.user_id " +
                    "ORDER BY b.created_at DESC " +
                    "LIMIT ?";
        
        List<Bill> bills = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bills.add(extractBillFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting recent bills: " + e.getMessage(), e);
        }
        
        return bills;
    }
    
    @Override
    public List<Bill> getPendingBills() throws DatabaseException {
        String sql = "SELECT b.*, c.customer_name, c.account_number, u.full_name as user_name " +
                    "FROM bills b " +
                    "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
                    "LEFT JOIN users u ON b.user_id = u.user_id " +
                    "WHERE b.payment_status IN ('PENDING', 'PARTIAL') " +
                    "ORDER BY b.bill_date ASC, b.bill_time ASC";
        
        List<Bill> bills = new ArrayList<>();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                bills.add(extractBillFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting pending bills: " + e.getMessage(), e);
        }
        
        return bills;
    }
    
    @Override
    public boolean updatePaymentStatus(int billId, String paymentStatus, String notes) throws DatabaseException {
        String sql = "UPDATE bills SET payment_status = ?, " +
                    "notes = CONCAT(IFNULL(notes, ''), ' | Status updated: ', ?) " +
                    "WHERE bill_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, paymentStatus);
            pstmt.setString(2, notes != null ? notes : "");
            pstmt.setInt(3, billId);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error updating payment status: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Bill getBillWithItems(int billId) throws DatabaseException {
        Bill bill = getBillById(billId);
        if (bill != null) {
            List<BillItem> items = billItemDAO.getBillItemsByBillId(billId);
            bill.setBillItems(items);
        }
        return bill;
    }
    
    @Override
    public Map<String, Object> getSalesStatistics() throws DatabaseException {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalBills", getTotalBillCount());
        stats.put("paidBills", getBillCountByStatus("PAID"));
        stats.put("pendingBills", getBillCountByStatus("PENDING"));
        stats.put("cancelledBills", getBillCountByStatus("CANCELLED"));
        stats.put("todaysSales", getTodaysSalesTotal());
        
        // Get current month sales
        Calendar cal = Calendar.getInstance();
        stats.put("currentMonthSales", getMonthlySalesTotal(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1));
        
        return stats;
    }
    
    @Override
    public Map<String, Object> getDailySalesSummary(Date date) throws DatabaseException {
        Map<String, Object> summary = new HashMap<>();
        
        String sql = "SELECT COUNT(*) as bill_count, " +
                    "COALESCE(SUM(total_amount), 0) as total_sales, " +
                    "COALESCE(AVG(total_amount), 0) as average_bill, " +
                    "COALESCE(SUM(CASE WHEN payment_method = 'CASH' THEN total_amount ELSE 0 END), 0) as cash_sales, " +
                    "COALESCE(SUM(CASE WHEN payment_method = 'CARD' THEN total_amount ELSE 0 END), 0) as card_sales, " +
                    "COALESCE(SUM(CASE WHEN payment_method = 'CHEQUE' THEN total_amount ELSE 0 END), 0) as cheque_sales, " +
                    "COALESCE(SUM(CASE WHEN payment_method = 'BANK_TRANSFER' THEN total_amount ELSE 0 END), 0) as transfer_sales " +
                    "FROM bills WHERE DATE(bill_date) = ? AND payment_status = 'PAID'";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, new java.sql.Date(date.getTime()));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    summary.put("billCount", rs.getInt("bill_count"));
                    summary.put("totalSales", rs.getDouble("total_sales"));
                    summary.put("averageBill", rs.getDouble("average_bill"));
                    summary.put("cashSales", rs.getDouble("cash_sales"));
                    summary.put("cardSales", rs.getDouble("card_sales"));
                    summary.put("chequeSales", rs.getDouble("cheque_sales"));
                    summary.put("transferSales", rs.getDouble("transfer_sales"));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting daily sales summary: " + e.getMessage(), e);
        }
        
        return summary;
    }
    
    @Override
    public List<Map<String, Object>> getTopSellingItems(Date startDate, Date endDate, int limit) throws DatabaseException {
        String sql = "SELECT i.item_code, i.item_name, i.selling_price, " +
                    "SUM(bi.quantity) as total_quantity, " +
                    "SUM(bi.total_price) as total_revenue, " +
                    "COUNT(DISTINCT b.bill_id) as bill_count " +
                    "FROM bill_items bi " +
                    "JOIN bills b ON bi.bill_id = b.bill_id " +
                    "JOIN items i ON bi.item_id = i.item_id " +
                    "WHERE b.bill_date BETWEEN ? AND ? AND b.payment_status = 'PAID' " +
                    "GROUP BY i.item_id, i.item_code, i.item_name, i.selling_price " +
                    "ORDER BY total_quantity DESC " +
                    "LIMIT ?";
        
        List<Map<String, Object>> topItems = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
            pstmt.setInt(3, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("itemCode", rs.getString("item_code"));
                    item.put("itemName", rs.getString("item_name"));
                    item.put("sellingPrice", rs.getDouble("selling_price"));
                    item.put("totalQuantity", rs.getInt("total_quantity"));
                    item.put("totalRevenue", rs.getDouble("total_revenue"));
                    item.put("billCount", rs.getInt("bill_count"));
                    topItems.add(item);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting top selling items: " + e.getMessage(), e);
        }
        
        return topItems;
    }
    
    @Override
    public Map<String, Object> getCustomerPurchaseSummary(int customerId) throws DatabaseException {
        Map<String, Object> summary = new HashMap<>();
        
        String sql = "SELECT COUNT(*) as total_bills, " +
                    "COALESCE(SUM(total_amount), 0) as total_spent, " +
                    "COALESCE(AVG(total_amount), 0) as average_bill, " +
                    "MAX(bill_date) as last_purchase_date, " +
                    "MIN(bill_date) as first_purchase_date " +
                    "FROM bills WHERE customer_id = ? AND payment_status = 'PAID'";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    summary.put("totalBills", rs.getInt("total_bills"));
                    summary.put("totalSpent", rs.getDouble("total_spent"));
                    summary.put("averageBill", rs.getDouble("average_bill"));
                    summary.put("lastPurchaseDate", rs.getDate("last_purchase_date"));
                    summary.put("firstPurchaseDate", rs.getDate("first_purchase_date"));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting customer purchase summary: " + e.getMessage(), e);
        }
        
        return summary;
    }
    
    @Override
    public boolean hasCustomerPendingBills(int customerId) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM bills WHERE customer_id = ? " +
                    "AND payment_status IN ('PENDING', 'PARTIAL')";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error checking customer pending bills: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public List<Bill> getOverdueBills(int daysOverdue) throws DatabaseException {
        String sql = "SELECT b.*, c.customer_name, c.account_number, u.full_name as user_name " +
                    "FROM bills b " +
                    "LEFT JOIN customers c ON b.customer_id = c.customer_id " +
                    "LEFT JOIN users u ON b.user_id = u.user_id " +
                    "WHERE b.payment_status IN ('PENDING', 'PARTIAL') " +
                    "AND DATEDIFF(CURDATE(), b.bill_date) > ? " +
                    "ORDER BY b.bill_date ASC";
        
        List<Bill> bills = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, daysOverdue);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bills.add(extractBillFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting overdue bills: " + e.getMessage(), e);
        }
        
        return bills;
    }
    
    /**
     * Extract Bill object from ResultSet
     */
    private Bill extractBillFromResultSet(ResultSet rs) throws SQLException {
        Bill bill = new Bill();
        bill.setBillId(rs.getInt("bill_id"));
        bill.setBillNumber(rs.getString("bill_number"));
        bill.setCustomerId(rs.getInt("customer_id"));
        bill.setUserId(rs.getInt("user_id"));
        bill.setBillDate(rs.getDate("bill_date"));
        bill.setBillTime(rs.getTime("bill_time"));
        bill.setSubtotal(rs.getDouble("subtotal"));
        bill.setDiscountPercentage(rs.getDouble("discount_percentage"));
        bill.setDiscountAmount(rs.getDouble("discount_amount"));
        bill.setTaxPercentage(rs.getDouble("tax_percentage"));
        bill.setTaxAmount(rs.getDouble("tax_amount"));
        bill.setTotalAmount(rs.getDouble("total_amount"));
        bill.setPaymentMethod(rs.getString("payment_method"));
        bill.setPaymentStatus(rs.getString("payment_status"));
        bill.setNotes(rs.getString("notes"));
        bill.setCreatedAt(rs.getTimestamp("created_at"));
        
        // Set customer and user names if available
        try {
            bill.setCustomerName(rs.getString("customer_name"));
            bill.setCustomerAccountNumber(rs.getString("account_number"));
            bill.setUserName(rs.getString("user_name"));
        } catch (SQLException e) {
            // Columns might not be present in all queries
        }
        
        return bill;
    }
    
    /**
     * Close database resources
     */
    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // Log error
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                // Log error
            }
        }
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                // Log error
            }
        }
    }
}