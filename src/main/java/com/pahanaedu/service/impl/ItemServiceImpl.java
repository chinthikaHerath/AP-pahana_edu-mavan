package com.pahanaedu.service.impl;

import com.pahanaedu.dao.interfaces.ItemDAO;
import com.pahanaedu.dao.interfaces.CategoryDAO;
import com.pahanaedu.dao.interfaces.StockMovementDAO;
import com.pahanaedu.dao.impl.ItemDAOImpl;
import com.pahanaedu.dao.impl.CategoryDAOImpl;
import com.pahanaedu.dao.impl.StockMovementDAOImpl;
import com.pahanaedu.exception.BusinessException;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.exception.DatabaseException;
import com.pahanaedu.model.Item;
import com.pahanaedu.model.Category;
import com.pahanaedu.model.StockMovement;
import com.pahanaedu.constant.MovementType;
import com.pahanaedu.service.interfaces.ItemService;
import com.pahanaedu.util.ValidationUtil;
import com.pahanaedu.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Date;

/**
 * Implementation of ItemService interface
 * Handles business logic for item and stock management
 */
public class ItemServiceImpl implements ItemService {
    private final ItemDAO itemDAO;
    private final CategoryDAO categoryDAO;
    private final StockMovementDAO stockMovementDAO;

    public ItemServiceImpl() {
        this.itemDAO = new ItemDAOImpl();
        this.categoryDAO = new CategoryDAOImpl();
        this.stockMovementDAO = new StockMovementDAOImpl();
    }

    // ========== Existing Item Management Methods ==========
    
