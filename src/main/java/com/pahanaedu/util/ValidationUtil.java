package com.pahanaedu.util;

import java.util.regex.Pattern;

/**
 * Utility class for common validation methods
 */
public class ValidationUtil {
    
    // Regular expression patterns
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^[0-9]{10}$");
    
    private static final Pattern NIC_PATTERN = 
        Pattern.compile("^([0-9]{9}[vVxX]|[0-9]{12})$");
    
    private static final Pattern USERNAME_PATTERN = 
        Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    
    /**
     * Check if string is null or empty
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validate phone number (Sri Lankan format - 10 digits)
     */
    public static boolean isValidPhone(String phone) {
        if (isNullOrEmpty(phone)) {
            return false;
        }
        // Remove any spaces or dashes
        phone = phone.replaceAll("[\\s-]", "");
        return PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * Validate NIC number (Sri Lankan format)
     * Old format: 9 digits + V/X
     * New format: 12 digits
     */
    public static boolean isValidNIC(String nic) {
        if (isNullOrEmpty(nic)) {
            return false;
        }
        return NIC_PATTERN.matcher(nic).matches();
    }
    
    /**
     * Validate username
     * - 3-20 characters
     * - Only letters, numbers, and underscore
     */
    public static boolean isValidUsername(String username) {
        if (isNullOrEmpty(username)) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }
    
    /**
     * Validate password strength
     * - Minimum 6 characters
     * - At least one uppercase letter
     * - At least one lowercase letter
     * - At least one digit
     */
    public static boolean isValidPassword(String password) {
        if (isNullOrEmpty(password)) {
            return false;
        }
        
        if (password.length() < 6) {
            return false;
        }
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        
        return hasUpper && hasLower && hasDigit;
    }
    
    /**
     * Validate decimal number
     */
    public static boolean isValidDecimal(String value) {
        if (isNullOrEmpty(value)) {
            return false;
        }
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate integer number
     */
    public static boolean isValidInteger(String value) {
        if (isNullOrEmpty(value)) {
            return false;
        }
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validate positive decimal (for prices)
     */
    public static boolean isValidPrice(String price) {
        if (!isValidDecimal(price)) {
            return false;
        }
        double value = Double.parseDouble(price);
        return value >= 0;
    }
    
    /**
     * Validate positive integer (for quantities)
     */
    public static boolean isValidQuantity(String quantity) {
        if (!isValidInteger(quantity)) {
            return false;
        }
        int value = Integer.parseInt(quantity);
        return value >= 0;
    }
    
    /**
     * Sanitize string input to prevent XSS
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        
        // Basic HTML encoding
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#x27;")
                   .replace("/", "&#x2F;");
    }
    
    /**
     * Validate string length
     */
    public static boolean isValidLength(String str, int minLength, int maxLength) {
        if (isNullOrEmpty(str)) {
            return minLength == 0;
        }
        int length = str.trim().length();
        return length >= minLength && length <= maxLength;
    }
}