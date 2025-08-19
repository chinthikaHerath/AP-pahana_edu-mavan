package com.pahanaedu.constant;

/**
 * Enum representing different types of stock movements
 */
public enum MovementType {
    
    /**
     * Stock coming into inventory (purchases, returns from customers)
     */
    IN("Stock In", "Addition to inventory"),
    
    /**
     * Stock going out of inventory (sales)
     */
    OUT("Stock Out", "Removal from inventory"),
    
    /**
     * Manual stock adjustment (corrections, audits)
     */
    ADJUSTMENT("Adjustment", "Manual stock adjustment"),
    
    /**
     * Customer returns (items returned to stock)
     */
    RETURN("Return", "Customer return to stock"),
    
    /**
     * Damaged or expired items removed from stock
     */
    DAMAGE("Damage", "Damaged or expired items"),
    
    /**
     * Initial stock when adding new items
     */
    INITIAL("Initial Stock", "Initial stock entry"),
    
    /**
     * Stock transfer between locations (if applicable)
     */
    TRANSFER("Transfer", "Stock transfer");
    
    private final String displayName;
    private final String description;
    
    MovementType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Check if this movement type increases stock
     * @return true if movement increases stock
     */
    public boolean isStockIncrease() {
        return this == IN || this == RETURN || 
               (this == ADJUSTMENT && true) || // Positive adjustments
               this == INITIAL;
    }
    
    /**
     * Check if this movement type decreases stock
     * @return true if movement decreases stock
     */
    public boolean isStockDecrease() {
        return this == OUT || this == DAMAGE || 
               (this == ADJUSTMENT && true); // Negative adjustments
    }
    
    /**
     * Get MovementType from string value
     * @param value String value
     * @return MovementType or null if not found
     */
    public static MovementType fromString(String value) {
        if (value != null) {
            for (MovementType type : MovementType.values()) {
                if (type.name().equalsIgnoreCase(value)) {
                    return type;
                }
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}