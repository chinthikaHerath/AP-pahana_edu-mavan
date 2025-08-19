package com.pahanaedu.dao.impl;

import com.pahanaedu.dao.interfaces.ItemDAO;
import com.pahanaedu.exception.DatabaseException;
import com.pahanaedu.model.Item;
import com.pahanaedu.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {
    
    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }
    
    @Override
    public int addItem(Item item) throws DatabaseException {
        String sql = "INSERT INTO items (item_code, item_name, description, category_id, " +
                     "author, publisher, isbn, unit_price, selling_price, quantity_in_stock, " +
                     "reorder_level, is_active, created_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, item.getItemCode());
            stmt.setString(2, item.getItemName());
            stmt.setString(3, item.getDescription());
            stmt.setInt(4, item.getCategoryId());
            stmt.setString(5, item.getAuthor());
            stmt.setString(6, item.getPublisher());
            stmt.setString(7, item.getIsbn());
            stmt.setDouble(8, item.getUnitPrice());
            stmt.setDouble(9, item.getSellingPrice());
            stmt.setInt(10, item.getQuantityInStock());
            stmt.setInt(11, item.getReorderLevel());
            stmt.setBoolean(12, item.isActive());
            stmt.setInt(13, item.getCreatedBy());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Creating item failed, no rows affected.");
            }
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new DatabaseException("Creating item failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error adding item: " + e.getMessage(), e, "ITEM_ADD_ERROR", e.getSQLState());
        }
    }

    @Override
    public boolean updateItem(Item item) throws DatabaseException {
        String sql = "UPDATE items SET item_name=?, description=?, category_id=?, " +
                     "author=?, publisher=?, isbn=?, unit_price=?, selling_price=?, " +
                     "quantity_in_stock=?, reorder_level=?, updated_at=CURRENT_TIMESTAMP " +
                     "WHERE item_id=?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, item.getItemName());
            stmt.setString(2, item.getDescription());
            stmt.setInt(3, item.getCategoryId());
            stmt.setString(4, item.getAuthor());
            stmt.setString(5, item.getPublisher());
            stmt.setString(6, item.getIsbn());
            stmt.setDouble(7, item.getUnitPrice());
            stmt.setDouble(8, item.getSellingPrice());
            stmt.setInt(9, item.getQuantityInStock());
            stmt.setInt(10, item.getReorderLevel());
            stmt.setInt(11, item.getItemId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating item: " + e.getMessage(), e, "ITEM_UPDATE_ERROR", e.getSQLState());
        }
    }

    @Override
    public boolean deactivateItem(int itemId) throws DatabaseException {
        String sql = "UPDATE items SET is_active=false WHERE item_id=?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, itemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deactivating item: " + e.getMessage(), e, "ITEM_DEACTIVATE_ERROR", e.getSQLState());
        }
    }

    @Override
    public Item getItemById(int itemId) throws DatabaseException {
        String sql = "SELECT i.*, c.category_name, u.full_name as created_by_username " +
                     "FROM items i " +
                     "LEFT JOIN categories c ON i.category_id = c.category_id " +
                     "LEFT JOIN users u ON i.created_by = u.user_id " +
                     "WHERE i.item_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, itemId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToItem(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error getting item by ID: " + e.getMessage(), e, "ITEM_GET_ERROR", e.getSQLState());
        }
    }

    @Override
    public List<Item> getAllActiveItems() throws DatabaseException {
        String sql = "SELECT i.*, c.category_name FROM items i " +
                     "LEFT JOIN categories c ON i.category_id = c.category_id " +
                     "WHERE i.is_active = true ORDER BY i.item_name";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            List<Item> items = new ArrayList<>();
            while (rs.next()) {
                items.add(mapRowToItem(rs));
            }
            return items;
        } catch (SQLException e) {
            throw new DatabaseException("Error getting all active items: " + e.getMessage(), e, "ITEM_LIST_ERROR", e.getSQLState());
        }
    }

    @Override
    public List<Item> getActiveItems() throws DatabaseException {
        // This is just an alias for getAllActiveItems()
        return getAllActiveItems();
    }

    @Override
    public List<Item> searchItems(String searchTerm) throws DatabaseException {
        String sql = "SELECT i.*, c.category_name FROM items i " +
                     "LEFT JOIN categories c ON i.category_id = c.category_id " +
                     "WHERE i.is_active = true AND " +
                     "(i.item_code LIKE ? OR i.item_name LIKE ? OR i.isbn LIKE ?) " +
                     "ORDER BY i.item_name";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String likeTerm = "%" + searchTerm + "%";
            stmt.setString(1, likeTerm);
            stmt.setString(2, likeTerm);
            stmt.setString(3, likeTerm);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Item> items = new ArrayList<>();
                while (rs.next()) {
                    items.add(mapRowToItem(rs));
                }
                return items;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error searching items: " + e.getMessage(), e, "ITEM_SEARCH_ERROR", e.getSQLState());
        }
    }

    @Override
    public List<Item> getItemsByCategory(int categoryId) throws DatabaseException {
        String sql = "SELECT i.*, c.category_name FROM items i " +
                     "LEFT JOIN categories c ON i.category_id = c.category_id " +
                     "WHERE i.is_active = true AND i.category_id = ? " +
                     "ORDER BY i.item_name";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoryId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Item> items = new ArrayList<>();
                while (rs.next()) {
                    items.add(mapRowToItem(rs));
                }
                return items;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error getting items by category: " + e.getMessage(), e, "ITEM_CATEGORY_ERROR", e.getSQLState());
        }
    }

    @Override
    public List<Item> getLowStockItems() throws DatabaseException {
        String sql = "SELECT i.*, c.category_name FROM items i " +
                     "LEFT JOIN categories c ON i.category_id = c.category_id " +
                     "WHERE i.is_active = true AND i.quantity_in_stock <= i.reorder_level " +
                     "ORDER BY i.quantity_in_stock ASC";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            List<Item> items = new ArrayList<>();
            while (rs.next()) {
                items.add(mapRowToItem(rs));
            }
            return items;
        } catch (SQLException e) {
            throw new DatabaseException("Error getting low stock items: " + e.getMessage(), e, "ITEM_LOWSTOCK_ERROR", e.getSQLState());
        }
    }
    
    @Override
    public List<Item> getOutOfStockItems() throws DatabaseException {
        String sql = "SELECT i.*, c.category_name FROM items i " +
                     "LEFT JOIN categories c ON i.category_id = c.category_id " +
                     "WHERE i.is_active = true AND i.quantity_in_stock = 0 " +
                     "ORDER BY i.item_name";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            List<Item> items = new ArrayList<>();
            while (rs.next()) {
                items.add(mapRowToItem(rs));
            }
            return items;
        } catch (SQLException e) {
            throw new DatabaseException("Error getting out of stock items: " + e.getMessage(), e, "ITEM_OUTSTOCK_ERROR", e.getSQLState());
        }
    }

    @Override
    public int getTotalItemCount() throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM items";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error getting total item count: " + e.getMessage(), e, "ITEM_COUNT_ERROR", e.getSQLState());
        }
    }
    
    @Override
    public int getActiveItemCount() throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM items WHERE is_active = true";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error getting active item count: " + e.getMessage(), e, "ITEM_ACTIVE_COUNT_ERROR", e.getSQLState());
        }
    }
    
    @Override
    public boolean updateItemStock(int itemId, int newQuantity) throws DatabaseException {
        String sql = "UPDATE items SET quantity_in_stock = ?, updated_at = CURRENT_TIMESTAMP " +
                     "WHERE item_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, itemId);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error updating item stock: " + e.getMessage(), e, "ITEM_STOCK_UPDATE_ERROR", e.getSQLState());
        }
    }
    
    @Override
    public boolean isItemCodeExists(String itemCode) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM items WHERE item_code = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, itemCode);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error checking item code existence: " + e.getMessage(), e, "ITEM_CODE_CHECK_ERROR", e.getSQLState());
        }
        
        return false;
    }
    
    @Override
    public boolean isISBNExists(String isbn) throws DatabaseException {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        
        String sql = "SELECT COUNT(*) FROM items WHERE isbn = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, isbn);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error checking ISBN existence: " + e.getMessage(), e, "ISBN_CHECK_ERROR", e.getSQLState());
        }
        
        return false;
    }
    
    
    @Override
    public boolean activateItem(int itemId) throws DatabaseException {
        String sql = "UPDATE items SET is_active = true, updated_at = CURRENT_TIMESTAMP WHERE item_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, itemId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error activating item: " + e.getMessage(), e, 
                                      "ITEM_ACTIVATE_ERROR", e.getSQLState());
        }
    }
    
    @Override
    public List<Item> getAllItems() throws DatabaseException {
        String sql = "SELECT i.*, c.category_name FROM items i " +
                     "LEFT JOIN categories c ON i.category_id = c.category_id " +
                     "ORDER BY i.is_active DESC, i.item_name"; // Active items first
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            List<Item> items = new ArrayList<>();
            while (rs.next()) {
                items.add(mapRowToItem(rs));
            }
            return items;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting all items: " + e.getMessage(), e, 
                                      "ITEM_LIST_ALL_ERROR", e.getSQLState());
        }
    }
    
    @Override
    public List<Item> getInactiveItems() throws DatabaseException {
        String sql = "SELECT i.*, c.category_name FROM items i " +
                     "LEFT JOIN categories c ON i.category_id = c.category_id " +
                     "WHERE i.is_active = false " +
                     "ORDER BY i.item_name";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            List<Item> items = new ArrayList<>();
            while (rs.next()) {
                items.add(mapRowToItem(rs));
            }
            return items;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting inactive items: " + e.getMessage(), e, 
                                      "ITEM_INACTIVE_ERROR", e.getSQLState());
        }
    }
    
    @Override
    public String generateItemCode() throws DatabaseException {
        String sql = "SELECT MAX(CAST(SUBSTRING(item_code, 3) AS UNSIGNED)) FROM items " +
                     "WHERE item_code LIKE 'IT%'";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            int nextNumber = 1;
            if (rs.next() && rs.getInt(1) > 0) {
                nextNumber = rs.getInt(1) + 1;
            }
            
            return String.format("IT%06d", nextNumber);
            
        } catch (SQLException e) {
            throw new DatabaseException("Error generating item code: " + e.getMessage(), e, "ITEM_CODE_GEN_ERROR", e.getSQLState());
        }
    }

    private Item mapRowToItem(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setItemId(rs.getInt("item_id"));
        item.setItemCode(rs.getString("item_code"));
        item.setItemName(rs.getString("item_name"));
        item.setDescription(rs.getString("description"));
        item.setCategoryId(rs.getInt("category_id"));
        item.setAuthor(rs.getString("author"));
        item.setPublisher(rs.getString("publisher"));
        item.setIsbn(rs.getString("isbn"));
        item.setUnitPrice(rs.getDouble("unit_price"));
        item.setSellingPrice(rs.getDouble("selling_price"));
        item.setQuantityInStock(rs.getInt("quantity_in_stock"));
        item.setReorderLevel(rs.getInt("reorder_level"));
        item.setActive(rs.getBoolean("is_active"));
        
        // Set created_by if available
        try {
            item.setCreatedBy(rs.getInt("created_by"));
        } catch (SQLException e) {
            // Column might not be present in all queries
        }
        
        // Set created_at if available
        try {
            item.setCreatedAt(rs.getTimestamp("created_at"));
        } catch (SQLException e) {
            // Column might not be present in all queries
        }
        
        // Set updated_at if available
        try {
            item.setUpdatedAt(rs.getTimestamp("updated_at"));
        } catch (SQLException e) {
            // Column might not be present in all queries
        }
        
        // Transient fields
        if (hasColumn(rs, "category_name")) {
            item.setCategoryName(rs.getString("category_name"));
        }
        if (hasColumn(rs, "created_by_username")) {
            item.setCreatedByUsername(rs.getString("created_by_username"));
        }
        
        return item;
    }

    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columns = metaData.getColumnCount();
        for (int i = 1; i <= columns; i++) {
            if (columnName.equalsIgnoreCase(metaData.getColumnName(i))) {
                return true;
            }
        }
        return false;
    }
}