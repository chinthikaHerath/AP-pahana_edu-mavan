package com.pahanaedu.dao.interfaces;

import com.pahanaedu.model.Category;
import com.pahanaedu.exception.DatabaseException;
import java.util.List;

/**
 * Interface for Category Data Access Object
 */
public interface CategoryDAO {
    
    /**
     * Get all categories
     * @return List of all categories
     * @throws DatabaseException if database operation fails
     */
    List<Category> getAllCategories() throws DatabaseException;
    
    /**
     * Get category by ID
     * @param categoryId Category ID
     * @return Category object or null if not found
     * @throws DatabaseException if database operation fails
     */
    Category getCategoryById(int categoryId) throws DatabaseException;
    
    /**
     * Add a new category
     * @param category Category to add
     * @return Generated category ID
     * @throws DatabaseException if database operation fails
     */
    int addCategory(Category category) throws DatabaseException;
    
    /**
     * Update an existing category
     * @param category Category with updated information
     * @return true if update successful
     * @throws DatabaseException if database operation fails
     */
    boolean updateCategory(Category category) throws DatabaseException;
    
    /**
     * Delete a category
     * @param categoryId Category ID to delete
     * @return true if deletion successful
     * @throws DatabaseException if database operation fails
     */
    boolean deleteCategory(int categoryId) throws DatabaseException;
    
    /**
     * Get active categories
     * @return List of active categories
     * @throws DatabaseException if database operation fails
     */
    List<Category> getActiveCategories() throws DatabaseException;
    
    /**
     * Check if category name exists
     * @param categoryName Category name to check
     * @return true if category name exists
     * @throws DatabaseException if database operation fails
     */
    boolean isCategoryNameExists(String categoryName) throws DatabaseException;
}