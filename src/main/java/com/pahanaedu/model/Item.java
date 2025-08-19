package com.pahanaedu.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Item model class representing the items table
 */
public class Item implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int itemId;
    private String itemCode;
    private String itemName;
    private String description;
    private int categoryId;
    private String author;
    private String publisher;
    private String isbn;
    private double unitPrice;
    private double sellingPrice;
    private int quantityInStock;
    private int reorderLevel;
    private boolean isActive;
    private int createdBy;
    private Date createdAt;
    private Date updatedAt;
    
    // Transient fields for display purposes
    private transient String categoryName;
    private transient String createdByUsername;
    private transient boolean lowStock;
    private transient boolean outOfStock;
    
    // Default constructor
    public Item() {
        this.isActive = true;
        this.quantityInStock = 0;
        this.reorderLevel = 10;
    }
    
    // Constructor with required fields
    public Item(String itemCode, String itemName, double unitPrice, double sellingPrice) {
        this();
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.sellingPrice = sellingPrice;
    }
    
    // Full constructor
    public Item(int itemId, String itemCode, String itemName, String description,
                int categoryId, String author, String publisher, String isbn,
                double unitPrice, double sellingPrice, int quantityInStock,
                int reorderLevel, boolean isActive, int createdBy,
                Date createdAt, Date updatedAt) {
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.description = description;
        this.categoryId = categoryId;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.unitPrice = unitPrice;
        this.sellingPrice = sellingPrice;
        this.quantityInStock = quantityInStock;
        this.reorderLevel = reorderLevel;
        this.isActive = isActive;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getItemId() {
        return itemId;
    }
    
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    
    public String getItemCode() {
        return itemCode;
    }
    
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    
    public String getItemName() {
        return itemName;
    }
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getPublisher() {
        return publisher;
    }
    
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public double getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public double getSellingPrice() {
        return sellingPrice;
    }
    
    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
    
    public int getQuantityInStock() {
        return quantityInStock;
    }
    
    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
    
    public int getReorderLevel() {
        return reorderLevel;
    }
    
    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public int getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Transient field getters and setters
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getCreatedByUsername() {
        return createdByUsername;
    }
    
    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }
    
    public boolean isLowStock() {
        return quantityInStock <= reorderLevel && quantityInStock > 0;
    }
    
    public void setLowStock(boolean lowStock) {
        this.lowStock = lowStock;
    }
    
    public boolean isOutOfStock() {
        return quantityInStock == 0;
    }
    
    public void setOutOfStock(boolean outOfStock) {
        this.outOfStock = outOfStock;
    }
    
    // Utility methods
    public double getProfit() {
        return sellingPrice - unitPrice;
    }
    
    public double getProfitMargin() {
        if (unitPrice == 0) return 0;
        return ((sellingPrice - unitPrice) / unitPrice) * 100;
    }
    
    public String getStockStatus() {
        if (isOutOfStock()) {
            return "Out of Stock";
        } else if (isLowStock()) {
            return "Low Stock";
        } else {
            return "In Stock";
        }
    }
    
    public String getStockStatusClass() {
        if (isOutOfStock()) {
            return "danger";
        } else if (isLowStock()) {
            return "warning";
        } else {
            return "success";
        }
    }
    
    public boolean isBook() {
        return isbn != null && !isbn.trim().isEmpty();
    }
    
    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", itemCode='" + itemCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", sellingPrice=" + sellingPrice +
                ", quantityInStock=" + quantityInStock +
                ", isActive=" + isActive +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Item item = (Item) o;
        
        if (itemId != item.itemId) return false;
        return itemCode != null ? itemCode.equals(item.itemCode) : item.itemCode == null;
    }
    
    @Override
    public int hashCode() {
        int result = itemId;
        result = 31 * result + (itemCode != null ? itemCode.hashCode() : 0);
        return result;
    }
}