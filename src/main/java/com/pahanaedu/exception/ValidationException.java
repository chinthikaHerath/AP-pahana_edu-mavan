package com.pahanaedu.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom exception class for validation errors
 */
public class ValidationException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    private String fieldName;
    private Object rejectedValue;
    private Map<String, List<String>> fieldErrors;
    
    /**
     * Constructor with message
     */
    public ValidationException(String message) {
        super(message);
        this.fieldErrors = new HashMap<>();
    }
    
    /**
     * Constructor with field name and message
     */
    public ValidationException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
        this.fieldErrors = new HashMap<>();
        addFieldError(fieldName, message);
    }
    
    /**
     * Constructor with field name, rejected value, and message
     */
    public ValidationException(String fieldName, Object rejectedValue, String message) {
        super(message);
        this.fieldName = fieldName;
        this.rejectedValue = rejectedValue;
        this.fieldErrors = new HashMap<>();
        addFieldError(fieldName, message);
    }
    
    /**
     * Constructor with field errors map
     */
    public ValidationException(Map<String, List<String>> fieldErrors) {
        super("Validation failed for multiple fields");
        this.fieldErrors = fieldErrors != null ? fieldErrors : new HashMap<>();
    }
    
    /**
     * Add field error
     */
    public void addFieldError(String fieldName, String errorMessage) {
        if (fieldErrors == null) {
            fieldErrors = new HashMap<>();
        }
        
        fieldErrors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
    }
    
    /**
     * Get field name
     */
    public String getFieldName() {
        return fieldName;
    }
    
    /**
     * Set field name
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
    /**
     * Get rejected value
     */
    public Object getRejectedValue() {
        return rejectedValue;
    }
    
    /**
     * Set rejected value
     */
    public void setRejectedValue(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
    }
    
    /**
     * Get all field errors
     */
    public Map<String, List<String>> getFieldErrors() {
        return fieldErrors;
    }
    
    /**
     * Get errors for a specific field
     */
    public List<String> getFieldErrors(String fieldName) {
        return fieldErrors != null ? fieldErrors.get(fieldName) : null;
    }
    
    /**
     * Check if there are errors for a specific field
     */
    public boolean hasFieldErrors(String fieldName) {
        return fieldErrors != null && fieldErrors.containsKey(fieldName);
    }
    
    /**
     * Check if there are any field errors
     */
    public boolean hasFieldErrors() {
        return fieldErrors != null && !fieldErrors.isEmpty();
    }
    
    /**
     * Get total error count
     */
    public int getErrorCount() {
        if (fieldErrors == null) return 0;
        
        return fieldErrors.values().stream()
                .mapToInt(List::size)
                .sum();
    }
    
    /**
     * Get all error messages as a list
     */
    public List<String> getAllErrorMessages() {
        List<String> allErrors = new ArrayList<>();
        
        if (fieldErrors != null) {
            fieldErrors.forEach((field, errors) -> {
                errors.forEach(error -> {
                    allErrors.add(field + ": " + error);
                });
            });
        }
        
        return allErrors;
    }
    
    /**
     * Get detailed error message
     */
    public String getDetailedMessage() {
        if (!hasFieldErrors()) {
            return getMessage();
        }
        
        StringBuilder details = new StringBuilder("Validation errors:\n");
        fieldErrors.forEach((field, errors) -> {
            details.append("  ").append(field).append(":\n");
            errors.forEach(error -> {
                details.append("    - ").append(error).append("\n");
            });
        });
        
        return details.toString();
    }
    
    @Override
    public String toString() {
        return getDetailedMessage();
    }
}