    @Override
    public int addItem(Item item, int createdBy) throws ValidationException, BusinessException {
        validateItem(item);
        item.setCreatedBy(createdBy);
        
        try {
            // Check if item code already exists
            if (itemDAO.isItemCodeExists(item.getItemCode())) {
                throw new BusinessException("Item code already exists: " + item.getItemCode());
            }
            
            // Check if ISBN already exists (if provided)
            if (item.getIsbn() != null && !item.getIsbn().trim().isEmpty()) {
                if (itemDAO.isISBNExists(item.getIsbn())) {
                    throw new BusinessException("ISBN already exists: " + item.getIsbn());
                }
            }
            
            return itemDAO.addItem(item);
        } catch (DatabaseException e) {
            throw new BusinessException("Error adding item to database: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateItem(Item item) throws ValidationException, BusinessException {
        validateItem(item);
        
        try {
            // Check if item exists
            Item existingItem = itemDAO.getItemById(item.getItemId());
            if (existingItem == null) {
                throw new BusinessException("Item not found");
            }
            
            // Check if item code is being changed and if new code already exists
            if (!existingItem.getItemCode().equals(item.getItemCode())) {
                if (itemDAO.isItemCodeExists(item.getItemCode())) {
                    throw new BusinessException("Item code already exists: " + item.getItemCode());
                }
            }
            
            // Check if ISBN is being changed and if new ISBN already exists
            if (item.getIsbn() != null && !item.getIsbn().trim().isEmpty()) {
                if (!item.getIsbn().equals(existingItem.getIsbn()) && itemDAO.isISBNExists(item.getIsbn())) {
                    throw new BusinessException("ISBN already exists: " + item.getIsbn());
                }
            }
            
            return itemDAO.updateItem(item);
        } catch (DatabaseException e) {
            throw new BusinessException("Error updating item: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deactivateItem(int itemId) throws BusinessException {
        try {
            // Check if item exists
            Item item = itemDAO.getItemById(itemId);
            if (item == null) {
                throw new BusinessException("Item not found with ID: " + itemId);
            }
            
            // Check if already inactive
            if (!item.isActive()) {
                throw new BusinessException("Item is already inactive");
            }
            
            // Warning about stock (not blocking by default)
            if (item.getQuantityInStock() > 0) {
                // Log warning but allow deactivation
                // You could make this configurable or add a force parameter
                System.out.println("Warning: Deactivating item with stock: " + item.getItemCode());
            }
            
            // Deactivate the item
            boolean success = itemDAO.deactivateItem(itemId);
            
            if (success) {
                // Log the deactivation (optional)
                // auditLogger.log("Item deactivated", itemId, userId);
            }
            
            return success;
            
        } catch (DatabaseException e) {
            throw new BusinessException("Error deactivating item: " + e.getMessage(), e);
        }
    }

    @Override
    public Item getItemById(int itemId) throws BusinessException {
        try {
            return itemDAO.getItemById(itemId);
        } catch (DatabaseException e) {
            throw new BusinessException("Error retrieving item: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Item> getAllActiveItems() throws BusinessException {
        try {
            return itemDAO.getAllActiveItems();
        } catch (DatabaseException e) {
            throw new BusinessException("Error retrieving active items: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Item> getActiveItems() throws BusinessException {
        return getAllActiveItems();
    }

    @Override
    public List<Item> searchItems(String searchTerm) throws BusinessException {
        try {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return getAllActiveItems();
            }
            return itemDAO.searchItems(searchTerm);
        } catch (DatabaseException e) {
            throw new BusinessException("Error searching items: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Item> getItemsByCategory(int categoryId) throws BusinessException {
        try {
            return itemDAO.getItemsByCategory(categoryId);
        } catch (DatabaseException e) {
            throw new BusinessException("Error retrieving items by category: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Item> getLowStockItems() throws BusinessException {
        try {
            return itemDAO.getLowStockItems();
        } catch (DatabaseException e) {
            throw new BusinessException("Error retrieving low stock items: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Category> getAllCategories() throws BusinessException {
        try {
            return categoryDAO.getAllCategories();
        } catch (DatabaseException e) {
            throw new BusinessException("Error retrieving categories: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean increaseStock(int itemId, int quantity) throws BusinessException {
        if (quantity <= 0) {
            throw new BusinessException("Quantity to increase must be positive");
        }
        
        try {
            Item item = itemDAO.getItemById(itemId);
            if (item == null) {
                throw new BusinessException("Item not found");
            }
            
            int newQuantity = item.getQuantityInStock() + quantity;
            return itemDAO.updateItemStock(itemId, newQuantity);
        } catch (DatabaseException e) {
            throw new BusinessException("Error increasing stock: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean decreaseStock(int itemId, int quantity) throws BusinessException {
        if (quantity <= 0) {
            throw new BusinessException("Quantity to decrease must be positive");
        }
        
        try {
            Item item = itemDAO.getItemById(itemId);
            if (item == null) {
                throw new BusinessException("Item not found");
            }
            
            if (item.getQuantityInStock() < quantity) {
                throw new BusinessException("Insufficient stock. Available: " + item.getQuantityInStock() + ", Requested: " + quantity);
            }
            
            int newQuantity = item.getQuantityInStock() - quantity;
            return itemDAO.updateItemStock(itemId, newQuantity);
        } catch (DatabaseException e) {
            throw new BusinessException("Error decreasing stock: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean adjustStock(int itemId, int newQuantity) throws BusinessException {
        if (newQuantity < 0) {
            throw new BusinessException("Stock quantity cannot be negative");
        }
        
        try {
            Item item = itemDAO.getItemById(itemId);
            if (item == null) {
                throw new BusinessException("Item not found");
            }
            
            return itemDAO.updateItemStock(itemId, newQuantity);
        } catch (DatabaseException e) {
            throw new BusinessException("Error adjusting stock: " + e.getMessage(), e);
        }
    }

    // ========== NEW: Stock Movement Methods Implementation ==========
    
    @Override
    public boolean adjustStockWithMovement(int itemId, MovementType movementType, 
                                          int quantity, String reason, int userId) 
                                          throws BusinessException {
        
        // Validate inputs
        if (quantity <= 0) {
            throw new BusinessException("Quantity must be positive");
        }
        
        if (movementType == null) {
            throw new BusinessException("Movement type is required");
        }
        
        if (ValidationUtil.isNullOrEmpty(reason)) {
            throw new BusinessException("Reason is required for stock adjustment");
        }
        
        Connection conn = null;
        
        try {
            // Get database connection
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Get current item
            Item item = itemDAO.getItemById(itemId);
            if (item == null) {
                throw new BusinessException("Item not found");
            }
            
            // Calculate new quantity based on movement type
            int newQuantity = item.getQuantityInStock();
            int movementQuantity = quantity; // Quantity to record in movement
            
            switch (movementType) {
                case IN:
                case RETURN:
                case INITIAL:
                    // Stock increase movements
                    newQuantity += quantity;
                    break;
                    
                case OUT:
                case DAMAGE:
                    // Stock decrease movements
                    if (item.getQuantityInStock() < quantity) {
                        throw new BusinessException(
                            String.format("Insufficient stock. Available: %d, Requested: %d", 
                                        item.getQuantityInStock(), quantity));
                    }
                    newQuantity -= quantity;
                    break;
                    
                case ADJUSTMENT:
                    // For adjustment, quantity is the new absolute value
                    movementQuantity = quantity - item.getQuantityInStock();
                    newQuantity = quantity;
                    break;
                    
                case TRANSFER:
                    // Transfer logic would go here if needed
                    throw new BusinessException("Transfer movement type not yet implemented");
                    
                default:
                    throw new BusinessException("Unknown movement type: " + movementType);
            }
            
            // Validate new quantity
            if (newQuantity < 0) {
                throw new BusinessException("Stock cannot be negative");
            }
            
            // Create stock movement record
            StockMovement movement = new StockMovement();
            movement.setItemId(itemId);
            movement.setMovementType(movementType);
            movement.setQuantity(movementQuantity);
            movement.setReason(reason);
            movement.setUserId(userId);
            movement.setReferenceType("MANUAL");
            movement.setReferenceId(0);
            
            // Save movement record
            int movementId = stockMovementDAO.addStockMovement(movement);
            
            if (movementId <= 0) {
                throw new BusinessException("Failed to create stock movement record");
            }
            
            // Update item stock
            boolean stockUpdated = itemDAO.updateItemStock(itemId, newQuantity);
            
            if (!stockUpdated) {
                throw new BusinessException("Failed to update item stock");
            }
            
            // Commit transaction
            conn.commit();
            return true;
            
        } catch (DatabaseException e) {
            // Rollback transaction on database error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    // Log rollback error
                }
            }
            throw new BusinessException("Database error during stock adjustment: " + e.getMessage(), e);
            
        } catch (SQLException e) {
            // Rollback transaction on SQL error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    // Log rollback error
                }
            }
            throw new BusinessException("Connection error during stock adjustment: " + e.getMessage(), e);
            
        } catch (BusinessException e) {
            // Rollback transaction on business error
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    // Log rollback error
                }
            }
            throw e; // Re-throw business exception
            
        } finally {
            // Reset auto-commit
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    // Log error
                }
            }
        }
    }
    
    @Override
    public List<StockMovement> getStockHistory(int itemId) throws BusinessException {
        try {
            return stockMovementDAO.getMovementsByItemId(itemId);
        } catch (DatabaseException e) {
            throw new BusinessException("Error retrieving stock history: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<StockMovement> getRecentStockMovements(int limit) throws BusinessException {
        if (limit <= 0) {
            limit = 10; // Default to 10 recent movements
        }
        
        try {
            return stockMovementDAO.getRecentMovements(limit);
        } catch (DatabaseException e) {
            throw new BusinessException("Error retrieving recent stock movements: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<StockMovement> getStockMovementsByDateRange(int itemId, Date startDate, Date endDate) 
                                                           throws BusinessException {
        if (startDate == null || endDate == null) {
            throw new BusinessException("Start date and end date are required");
        }
        
        if (startDate.after(endDate)) {
            throw new BusinessException("Start date must be before end date");
        }
        
        try {
            if (itemId > 0) {
                // Get movements for specific item
                return stockMovementDAO.getAuditTrail(itemId, startDate, endDate);
            } else {
                // Get movements for all items
                return stockMovementDAO.getMovementsByDateRange(startDate, endDate);
            }
        } catch (DatabaseException e) {
            throw new BusinessException("Error retrieving stock movements by date: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Item> getOutOfStockItems() throws BusinessException {
        try {
            return itemDAO.getOutOfStockItems();
        } catch (DatabaseException e) {
            throw new BusinessException("Error retrieving out of stock items: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Integer> getItemCountByCategory() throws BusinessException {
        try {
            Map<String, Integer> categoryCount = new HashMap<>();
            List<Category> categories = categoryDAO.getAllCategories();
            
            for (Category category : categories) {
                List<Item> items = itemDAO.getItemsByCategory(category.getCategoryId());
                categoryCount.put(category.getCategoryName(), items.size());
            }
            
            return categoryCount;
        } catch (DatabaseException e) {
            throw new BusinessException("Error getting item count by category: " + e.getMessage(), e);
        }
    }

    @Override
    public double getTotalInventoryValue() throws BusinessException {
        try {
            List<Item> allItems = itemDAO.getAllActiveItems();
            double totalValue = 0.0;
            
            for (Item item : allItems) {
                totalValue += (item.getUnitPrice() * item.getQuantityInStock());
            }
            
            return totalValue;
        } catch (DatabaseException e) {
            throw new BusinessException("Error calculating inventory value: " + e.getMessage(), e);
        }
    }
    
    
    @Override
    public boolean activateItem(int itemId) throws BusinessException {
        try {
            // Check if item exists
            Item item = itemDAO.getItemById(itemId);
            if (item == null) {
                throw new BusinessException("Item not found with ID: " + itemId);
            }
            
            // Check if already active
            if (item.isActive()) {
                throw new BusinessException("Item is already active");
            }
            
            // Activate the item
            boolean success = itemDAO.activateItem(itemId);
            
            if (success) {
                // Log the activation (optional - if you have audit logging)
                // auditLogger.log("Item activated", itemId, userId);
            }
            
            return success;
            
        } catch (DatabaseException e) {
            throw new BusinessException("Error activating item: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Item> getAllItems(boolean includeInactive) throws BusinessException {
        try {
            if (includeInactive) {
                return itemDAO.getAllItems(); // Returns both active and inactive
            } else {
                return itemDAO.getAllActiveItems(); // Returns only active items
            }
        } catch (DatabaseException e) {
            throw new BusinessException("Error retrieving items: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Item> getInactiveItems() throws BusinessException {
        try {
            return itemDAO.getInactiveItems();
        } catch (DatabaseException e) {
            throw new BusinessException("Error retrieving inactive items: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean canDeactivateItem(int itemId) throws BusinessException {
        try {
            Item item = itemDAO.getItemById(itemId);
            if (item == null) {
                throw new BusinessException("Item not found");
            }
            
            // Check various conditions
            if (!item.isActive()) {
                throw new BusinessException("Item is already inactive");
            }
            
            // You can add more business rules here
            // For example: check if item is in any pending orders
            
            // Warning (not blocking) if item has stock
            if (item.getQuantityInStock() > 0) {
                // This is just a warning, not a blocker
                // The controller can use force=true to override
                return true; // Can deactivate, but with warning
            }
            
            return true;
            
        } catch (DatabaseException e) {
            throw new BusinessException("Error checking deactivation eligibility: " + e.getMessage(), e);
        }
    }

    // Private validation method
    private void validateItem(Item item) throws ValidationException {
        Map<String, List<String>> errors = new HashMap<>();
        
        if (item.getItemCode() == null || item.getItemCode().trim().isEmpty()) {
            errors.put("itemCode", Arrays.asList("Item code is required"));
        } else if (item.getItemCode().length() > 20) {
            errors.put("itemCode", Arrays.asList("Item code must not exceed 20 characters"));
        }
        
        if (item.getItemName() == null || item.getItemName().trim().isEmpty()) {
            errors.put("itemName", Arrays.asList("Item name is required"));
        } else if (item.getItemName().length() > 100) {
            errors.put("itemName", Arrays.asList("Item name must not exceed 100 characters"));
        }
        
        if (item.getCategoryId() <= 0) {
            errors.put("categoryId", Arrays.asList("Category is required"));
        }
        
        if (item.getUnitPrice() < 0) {
            errors.put("unitPrice", Arrays.asList("Unit price cannot be negative"));
        }
        
        if (item.getSellingPrice() <= 0) {
            errors.put("sellingPrice", Arrays.asList("Selling price must be positive"));
        }
        
        if (item.getSellingPrice() < item.getUnitPrice()) {
            errors.put("sellingPrice", Arrays.asList("Selling price cannot be less than unit price"));
        }
        
        if (item.getQuantityInStock() < 0) {
            errors.put("quantityInStock", Arrays.asList("Stock quantity cannot be negative"));
        }
        
        if (item.getReorderLevel() < 0) {
            errors.put("reorderLevel", Arrays.asList("Reorder level cannot be negative"));
        }
        
        // Validate ISBN format if provided
        if (item.getIsbn() != null && !item.getIsbn().trim().isEmpty()) {
            String isbn = item.getIsbn().replaceAll("-", "").replaceAll(" ", "");
            if (isbn.length() != 10 && isbn.length() != 13) {
                errors.put("isbn", Arrays.asList("ISBN must be 10 or 13 digits"));
            }
        }
        
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}