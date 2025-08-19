package com.pahanaedu.model;

import java.io.Serializable;

/**
 * BillItem model class representing the bill_items table
 */
public class BillItem implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int billItemId;
    private int billId;
    private int itemId;
    private int quantity;
    private double unitPrice;
    private double discountPercentage;
    private double discountAmount;
    private double totalPrice;
    
    // Transient fields for display purposes
    private transient String itemCode;
    private transient String itemName;
    private transient String categoryName;
    private transient Item item;
    
    // Default constructor
    public BillItem() {
        this.quantity = 1;
        this.discountPercentage = 0.0;
        this.discountAmount = 0.0;
    }
    
    // Constructor with required fields
    public BillItem(int itemId, int quantity, double unitPrice) {
        this();
        this.itemId = itemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        calculateTotalPrice();
    }
    
    // Constructor with item object
    public BillItem(Item item, int quantity) {
        this();
        if (item != null) {
            this.itemId = item.getItemId();
            this.itemCode = item.getItemCode();
            this.itemName = item.getItemName();
            this.unitPrice = item.getSellingPrice();
            this.item = item;
        }
        this.quantity = quantity;
        calculateTotalPrice();
    }
    
    // Full constructor
    public BillItem(int billItemId, int billId, int itemId, int quantity,
                    double unitPrice, double discountPercentage, double discountAmount,
                    double totalPrice) {
        this.billItemId = billItemId;
        this.billId = billId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountPercentage = discountPercentage;
        this.discountAmount = discountAmount;
        this.totalPrice = totalPrice;
    }
    
    // Getters and Setters
    public int getBillItemId() {
        return billItemId;
    }
    
    public void setBillItemId(int billItemId) {
        this.billItemId = billItemId;
    }
    
    public int getBillId() {
        return billId;
    }
    
    public void setBillId(int billId) {
        this.billId = billId;
    }
    
    public int getItemId() {
        return itemId;
    }
    
    public void setItemId(int itemId) {
        this.itemId = itemId;
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
    
    public double getDiscountPercentage() {
        return discountPercentage;
    }
    
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
        calculateTotalPrice();
    }
    
    /**
     * Calculate discount amount for this item
     * @return Discount amount
     */
    public double getDiscountAmount() {
        if (discountPercentage > 0 && unitPrice > 0 && quantity > 0) {
            double totalBeforeDiscount = unitPrice * quantity;
            return (totalBeforeDiscount * discountPercentage) / 100;
        }
        return discountAmount > 0 ? discountAmount : 0.0;
    }
    
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
        calculateTotalPrice();
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    // Transient field getters and setters
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
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public Item getItem() {
        return item;
    }
    
    public void setItem(Item item) {
        this.item = item;
        if (item != null) {
            this.itemId = item.getItemId();
            this.itemCode = item.getItemCode();
            this.itemName = item.getItemName();
            this.unitPrice = item.getSellingPrice();
        }
    }
    
    // Utility methods
    public void calculateTotalPrice() {
        double subtotal = this.unitPrice * this.quantity;
        
        // Calculate discount
        if (this.discountPercentage > 0) {
            this.discountAmount = subtotal * (this.discountPercentage / 100);
        }
        
        this.totalPrice = subtotal - this.discountAmount;
    }
    
    public double getSubtotal() {
        return this.unitPrice * this.quantity;
    }
    
    public void increaseQuantity() {
        this.quantity++;
        calculateTotalPrice();
    }
    
    public void decreaseQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
            calculateTotalPrice();
        }
    }
    
    public boolean isDiscounted() {
        return this.discountAmount > 0;
    }
    
    @Override
    public String toString() {
        return "BillItem{" +
                "billItemId=" + billItemId +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        BillItem billItem = (BillItem) o;
        
        if (billItemId != billItem.billItemId) return false;
        if (billId != billItem.billId) return false;
        return itemId == billItem.itemId;
    }
    
    @Override
    public int hashCode() {
        int result = billItemId;
        result = 31 * result + billId;
        result = 31 * result + itemId;
        return result;
    }
}
