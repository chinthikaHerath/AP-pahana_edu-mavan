package com.pahanaedu.constant;

/**
 * Enum for payment methods
 */
public enum PaymentMethod {
    CASH("Cash", "fa-money-bill", "success"),
    CARD("Credit/Debit Card", "fa-credit-card", "primary"),
    CHEQUE("Cheque", "fa-file-invoice", "info"),
    BANK_TRANSFER("Bank Transfer", "fa-university", "warning");
    
    private final String displayName;
    private final String iconClass;
    private final String colorClass;
    
    PaymentMethod(String displayName, String iconClass, String colorClass) {
        this.displayName = displayName;
        this.iconClass = iconClass;
        this.colorClass = colorClass;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getIconClass() {
        return iconClass;
    }
    
    public String getColorClass() {
        return colorClass;
    }
    
    /**
     * Get PaymentMethod from string value
     * @param value String value
     * @return PaymentMethod or null
     */
    public static PaymentMethod fromString(String value) {
        if (value == null) return null;
        
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.name().equalsIgnoreCase(value)) {
                return method;
            }
        }
        return null;
    }
    
    /**
     * Check if payment method requires additional information
     * @return true if additional info needed
     */
    public boolean requiresAdditionalInfo() {
        return this == CHEQUE || this == BANK_TRANSFER;
    }
    
    /**
     * Get placeholder text for additional information field
     * @return Placeholder text
     */
    public String getAdditionalInfoPlaceholder() {
        switch (this) {
            case CHEQUE:
                return "Enter cheque number";
            case BANK_TRANSFER:
                return "Enter transaction reference";
            default:
                return "";
        }
    }
}