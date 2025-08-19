package com.pahanaedu.dto;

import java.io.Serializable;

/**
 * Data Transfer Object for Item information in bills
 * Used for transferring item data in billing operations
 */
public class ItemDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int itemId;
    private String itemCode;
    private String itemName;
    private int quantity;
    private double unitPrice;
    private double sellingPrice;
    private double discountPercentage;
    private double discountAmount;
    private double totalPrice;
    private int availableStock;
    
    // Default constructor
    public ItemDTO() {
        this.discountPercentage = 0.0;
        this.quantity = 1;
    }
    
    // Constructor with required fields
    public ItemDTO(int itemId, int quantity, double unitPrice) {
        this();
        this.itemId = itemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        calculateTotalPrice();
    }
    
    // Full constructor
    public ItemDTO(int itemId, String itemCode, String itemName, int quantity, 
                   double unitPrice, double sellingPrice, double discountPercentage) {
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.sellingPrice = sellingPrice;
        this.discountPercentage = discountPercentage;
        calculateTotalPrice();
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
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateTotalPrice();
    }
    
    public double getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        calculateTotalPrice();
    }
    
    public double getSellingPrice() {
        return sellingPrice;
    }
    
    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
    
    public double getDiscountPercentage() {
        return discountPercentage;
    }
    
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
        calculateTotalPrice();
    }
    
    public double getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public int getAvailableStock() {
        return availableStock;
    }
    
    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }
    
    /**
     * Calculate total price for this item
     */
    public void calculateTotalPrice() {
        if (quantity > 0 && unitPrice > 0) {
            double subtotal = unitPrice * quantity;
            this.discountAmount = (subtotal * discountPercentage) / 100;
            this.totalPrice = subtotal - discountAmount;
        } else {
            this.discountAmount = 0.0;
            this.totalPrice = 0.0;
        }
    }
    
    /**
     * Recalculate totals (alias for calculateTotalPrice)
     */
    public void recalculateTotals() {
        calculateTotalPrice();
    }
    
    /**
     * Get savings amount (same as discount amount)
     * @return Savings amount
     */
    public double getSavings() {
        return discountAmount;
    }
    
    /**
     * Check if item has discount
     * @return true if discount is applied
     */
    public boolean hasDiscount() {
        return discountPercentage > 0 || discountAmount > 0;
    }
    
    /**
     * Validate item data
     * @return true if valid
     */
    public boolean isValid() {
        return itemId > 0 && quantity > 0 && unitPrice >= 0;
    }
    
    @Override
    public String toString() {
        return "ItemDTO{" +
                "itemId=" + itemId +
                ", itemCode='" + itemCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                '}';
    }
}