package com.pahanaedu.dao.impl;

import com.pahanaedu.dao.interfaces.BillItemDAO;
import com.pahanaedu.model.BillItem;
import com.pahanaedu.exception.DatabaseException;
import com.pahanaedu.util.DBConnection;
import com.pahanaedu.dao.interfaces.ItemDAO;
import com.pahanaedu.model.Item;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Implementation of BillItemDAO interface
 * Handles all database operations for BillItem entity
 */
public class BillItemDAOImpl implements BillItemDAO {
    
    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }
    
    @Override
    public int addBillItem(BillItem billItem) throws DatabaseException {
        String sql = "INSERT INTO bill_items (bill_id, item_id, quantity, unit_price, " +
                    "discount_percentage, discount_amount, total_price) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Calculate total price if not set
            if (billItem.getTotalPrice() == 0) {
                billItem.calculateTotalPrice();
            }
            
            pstmt.setInt(1, billItem.getBillId());
            pstmt.setInt(2, billItem.getItemId());
            pstmt.setInt(3, billItem.getQuantity());
            pstmt.setDouble(4, billItem.getUnitPrice());
            pstmt.setDouble(5, billItem.getDiscountPercentage());
            pstmt.setDouble(6, billItem.getDiscountAmount());
            pstmt.setDouble(7, billItem.getTotalPrice());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Creating bill item failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new DatabaseException("Creating bill item failed, no ID obtained.");
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error adding bill item: " + e.getMessage(), e);
        }
    }
    
    @Override
    public int addBillItemsBatch(List<BillItem> billItems) throws DatabaseException {
        String sql = "INSERT INTO bill_items (bill_id, item_id, quantity, unit_price, " +
                    "discount_percentage, discount_amount, total_price) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            
            int count = 0;
            for (BillItem item : billItems) {
                // Calculate total price if not set
                if (item.getTotalPrice() == 0) {
                    item.calculateTotalPrice();
                }
                
                pstmt.setInt(1, item.getBillId());
                pstmt.setInt(2, item.getItemId());
                pstmt.setInt(3, item.getQuantity());
                pstmt.setDouble(4, item.getUnitPrice());
                pstmt.setDouble(5, item.getDiscountPercentage());
                pstmt.setDouble(6, item.getDiscountAmount());
                pstmt.setDouble(7, item.getTotalPrice());
                
                pstmt.addBatch();
                count++;
                
                // Execute batch every 100 items
                if (count % 100 == 0) {
                    pstmt.executeBatch();
                }
            }
            
            // Execute remaining items
            pstmt.executeBatch();
            conn.commit();
            
            return billItems.size();
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DatabaseException("Error rolling back transaction: " + ex.getMessage(), ex);
                }
            }
            throw new DatabaseException("Error adding bill items in batch: " + e.getMessage(), e);
        } finally {
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
    
    @Override
    public boolean updateBillItem(BillItem billItem) throws DatabaseException {
        String sql = "UPDATE bill_items SET quantity = ?, unit_price = ?, " +
                    "discount_percentage = ?, discount_amount = ?, total_price = ? " +
                    "WHERE bill_item_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Calculate total price if not set
            if (billItem.getTotalPrice() == 0) {
                billItem.calculateTotalPrice();
            }
            
            pstmt.setInt(1, billItem.getQuantity());
            pstmt.setDouble(2, billItem.getUnitPrice());
            pstmt.setDouble(3, billItem.getDiscountPercentage());
            pstmt.setDouble(4, billItem.getDiscountAmount());
            pstmt.setDouble(5, billItem.getTotalPrice());
            pstmt.setInt(6, billItem.getBillItemId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error updating bill item: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteBillItem(int billItemId) throws DatabaseException {
        String sql = "DELETE FROM bill_items WHERE bill_item_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, billItemId);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting bill item: " + e.getMessage(), e);
        }
    }
    
    @Override
    public int deleteBillItemsByBillId(int billId) throws DatabaseException {
        String sql = "DELETE FROM bill_items WHERE bill_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, billId);
            
            return pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting bill items by bill ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public BillItem getBillItemById(int billItemId) throws DatabaseException {
        String sql = "SELECT bi.*, i.item_code, i.item_name, c.category_name " +
                    "FROM bill_items bi " +
                    "JOIN items i ON bi.item_id = i.item_id " +
                    "LEFT JOIN categories c ON i.category_id = c.category_id " +
                    "WHERE bi.bill_item_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, billItemId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractBillItemFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting bill item by ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public List<BillItem> getBillItemsByBillId(int billId) throws DatabaseException {
        String sql = "SELECT bi.*, i.item_name, i.item_code, i.selling_price " +
                    "FROM bill_items bi " +
                    "JOIN items i ON bi.item_id = i.item_id " +
                    "WHERE bi.bill_id = ?";
        
        List<BillItem> items = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, billId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BillItem item = new BillItem();
                    item.setBillItemId(rs.getInt("bill_item_id"));
                    item.setBillId(rs.getInt("bill_id"));
                    item.setItemId(rs.getInt("item_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getDouble("unit_price"));
                    item.setDiscountPercentage(rs.getDouble("discount_percentage"));
                    item.setDiscountAmount(rs.getDouble("discount_amount"));
                    item.setTotalPrice(rs.getDouble("total_price"));
                    
                    // Set item details
                    Item itemDetails = new Item();
                    itemDetails.setItemId(rs.getInt("item_id"));
                    itemDetails.setItemName(rs.getString("item_name"));
                    itemDetails.setItemCode(rs.getString("item_code"));
                    item.setItem(itemDetails);
                    
                    items.add(item);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting bill items: " + e.getMessage(), e);
        }
        
        return items;
    }
    
    @Override
    public List<BillItem> getBillItemsByItemId(int itemId) throws DatabaseException {
        String sql = "SELECT bi.*, i.item_code, i.item_name, c.category_name " +
                    "FROM bill_items bi " +
                    "JOIN items i ON bi.item_id = i.item_id " +
                    "LEFT JOIN categories c ON i.category_id = c.category_id " +
                    "WHERE bi.item_id = ? " +
                    "ORDER BY bi.bill_id, bi.bill_item_id";
        
        List<BillItem> items = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, itemId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(extractBillItemFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting bill items by item ID: " + e.getMessage(), e);
        }
        
        return items;
    }
    
    @Override
    public int getTotalQuantitySold(int itemId) throws DatabaseException {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM bill_items " +
                    "WHERE item_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, itemId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting total quantity sold: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public double getTotalRevenue(int itemId) throws DatabaseException {
        String sql = "SELECT COALESCE(SUM(total_price), 0) FROM bill_items " +
                    "WHERE item_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, itemId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting total revenue: " + e.getMessage(), e);
        }
        
        return 0.0;
    }
    
    @Override
    public boolean isItemInBill(int billId, int itemId) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM bill_items " +
                    "WHERE bill_id = ? AND item_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, billId);
            pstmt.setInt(2, itemId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error checking if item is in bill: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public int getItemCountForBill(int billId) throws DatabaseException {
        String sql = "SELECT COUNT(DISTINCT item_id) FROM bill_items " +
                    "WHERE bill_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, billId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting item count for bill: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public int getTotalQuantityForBill(int billId) throws DatabaseException {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM bill_items " +
                    "WHERE bill_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, billId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting total quantity for bill: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public boolean updateBillItemQuantity(int billItemId, int newQuantity) throws DatabaseException {
        String sql = "UPDATE bill_items SET quantity = ?, total_price = (unit_price * ?) - discount_amount " +
                    "WHERE bill_item_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, newQuantity);
            pstmt.setInt(3, billItemId);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error updating bill item quantity: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean updateBillItemDiscount(int billItemId, double discountPercentage) throws DatabaseException {
        String sql = "UPDATE bill_items SET discount_percentage = ?, " +
                    "discount_amount = (unit_price * quantity * ? / 100), " +
                    "total_price = (unit_price * quantity) - (unit_price * quantity * ? / 100) " +
                    "WHERE bill_item_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, discountPercentage);
            pstmt.setDouble(2, discountPercentage);
            pstmt.setDouble(3, discountPercentage);
            pstmt.setInt(4, billItemId);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error updating bill item discount: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Map<String, Object>> getItemSalesSummary(String startDate, String endDate) throws DatabaseException {
        String sql = "SELECT i.item_id, i.item_code, i.item_name, " +
                    "SUM(bi.quantity) as total_quantity, " +
                    "SUM(bi.total_price) as total_revenue, " +
                    "COUNT(DISTINCT bi.bill_id) as bill_count " +
                    "FROM bill_items bi " +
                    "JOIN items i ON bi.item_id = i.item_id " +
                    "JOIN bills b ON bi.bill_id = b.bill_id " +
                    "WHERE b.bill_date BETWEEN ? AND ? " +
                    "GROUP BY i.item_id, i.item_code, i.item_name " +
                    "ORDER BY total_revenue DESC";
        
        List<Map<String, Object>> summary = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("itemId", rs.getInt("item_id"));
                    item.put("itemCode", rs.getString("item_code"));
                    item.put("itemName", rs.getString("item_name"));
                    item.put("totalQuantity", rs.getInt("total_quantity"));
                    item.put("totalRevenue", rs.getDouble("total_revenue"));
                    item.put("billCount", rs.getInt("bill_count"));
                    summary.add(item);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting item sales summary: " + e.getMessage(), e);
        }
        
        return summary;
    }
    
    @Override
    public List<Map<String, Object>> getMostSoldItems(int limit) throws DatabaseException {
        String sql = "SELECT i.item_id, i.item_code, i.item_name, " +
                    "SUM(bi.quantity) as total_quantity " +
                    "FROM bill_items bi " +
                    "JOIN items i ON bi.item_id = i.item_id " +
                    "GROUP BY i.item_id, i.item_code, i.item_name " +
                    "ORDER BY total_quantity DESC " +
                    "LIMIT ?";
        
        List<Map<String, Object>> items = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("itemId", rs.getInt("item_id"));
                    item.put("itemCode", rs.getString("item_code"));
                    item.put("itemName", rs.getString("item_name"));
                    item.put("totalQuantity", rs.getInt("total_quantity"));
                    items.add(item);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting most sold items: " + e.getMessage(), e);
        }
        
        return items;
    }
    
    @Override
    public List<Map<String, Object>> getLeastSoldItems(int limit) throws DatabaseException {
        String sql = "SELECT i.item_id, i.item_code, i.item_name, " +
                    "COALESCE(SUM(bi.quantity), 0) as total_quantity " +
                    "FROM items i " +
                    "LEFT JOIN bill_items bi ON i.item_id = bi.item_id " +
                    "GROUP BY i.item_id, i.item_code, i.item_name " +
                    "ORDER BY total_quantity ASC " +
                    "LIMIT ?";
        
        List<Map<String, Object>> items = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("itemId", rs.getInt("item_id"));
                    item.put("itemCode", rs.getString("item_code"));
                    item.put("itemName", rs.getString("item_name"));
                    item.put("totalQuantity", rs.getInt("total_quantity"));
                    items.add(item);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting least sold items: " + e.getMessage(), e);
        }
        
        return items;
    }
    
    @Override
    public List<Map<String, Object>> getItemsNeverSold() throws DatabaseException {
        String sql = "SELECT i.item_id, i.item_code, i.item_name " +
                    "FROM items i " +
                    "LEFT JOIN bill_items bi ON i.item_id = bi.item_id " +
                    "WHERE bi.item_id IS NULL";
        
        List<Map<String, Object>> items = new ArrayList<>();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("itemId", rs.getInt("item_id"));
                item.put("itemCode", rs.getString("item_code"));
                item.put("itemName", rs.getString("item_name"));
                items.add(item);
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting items never sold: " + e.getMessage(), e);
        }
        
        return items;
    }
    
    @Override
    public List<Map<String, Object>> getRevenueByCategory(String startDate, String endDate) throws DatabaseException {
        String sql = "SELECT c.category_id, c.category_name, " +
                    "SUM(bi.total_price) as total_revenue, " +
                    "SUM(bi.quantity) as total_quantity " +
                    "FROM bill_items bi " +
                    "JOIN items i ON bi.item_id = i.item_id " +
                    "JOIN categories c ON i.category_id = c.category_id " +
                    "JOIN bills b ON bi.bill_id = b.bill_id " +
                    "WHERE b.bill_date BETWEEN ? AND ? " +
                    "GROUP BY c.category_id, c.category_name " +
                    "ORDER BY total_revenue DESC";
        
        List<Map<String, Object>> revenue = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> category = new HashMap<>();
                    category.put("categoryId", rs.getInt("category_id"));
                    category.put("categoryName", rs.getString("category_name"));
                    category.put("totalRevenue", rs.getDouble("total_revenue"));
                    category.put("totalQuantity", rs.getInt("total_quantity"));
                    revenue.add(category);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting revenue by category: " + e.getMessage(), e);
        }
        
        return revenue;
    }
    
    @Override
    public boolean replaceBillItems(int billId, List<BillItem> newItems) throws DatabaseException {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            // First delete existing items
            deleteBillItemsByBillId(billId);
            
            // Then add new items
            for (BillItem item : newItems) {
                item.setBillId(billId);
                addBillItem(item);
            }
            
            conn.commit();
            return true;
            
        } catch (DatabaseException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DatabaseException("Error rolling back transaction: " + ex.getMessage(), ex);
                }
            }
            throw e;
        } catch (SQLException e) {
            throw new DatabaseException("Error replacing bill items: " + e.getMessage(), e);
        } finally {
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
    
    @Override
    public List<BillItem> getBillItemsWithDetails(int billId) throws DatabaseException {
        String sql = "SELECT bi.*, i.item_code, i.item_name, i.selling_price, " +
                    "c.category_name, c.category_id " +
                    "FROM bill_items bi " +
                    "JOIN items i ON bi.item_id = i.item_id " +
                    "LEFT JOIN categories c ON i.category_id = c.category_id " +
                    "WHERE bi.bill_id = ? " +
                    "ORDER BY bi.bill_item_id";
        
        List<BillItem> items = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, billId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BillItem item = extractBillItemFromResultSet(rs);
                    items.add(item);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting bill items with details: " + e.getMessage(), e);
        }
        
        return items;
    }
    
    @Override
    public double recalculateBillItemTotal(int billItemId) throws DatabaseException {
        String sql = "UPDATE bill_items SET total_price = (unit_price * quantity) - discount_amount " +
                    "WHERE bill_item_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, billItemId);
            pstmt.executeUpdate();
            
            // Return the new total price
            sql = "SELECT total_price FROM bill_items WHERE bill_item_id = ?";
            try (PreparedStatement pstmt2 = conn.prepareStatement(sql)) {
                pstmt2.setInt(1, billItemId);
                try (ResultSet rs = pstmt2.executeQuery()) {
                    if (rs.next()) {
                        return rs.getDouble(1);
                    }
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error recalculating bill item total: " + e.getMessage(), e);
        }
        
        return 0.0;
    }
    
    @Override
    public List<BillItem> getDiscountedItems(int billId) throws DatabaseException {
        String sql = "SELECT bi.*, i.item_code, i.item_name, c.category_name " +
                    "FROM bill_items bi " +
                    "JOIN items i ON bi.item_id = i.item_id " +
                    "LEFT JOIN categories c ON i.category_id = c.category_id " +
                    "WHERE bi.bill_id = ? AND bi.discount_amount > 0 " +
                    "ORDER BY bi.bill_item_id";
        
        List<BillItem> items = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, billId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(extractBillItemFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting discounted items: " + e.getMessage(), e);
        }
        
        return items;
    }
    
    @Override
    public double getTotalDiscountForBill(int billId) throws DatabaseException {
        String sql = "SELECT COALESCE(SUM(discount_amount), 0) FROM bill_items " +
                    "WHERE bill_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, billId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting total discount for bill: " + e.getMessage(), e);
        }
        
        return 0.0;
    }
    
    /**
     * Extract BillItem object from ResultSet
     */
    private BillItem extractBillItemFromResultSet(ResultSet rs) throws SQLException {
        BillItem item = new BillItem();
        item.setBillItemId(rs.getInt("bill_item_id"));
        item.setBillId(rs.getInt("bill_id"));
        item.setItemId(rs.getInt("item_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getDouble("unit_price"));
        item.setDiscountPercentage(rs.getDouble("discount_percentage"));
        item.setDiscountAmount(rs.getDouble("discount_amount"));
        item.setTotalPrice(rs.getDouble("total_price"));
        
        // Create and set the Item object if item details are available
        try {
            String itemCode = rs.getString("item_code");
            String itemName = rs.getString("item_name");
            
            if (itemCode != null && itemName != null) {
                // Create a complete Item object
                Item itemObj = new Item();
                itemObj.setItemId(rs.getInt("item_id"));
                itemObj.setItemCode(itemCode);
                itemObj.setItemName(itemName);
                
                // Set additional fields if available
                try {
                    itemObj.setSellingPrice(rs.getDouble("selling_price"));
                } catch (SQLException e) {
                    // selling_price column might not be present
                    itemObj.setSellingPrice(item.getUnitPrice());
                }
                
                try {
                    itemObj.setCategoryId(rs.getInt("category_id"));
                } catch (SQLException e) {
                    // category_id might not be present
                }
                
                // Set the item object in BillItem
                item.setItem(itemObj);
            }
            
            // Also set transient fields for backward compatibility
            item.setItemCode(itemCode);
            item.setItemName(itemName);
            
            try {
                item.setCategoryName(rs.getString("category_name"));
            } catch (SQLException e) {
                // category_name might not be present
            }
            
        } catch (SQLException e) {
            // If item details are not available, try to load item separately
            // This ensures we always have an Item object
            try {
                ItemDAO itemDAO = new ItemDAOImpl();
                Item itemObj = itemDAO.getItemById(item.getItemId());
                if (itemObj != null) {
                    item.setItem(itemObj);
                }
            } catch (Exception ex) {
                // Log the error but don't fail the entire operation
                System.err.println("Could not load item details for item ID: " + item.getItemId());
            }
        }
        
        return item;
    }
}