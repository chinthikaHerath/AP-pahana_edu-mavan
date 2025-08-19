package com.pahanaedu.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class ModelTest {
    
    @Test
    @DisplayName("Test User model getters and setters")
    public void testUserModel() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setRole("ADMIN");
        user.setActive(true);
        
        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("ADMIN", user.getRole());
        assertTrue(user.isActive());
    }
    
    @Test
    @DisplayName("Test Customer model full address generation")
    public void testCustomerFullAddress() {
        Customer customer = new Customer();
        customer.setAddress("123 Main Street");
        customer.setCity("Colombo");
        customer.setPostalCode("10100");
        
        String fullAddress = customer.getFullAddress();
        assertTrue(fullAddress.contains("123 Main Street"));
        assertTrue(fullAddress.contains("Colombo"));
        assertTrue(fullAddress.contains("10100"));
    }
    
    @Test
    @DisplayName("Test Customer contact numbers formatting")
    public void testCustomerContactNumbers() {
        Customer customer = new Customer();
        customer.setTelephone("0112345678");
        customer.setMobile("0771234567");
        
        String contacts = customer.getContactNumbers();
        assertTrue(contacts.contains("Tel: 0112345678"));
        assertTrue(contacts.contains("Mobile: 0771234567"));
    }
    
    @Test
    @DisplayName("Test Bill payment status methods")
    public void testBillPaymentStatus() {
        Bill bill = new Bill();
        
        bill.setPaymentStatus("PAID");
        assertTrue(bill.isPaid());
        assertFalse(bill.isPending());
        
        bill.setPaymentStatus("PENDING");
        assertFalse(bill.isPaid());
        assertTrue(bill.isPending());
        
        bill.setPaymentStatus("CANCELLED");
        assertTrue(bill.isCancelled());
    }
    
    @Test
    @DisplayName("Test Item model with categories")
    public void testItemModel() {
        Item item = new Item();
        item.setItemCode("TB001");
        item.setItemName("Test Book");
        item.setUnitPrice(100.00);
        item.setSellingPrice(120.00);
        item.setQuantityInStock(50);
        
        assertEquals("TB001", item.getItemCode());
        assertEquals("Test Book", item.getItemName());
        assertEquals(100.00, item.getUnitPrice(), 0.01);
        assertEquals(120.00, item.getSellingPrice(), 0.01);
        assertEquals(50, item.getQuantityInStock());
    }
}