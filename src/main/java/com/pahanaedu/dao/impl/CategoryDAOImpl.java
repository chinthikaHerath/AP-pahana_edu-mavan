package com.pahanaedu.dao.impl;

import com.pahanaedu.dao.interfaces.CategoryDAO;
import com.pahanaedu.exception.DatabaseException;
import com.pahanaedu.model.Category;
import com.pahanaedu.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of CategoryDAO interface
 * Handles all database operations for Category entity
 */
public class CategoryDAOImpl implements CategoryDAO {
    
    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }
    
    @Override
    public List<Category> getAllCategories() throws DatabaseException {
        String sql = "SELECT * FROM categories ORDER BY category_name";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            List<Category> categories = new ArrayList<>();
            while (rs.next()) {
                categories.add(mapRowToCategory(rs));
            }
            return categories;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving all categories: " + e.getMessage(), e, 
                                      "CATEGORY_LIST_ERROR", e.getSQLState());
        }
    }
    
    @Override
    public List<Category> getActiveCategories() throws DatabaseException {
        String sql = "SELECT * FROM categories WHERE is_active = true ORDER BY category_name";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            List<Category> categories = new ArrayList<>();
            while (rs.next()) {
                categories.add(mapRowToCategory(rs));
            }
            return categories;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving active categories: " + e.getMessage(), e, 
                                      "CATEGORY_ACTIVE_LIST_ERROR", e.getSQLState());
        }
    }
    
    @Override
    public Category getCategoryById(int categoryId) throws DatabaseException {
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoryId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToCategory(rs);
                }
                return null;
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving category by ID: " + e.getMessage(), e, 
                                      "CATEGORY_GET_ERROR", e.getSQLState());
        }
    }
    
    @Override
    public int addCategory(Category category) throws DatabaseException {
        String sql = "INSERT INTO categories (category_name, description, is_active) VALUES (?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getDescription());
            stmt.setBoolean(3, category.isActive());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Creating category failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new DatabaseException("Creating category failed, no ID obtained.");
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error adding category: " + e.getMessage(), e, 
                                      "CATEGORY_ADD_ERROR", e.getSQLState());
        }
    }
    
    @Override
    public boolean updateCategory(Category category) throws DatabaseException {
        String sql = "UPDATE categories SET category_name = ?, description = ?, is_active = ? " +
                     "WHERE category_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, category.getCategoryName());
            stmt.setString(2, category.getDescription());
            stmt.setBoolean(3, category.isActive());
            stmt.setInt(4, category.getCategoryId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error updating category: " + e.getMessage(), e, 
                                      "CATEGORY_UPDATE_ERROR", e.getSQLState());
        }
    }
    
    @Override
    public boolean deleteCategory(int categoryId) throws DatabaseException {
        // First check if category has items
        if (hasItems(categoryId)) {
            throw new DatabaseException("Cannot delete category with existing items", null, 
                                      "CATEGORY_HAS_ITEMS", null);
        }
        
        String sql = "DELETE FROM categories WHERE category_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoryId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting category: " + e.getMessage(), e, 
                                      "CATEGORY_DELETE_ERROR", e.getSQLState());
        }
    }
    
    @Override
    public boolean isCategoryNameExists(String categoryName) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM categories WHERE category_name = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoryName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error checking category name existence: " + e.getMessage(), e, 
                                      "CATEGORY_NAME_CHECK_ERROR", e.getSQLState());
        }
        
        return false;
    }
    
    /**
     * Check if category has items
     * @param categoryId Category ID to check
     * @return true if category has items
     * @throws DatabaseException if database operation fails
     */
    private boolean hasItems(int categoryId) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM items WHERE category_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoryId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error checking if category has items: " + e.getMessage(), e, 
                                      "CATEGORY_ITEMS_CHECK_ERROR", e.getSQLState());
        }
        
        return false;
    }
    
    /**
     * Map ResultSet row to Category object
     * @param rs ResultSet
     * @return Category object
     * @throws SQLException if column retrieval fails
     */
    private Category mapRowToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setCategoryId(rs.getInt("category_id"));
        category.setCategoryName(rs.getString("category_name"));
        category.setDescription(rs.getString("description"));
        category.setActive(rs.getBoolean("is_active"));
        
        // Set created_at if available
        try {
            category.setCreatedAt(rs.getTimestamp("created_at"));
        } catch (SQLException e) {
            // Column might not be present in all queries
        }
        
        // Set item count if available (for queries with COUNT)
        try {
            category.setItemCount(rs.getInt("item_count"));
        } catch (SQLException e) {
            // Column might not be present in all queries
        }
        
        return category;
    }
}