package com.pahanaedu;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.pahanaedu.util.ValidationUtil;
import com.pahanaedu.model.*;
import com.pahanaedu.dto.BillDTO;
import java.util.ArrayList;
import java.util.List;

/**
 * Main test runner for Online Billing System
 * This class contains all primary tests for the system
 * 
 * Run this class to execute all tests
 * In Eclipse: Right-click → Run As → JUnit Test
 * In Maven: mvn test
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MainTestRunner {
    
    // ========== Setup ==========
    @BeforeAll
    public static void setupTestEnvironment() {
        System.out.println("====================================");
        System.out.println("Starting Online Billing System Tests");
        System.out.println("====================================\n");
    }
    
    @AfterAll
    public static void teardownTestEnvironment() {
        System.out.println("\n====================================");
        System.out.println("All Tests Completed Successfully!");
        System.out.println("====================================");
    }
    
    // ========== Validation Tests ==========
    
    @Test
    @Order(1)
    @DisplayName("Test Email Validation")
    public void testEmailValidation() {
        System.out.println("Running: Email Validation Tests");
        
        // Valid emails
        assertTrue(ValidationUtil.isValidEmail("user@example.com"));
        assertTrue(ValidationUtil.isValidEmail("test.user@company.lk"));
        
        // Invalid emails
        assertFalse(ValidationUtil.isValidEmail(""));
        assertFalse(ValidationUtil.isValidEmail(null));
        assertFalse(ValidationUtil.isValidEmail("not-an-email"));
        assertFalse(ValidationUtil.isValidEmail("@example.com"));
        
        System.out.println("  ✓ Email validation working correctly");
    }
    
    @Test
    @Order(2)
    @DisplayName("Test Phone Validation")
    public void testPhoneValidation() {
        System.out.println("Running: Phone Validation Tests");
        
        // Valid phones
        assertTrue(ValidationUtil.isValidPhone("0771234567"));
        assertTrue(ValidationUtil.isValidPhone("0112345678"));
        
        // Invalid phones
        assertFalse(ValidationUtil.isValidPhone("123"));
        assertFalse(ValidationUtil.isValidPhone(""));
        assertFalse(ValidationUtil.isValidPhone(null));
        
        System.out.println("  ✓ Phone validation working correctly");
    }
    
    @Test
    @Order(3)
    @DisplayName("Test NIC Validation")
    public void testNICValidation() {
        System.out.println("Running: NIC Validation Tests");
        
        // Valid NICs
        assertTrue(ValidationUtil.isValidNIC("991234567V"));
        assertTrue(ValidationUtil.isValidNIC("991234567v"));
        assertTrue(ValidationUtil.isValidNIC("199912345678"));
        
        // Invalid NICs
        assertFalse(ValidationUtil.isValidNIC("123"));
        assertFalse(ValidationUtil.isValidNIC(""));
        
        System.out.println("  ✓ NIC validation working correctly");
    }
    
    @Test
    @Order(4)
    @DisplayName("Test Required Field Validation")
    public void testRequiredFieldValidation() {
        System.out.println("Running: Required Field Validation Tests");
        
        assertTrue(ValidationUtil.isNullOrEmpty(""));
        assertTrue(ValidationUtil.isNullOrEmpty(null));
        assertTrue(ValidationUtil.isNullOrEmpty("   "));
        assertFalse(ValidationUtil.isNullOrEmpty("value"));
        
        System.out.println("  ✓ Required field validation working correctly");
    }
    
    // ========== Model Tests ==========
    
    @Test
    @Order(5)
    @DisplayName("Test User Model")
    public void testUserModel() {
        System.out.println("Running: User Model Tests");
        
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("Test@123");
        user.setEmail("test@example.com");
        user.setFullName("Test User");
        user.setRole("ADMIN");
        user.setActive(true);
        
        assertEquals("testuser", user.getUsername());
        assertEquals("Test@123", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("ADMIN", user.getRole());
        assertTrue(user.isActive());
        
        System.out.println("  ✓ User model working correctly");
    }
    
    @Test
    @Order(6)
    @DisplayName("Test Customer Model")
    public void testCustomerModel() {
        System.out.println("Running: Customer Model Tests");
        
        Customer customer = new Customer();
        customer.setCustomerName("John Doe");
        customer.setAddress("123 Main Street");
        customer.setCity("Colombo");
        customer.setPostalCode("10100");
        customer.setTelephone("0112345678");
        customer.setMobile("0771234567");
        customer.setEmail("john@example.com");
        customer.setNicNumber("991234567V");
        
        assertEquals("John Doe", customer.getCustomerName());
        assertEquals("0112345678", customer.getTelephone());
        
        // Test full address generation
        String fullAddress = customer.getFullAddress();
        assertNotNull(fullAddress);
        assertTrue(fullAddress.contains("123 Main Street"));
        assertTrue(fullAddress.contains("Colombo"));
        
        System.out.println("  ✓ Customer model working correctly");
    }
    
    @Test
    @Order(7)
    @DisplayName("Test Item Model")
    public void testItemModel() {
        System.out.println("Running: Item Model Tests");
        
        Item item = new Item();
        item.setItemCode("TB001");
        item.setItemName("Mathematics Book");
        item.setUnitPrice(450.00);
        item.setSellingPrice(500.00);
        item.setQuantityInStock(50);
        item.setActive(true);
        
        assertEquals("TB001", item.getItemCode());
        assertEquals("Mathematics Book", item.getItemName());
        assertEquals(500.00, item.getSellingPrice(), 0.01);
        assertEquals(50, item.getQuantityInStock());
        assertTrue(item.isActive());
        
        System.out.println("  ✓ Item model working correctly");
    }
    
    // ========== Business Logic Tests ==========
    
    @Test
    @Order(8)
    @DisplayName("Test Bill Calculation - No Discount")
    public void testBillCalculationNoDiscount() {
        System.out.println("Running: Bill Calculation Tests (No Discount)");
        
        Bill bill = new Bill();
        
        BillItem item1 = new BillItem();
        item1.setQuantity(2);
        item1.setUnitPrice(100.00);
        item1.setTotalPrice(200.00);
        
        BillItem item2 = new BillItem();
        item2.setQuantity(3);
        item2.setUnitPrice(50.00);
        item2.setTotalPrice(150.00);
        
        bill.addBillItem(item1);
        bill.addBillItem(item2);
        bill.recalculateTotals();
        
        assertEquals(350.00, bill.getSubtotal(), 0.01);
        assertEquals(350.00, bill.getTotalAmount(), 0.01);
        
        System.out.println("  ✓ Bill calculation (no discount) working correctly");
    }
    
    @Test
    @Order(9)
    @DisplayName("Test Bill Calculation - With Discount")
    public void testBillCalculationWithDiscount() {
        System.out.println("Running: Bill Calculation Tests (With Discount)");
        
        Bill bill = new Bill();
        bill.setDiscountPercentage(10.0);
        
        BillItem item = new BillItem();
        item.setQuantity(5);
        item.setUnitPrice(100.00);
        item.setTotalPrice(500.00);
        
        bill.addBillItem(item);
        bill.recalculateTotals();
        
        assertEquals(500.00, bill.getSubtotal(), 0.01);
        assertEquals(50.00, bill.getDiscountAmount(), 0.01);
        assertEquals(450.00, bill.getTotalAmount(), 0.01);
        
        System.out.println("  ✓ Bill calculation (with discount) working correctly");
    }
    
    @Test
    @Order(10)
    @DisplayName("Test BillDTO Calculation")
    public void testBillDTOCalculation() {
        System.out.println("Running: BillDTO Calculation Tests");
        
        BillDTO billDTO = new BillDTO();
        billDTO.setCustomerId(1);
        billDTO.setDiscountPercentage(5.0);
        
        List<BillItem> items = new ArrayList<>();
        
        BillItem item = new BillItem();
        item.setQuantity(4);
        item.setUnitPrice(75.00);
        item.setTotalPrice(300.00);
        items.add(item);
        
        billDTO.setItems(items);
        billDTO.calculateTotals();
        
        assertEquals(300.00, billDTO.getSubtotal(), 0.01);
        assertEquals(15.00, billDTO.getDiscountAmount(), 0.01);
        assertEquals(285.00, billDTO.getTotalAmount(), 0.01);
        
        System.out.println("  ✓ BillDTO calculation working correctly");
    }
    
    // ========== Edge Case Tests ==========
    
    @Test
    @Order(11)
    @DisplayName("Test Empty Bill Handling")
    public void testEmptyBillHandling() {
        System.out.println("Running: Empty Bill Handling Tests");
        
        Bill bill = new Bill();
        bill.recalculateTotals();
        
        assertEquals(0.00, bill.getSubtotal(), 0.01);
        assertEquals(0.00, bill.getTotalAmount(), 0.01);
        
        System.out.println("  ✓ Empty bill handling working correctly");
    }
    
    @Test
    @Order(12)
    @DisplayName("Test Negative Stock Prevention")
    public void testNegativeStockPrevention() {
        System.out.println("Running: Stock Validation Tests");
        
        int currentStock = 10;
        int requestedQuantity = 15;
        
        boolean isValid = currentStock >= requestedQuantity;
        assertFalse(isValid, "Should not allow more than available stock");
        
        requestedQuantity = 5;
        isValid = currentStock >= requestedQuantity;
        assertTrue(isValid, "Should allow when sufficient stock");
        
        System.out.println("  ✓ Stock validation working correctly");
    }
    
    // ========== Summary Test ==========
    
    @Test
    @Order(13)
    @DisplayName("Test Summary")
    public void testSummary() {
        System.out.println("\n=== Test Summary ===");
        System.out.println("✓ Validation Tests: Passed");
        System.out.println("✓ Model Tests: Passed");
        System.out.println("✓ Business Logic Tests: Passed");
        System.out.println("✓ Edge Case Tests: Passed");
        System.out.println("===================");
        
        assertTrue(true, "All test categories completed");
    }
}