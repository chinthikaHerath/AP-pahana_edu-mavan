package com.pahanaedu.constant;

/**
 * Enum for payment status
 */
public enum PaymentStatus {
    PAID("Paid", "success", "fa-check-circle", true),
    PENDING("Pending", "warning", "fa-clock", false),
    PARTIAL("Partial Payment", "info", "fa-adjust", false),
    CANCELLED("Cancelled", "danger", "fa-times-circle", true);
    
    private final String displayName;
    private final String colorClass;
    private final String iconClass;
    private final boolean isFinal;
    
    PaymentStatus(String displayName, String colorClass, String iconClass, boolean isFinal) {
        this.displayName = displayName;
        this.colorClass = colorClass;
        this.iconClass = iconClass;
        this.isFinal = isFinal;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getColorClass() {
        return colorClass;
    }
    
    public String getIconClass() {
        return iconClass;
    }
    
    public boolean isFinal() {
        return isFinal;
    }
    
    /**
     * Get PaymentStatus from string value
     * @param value String value
     * @return PaymentStatus or null
     */
    public static PaymentStatus fromString(String value) {
        if (value == null) return null;
        
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return null;
    }
    
    /**
     * Check if status allows editing
     * @return true if editing is allowed
     */
    public boolean isEditable() {
        return this == PENDING || this == PARTIAL;
    }
    
    /**
     * Check if status allows cancellation
     * @return true if cancellation is allowed
     */
    public boolean isCancellable() {
        return this == PENDING || this == PARTIAL;
    }
    
    /**
     * Check if payment is complete
     * @return true if payment is complete
     */
    public boolean isComplete() {
        return this == PAID;
    }
    
    /**
     * Get next logical status
     * @return Next status in workflow
     */
    public PaymentStatus getNextStatus() {
        switch (this) {
            case PENDING:
                return PAID;
            case PARTIAL:
                return PAID;
            default:
                return this;
        }
    }
}