package com.pahanaedu.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Category model class representing the categories table
 */
public class Category implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int categoryId;
    private String categoryName;
    private String description;
    private boolean isActive;
    private Date createdAt;
    
    // Transient fields for display purposes
    private transient int itemCount;
    
    // Default constructor
    public Category() {
        this.isActive = true;
    }
    
    // Constructor with required fields
    public Category(String categoryName) {
        this();
        this.categoryName = categoryName;
    }
    
    // Constructor with name and description
    public Category(String categoryName, String description) {
        this();
        this.categoryName = categoryName;
        this.description = description;
    }
    
    // Full constructor
    public Category(int categoryId, String categoryName, String description, 
                   boolean isActive, Date createdAt) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    // Transient field getters and setters
    public int getItemCount() {
        return itemCount;
    }
    
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
    
    // Utility methods
    public String getStatus() {
        return isActive ? "Active" : "Inactive";
    }
    
    public String getStatusClass() {
        return isActive ? "success" : "danger";
    }
    
    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Category category = (Category) o;
        
        if (categoryId != category.categoryId) return false;
        return categoryName != null ? categoryName.equals(category.categoryName) : category.categoryName == null;
    }
    
    @Override
    public int hashCode() {
        int result = categoryId;
        result = 31 * result + (categoryName != null ? categoryName.hashCode() : 0);
        return result;
    }
}