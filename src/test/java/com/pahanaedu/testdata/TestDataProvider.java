package com.pahanaedu.testdata;

import com.pahanaedu.model.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class TestDataProvider {
    
    // Valid Test Data
    public static User getValidAdminUser() {
        User user = new User();
        user.setUserId(1);
        user.setUsername("admin");
        user.setPassword("admin123");
        user.setEmail("admin@test.com");
        user.setFullName("Admin User");
        user.setRole("ADMIN"); // String, not enum
        user.setActive(true);
        return user;
    }
    
    public static User getValidStaffUser() {
        User user = new User();
        user.setUserId(2);
        user.setUsername("staff1");
        user.setPassword("staff123");
        user.setEmail("staff@test.com");
        user.setFullName("Staff User");
        user.setRole("STAFF"); // String
        user.setActive(true);
        return user;
    }
    
    public static Customer getValidCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setAccountNumber("ACC001");
        customer.setCustomerName("John Doe");
        customer.setAddress("123 Main Street");
        customer.setCity("Colombo");
        customer.setPostalCode("10100");
        customer.setTelephone("0112345678"); // Use setTelephone, not setPhone
        customer.setMobile("0771234567");
        customer.setEmail("john@example.com");
        customer.setNicNumber("991234567V");
        customer.setRegistrationDate(new Date());
        customer.setActive(true);
        customer.setCreatedBy(1);
        return customer;
    }
    
    public static Item getValidItem() {
        Item item = new Item();
        item.setItemId(1);
        item.setItemCode("TB001");
        item.setItemName("Mathematics Grade 10");
        item.setDescription("Grade 10 Math textbook");
        item.setCategoryId(1);
        item.setUnitPrice(450.00);
        item.setSellingPrice(500.00);
        item.setQuantityInStock(50);
        item.setActive(true);
        return item;
    }
    
    public static Bill getValidBill() {
        Bill bill = new Bill();
        bill.setBillId(1);
        bill.setBillNumber("BILL-2024-001");
        bill.setCustomerId(1);
        bill.setBillDate(new Date());
        bill.setSubtotal(1000.00); // Use setSubtotal (lowercase 't')
        bill.setDiscountPercentage(10.0);
        bill.setDiscountAmount(100.00);
        bill.setTotalAmount(900.00);
        bill.setPaymentMethod("CASH"); // String, not enum
        bill.setPaymentStatus("PAID"); // String, not enum
        return bill;
    }
    
    // Invalid Test Data (for negative testing)
    public static User getInvalidUser() {
        User user = new User();
        user.setUsername(""); // Empty username
        user.setPassword("123"); // Too short password
        user.setEmail("invalid-email"); // Invalid email format
        return user;
    }
    
    public static Customer getInvalidCustomer() {
        Customer customer = new Customer();
        customer.setCustomerName(""); // Empty name
        customer.setEmail("not-an-email"); // Invalid email
        customer.setTelephone("123"); // Invalid phone
        return customer;
    }
    
    public static Item getInvalidItem() {
        Item item = new Item();
        item.setItemCode(""); // Empty code
        item.setItemName(""); // Empty name
        item.setUnitPrice(-100.00); // Negative price
        item.setQuantityInStock(-5); // Negative stock
        return item;
    }
    
    // Lists for bulk testing
    public static List<Customer> getCustomerList() {
        List<Customer> customers = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Customer customer = new Customer();
            customer.setCustomerId(i);
            customer.setAccountNumber("ACC00" + i);
            customer.setCustomerName("Customer " + i);
            customer.setAddress("Address " + i);
            customer.setTelephone("011234567" + i);
            customer.setEmail("customer" + i + "@test.com");
            customer.setNicNumber("99123456" + i + "V");
            customer.setActive(true);
            customers.add(customer);
        }
        return customers;
    }
}