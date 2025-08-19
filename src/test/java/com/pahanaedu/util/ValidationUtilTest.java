package com.pahanaedu.util;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilTest {
    
    @Test
    @DisplayName("Test valid email validation")
    public void testValidEmail() {
        assertTrue(ValidationUtil.isValidEmail("test@example.com"));
        assertTrue(ValidationUtil.isValidEmail("user.name@company.co.uk"));
        assertTrue(ValidationUtil.isValidEmail("admin@localhost.lk"));
    }
    
    @Test
    @DisplayName("Test invalid email validation")
    public void testInvalidEmail() {
        assertFalse(ValidationUtil.isValidEmail(""));
        assertFalse(ValidationUtil.isValidEmail(null));
        assertFalse(ValidationUtil.isValidEmail("notanemail"));
        assertFalse(ValidationUtil.isValidEmail("@example.com"));
    }
    
    @Test
    @DisplayName("Test valid phone number validation")
    public void testValidPhone() {
        // Use isValidPhone, not isValidPhoneNumber
        assertTrue(ValidationUtil.isValidPhone("0771234567"));
        assertTrue(ValidationUtil.isValidPhone("0112345678"));
    }
    
    @Test
    @DisplayName("Test invalid phone number validation")
    public void testInvalidPhone() {
        assertFalse(ValidationUtil.isValidPhone(""));
        assertFalse(ValidationUtil.isValidPhone(null));
        assertFalse(ValidationUtil.isValidPhone("123"));
        assertFalse(ValidationUtil.isValidPhone("abcdefghij"));
    }
    
    @Test
    @DisplayName("Test null or empty validation")
    public void testIsNullOrEmpty() {
        assertTrue(ValidationUtil.isNullOrEmpty(""));
        assertTrue(ValidationUtil.isNullOrEmpty(null));
        assertTrue(ValidationUtil.isNullOrEmpty("   "));
        assertFalse(ValidationUtil.isNullOrEmpty("value"));
    }
    
    @Test
    @DisplayName("Test price validation with String")
    public void testIsValidPrice() {
        // isValidPrice takes String parameter
        assertTrue(ValidationUtil.isValidPrice("100.00"));
        assertTrue(ValidationUtil.isValidPrice("0.01"));
        assertFalse(ValidationUtil.isValidPrice("-10.00"));
    }
    
    @Test
    @DisplayName("Test quantity validation")
    public void testIsValidQuantity() {
        assertTrue(ValidationUtil.isValidQuantity("10"));
        assertTrue(ValidationUtil.isValidQuantity("0"));
        assertFalse(ValidationUtil.isValidQuantity("-5"));
        assertFalse(ValidationUtil.isValidQuantity("abc"));
    }
    
    @Test
    @DisplayName("Test NIC validation")
    public void testIsValidNIC() {
        assertTrue(ValidationUtil.isValidNIC("991234567V"));
        assertTrue(ValidationUtil.isValidNIC("991234567v"));
        assertTrue(ValidationUtil.isValidNIC("199912345678"));
        assertFalse(ValidationUtil.isValidNIC("123"));
        assertFalse(ValidationUtil.isValidNIC(""));
    }
    
    @Test
    @DisplayName("Test username validation")
    public void testIsValidUsername() {
        assertTrue(ValidationUtil.isValidUsername("user123"));
        assertTrue(ValidationUtil.isValidUsername("admin_user"));
        assertFalse(ValidationUtil.isValidUsername("ab")); // Too short
        assertFalse(ValidationUtil.isValidUsername("user@123")); // Invalid chars
    }
    
    @Test
    @DisplayName("Test string length validation")
    public void testIsValidLength() {
        assertTrue(ValidationUtil.isValidLength("test", 1, 10));
        assertTrue(ValidationUtil.isValidLength("hello", 5, 5));
        assertFalse(ValidationUtil.isValidLength("hi", 3, 10));
        assertFalse(ValidationUtil.isValidLength("toolongstring", 1, 5));
    }
}