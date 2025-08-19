package com.pahanaedu.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom exception class for business logic errors
 */
public class BusinessException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    private String errorCode;
    private Map<String, List<String>> businessErrors;
    
    /**
     * Constructor with message
     */
    public BusinessException(String message) {
        super(message);
        this.businessErrors = new HashMap<>();
    }
    
    /**
     * Constructor with message and cause
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.businessErrors = new HashMap<>();
    }
    
    /**
     * Constructor with error code and message
     */
    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.businessErrors = new HashMap<>();
        addBusinessError(errorCode, message);
    }
    
    /**
     * Constructor with error code, message, and cause
     */
    public BusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.businessErrors = new HashMap<>();
        addBusinessError(errorCode, message);
    }
    
    /**
     * Constructor with business errors map
     */
    public BusinessException(Map<String, List<String>> businessErrors) {
        super("Business rule violation occurred");
        this.businessErrors = businessErrors != null ? businessErrors : new HashMap<>();
    }
    
    /**
     * Add business error
     */
    public void addBusinessError(String errorCode, String errorMessage) {
        if (businessErrors == null) {
            businessErrors = new HashMap<>();
        }
        
        businessErrors.computeIfAbsent(errorCode, k -> new ArrayList<>()).add(errorMessage);
    }
    
    /**
     * Get error code
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Set error code
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    /**
     * Get all business errors
     */
    public Map<String, List<String>> getBusinessErrors() {
        return businessErrors;
    }
    
    /**
     * Get errors for a specific error code
     */
    public List<String> getBusinessErrors(String errorCode) {
        return businessErrors != null ? businessErrors.get(errorCode) : null;
    }
    
    /**
     * Check if there are errors for a specific error code
     */
    public boolean hasBusinessErrors(String errorCode) {
        return businessErrors != null && businessErrors.containsKey(errorCode);
    }
    
    /**
     * Check if there are any business errors
     */
    public boolean hasBusinessErrors() {
        return businessErrors != null && !businessErrors.isEmpty();
    }
    
    /**
     * Get total error count
     */
    public int getErrorCount() {
        if (businessErrors == null) return 0;
        
        return businessErrors.values().stream()
                .mapToInt(List::size)
                .sum();
    }
    
    /**
     * Get all error messages as a list
     */
    public List<String> getAllErrorMessages() {
        List<String> allErrors = new ArrayList<>();
        
        if (businessErrors != null) {
            businessErrors.forEach((code, errors) -> {
                errors.forEach(error -> {
                    allErrors.add(code + ": " + error);
                });
            });
        }
        
        return allErrors;
    }
    
    /**
     * Get detailed error message
     */
    public String getDetailedMessage() {
        if (!hasBusinessErrors()) {
            return getMessage();
        }
        
        StringBuilder details = new StringBuilder("Business errors:\n");
        businessErrors.forEach((code, errors) -> {
            details.append("  ").append(code).append(":\n");
            errors.forEach(error -> {
                details.append("    - ").append(error).append("\n");
            });
        });
        
        if (getCause() != null) {
            details.append("  Cause: ").append(getCause().getMessage()).append("\n");
        }
        
        return details.toString();
    }
    
    @Override
    public String toString() {
        return getDetailedMessage();
    }
}