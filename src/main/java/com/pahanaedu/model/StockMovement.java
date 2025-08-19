package com.pahanaedu.model;

import com.pahanaedu.constant.MovementType;
import java.io.Serializable;
import java.util.Date;

/**
 * StockMovement model class representing the stock_movements table
 */
public class StockMovement implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int movementId;
    private int itemId;
    private MovementType movementType;
    private int quantity;
    private String referenceType;
    private int referenceId;
    private String reason;
    private int userId;
    private Date movementDate;
    
    // Transient fields for display purposes
    private transient String itemCode;
    private transient String itemName;
    private transient String userName;
    private transient double unitPrice;
    private transient double totalValue;
    
    // Default constructor
    public StockMovement() {
        this.movementDate = new Date();
    }
    
    // Constructor with required fields
    public StockMovement(int itemId, MovementType movementType, int quantity, int userId) {
        this();
        this.itemId = itemId;
        this.movementType = movementType;
        this.quantity = quantity;
        this.userId = userId;
    }
    
    // Constructor with reference
    public StockMovement(int itemId, MovementType movementType, int quantity, 
                        String referenceType, int referenceId, int userId) {
        this(itemId, movementType, quantity, userId);
        this.referenceType = referenceType;
        this.referenceId = referenceId;
    }
    
    // Full constructor
    public StockMovement(int movementId, int itemId, MovementType movementType, 
                        int quantity, String referenceType, int referenceId, 
                        String reason, int userId, Date movementDate) {
        this.movementId = movementId;
        this.itemId = itemId;
        this.movementType = movementType;
        this.quantity = quantity;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.reason = reason;
        this.userId = userId;
        this.movementDate = movementDate;
    }
    
    // Getters and Setters
    public int getMovementId() {
        return movementId;
    }
    
    public void setMovementId(int movementId) {
        this.movementId = movementId;
    }
    
    public int getItemId() {
        return itemId;
    }
    
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    
    public MovementType getMovementType() {
        return movementType;
    }
    
    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public String getReferenceType() {
        return referenceType;
    }
    
    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }
    
    public int getReferenceId() {
        return referenceId;
    }
    
    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public Date getMovementDate() {
        return movementDate;
    }
    
    public void setMovementDate(Date movementDate) {
        this.movementDate = movementDate;
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
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public double getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public double getTotalValue() {
        return totalValue;
    }
    
    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }
    
    // Utility methods
    /**
     * Get the absolute quantity (always positive)
     */
    public int getAbsoluteQuantity() {
        return Math.abs(quantity);
    }
    
    /**
     * Check if this is a stock increase movement
     */
    public boolean isStockIncrease() {
        return movementType != null && movementType.isStockIncrease();
    }
    
    /**
     * Check if this is a stock decrease movement
     */
    public boolean isStockDecrease() {
        return movementType != null && movementType.isStockDecrease();
    }
    
    /**
     * Get movement direction symbol
     */
    public String getMovementSymbol() {
        if (isStockIncrease()) {
            return "+";
        } else if (isStockDecrease()) {
            return "-";
        }
        return "";
    }
    
    /**
     * Get movement class for CSS styling
     */
    public String getMovementClass() {
        if (isStockIncrease()) {
            return "text-success";
        } else if (isStockDecrease()) {
            return "text-danger";
        }
        return "text-muted";
    }
    
    /**
     * Get reference display text
     */
    public String getReferenceDisplay() {
        if (referenceType != null && referenceId > 0) {
            return referenceType + "#" + referenceId;
        }
        return "-";
    }
    
    /**
     * Calculate total value of movement
     */
    public double calculateTotalValue() {
        return Math.abs(quantity) * unitPrice;
    }
    
    @Override
    public String toString() {
        return "StockMovement{" +
                "movementId=" + movementId +
                ", itemId=" + itemId +
                ", movementType=" + movementType +
                ", quantity=" + quantity +
                ", referenceType='" + referenceType + '\'' +
                ", referenceId=" + referenceId +
                ", movementDate=" + movementDate +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        StockMovement that = (StockMovement) o;
        
        return movementId == that.movementId;
    }
    
    @Override
    public int hashCode() {
        return movementId;
    }
}