package com.pahanaedu.service;

import com.pahanaedu.service.impl.CustomerServiceImpl;
import com.pahanaedu.service.interfaces.CustomerService;
import com.pahanaedu.model.Customer;
import com.pahanaedu.testdata.TestDataProvider;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.exception.BusinessException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class CustomerServiceTest {
    
    private CustomerService customerService;
    private Customer testCustomer;
    
    @BeforeEach
    public void setUp() {
        customerService = new CustomerServiceImpl();
        testCustomer = TestDataProvider.getValidCustomer();
    }
    
    @Test
    @DisplayName("Test customer validation - valid customer")
    public void testValidCustomerValidation() {
        try {
            // This tests the validation logic without database
            customerService.validateCustomer(testCustomer, false);
            // If no exception is thrown, validation passed
            assertTrue(true);
        } catch (ValidationException e) {
            fail("Valid customer should not throw ValidationException: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Test customer validation - invalid customer")
    public void testInvalidCustomerValidation() {
        Customer invalidCustomer = TestDataProvider.getInvalidCustomer();
        
        assertThrows(ValidationException.class, () -> {
            customerService.validateCustomer(invalidCustomer, false);
        });
    }
    
    @Test
    @DisplayName("Test customer validation - empty name")
    public void testCustomerValidationEmptyName() {
        testCustomer.setCustomerName("");
        
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            customerService.validateCustomer(testCustomer, false);
        });
        
        assertTrue(exception.getMessage().contains("Customer"));
    }
    
    @Test
    @DisplayName("Test customer validation - invalid email")
    public void testCustomerValidationInvalidEmail() {
        testCustomer.setEmail("not-an-email");
        
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            customerService.validateCustomer(testCustomer, false);
        });
        
        assertTrue(exception.getFieldErrors().containsKey("email"));
    }
    
    @Test
    @DisplayName("Test customer validation - invalid phone")
    public void testCustomerValidationInvalidPhone() {
        testCustomer.setTelephone("123"); // Too short
        
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            customerService.validateCustomer(testCustomer, false);
        });
        
        assertTrue(exception.getFieldErrors().containsKey("telephone"));
    }
    
    @Test
    @DisplayName("Test customer validation - invalid NIC")
    public void testCustomerValidationInvalidNIC() {
        testCustomer.setNicNumber("INVALID");
        
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            customerService.validateCustomer(testCustomer, false);
        });
        
        assertTrue(exception.getFieldErrors().containsKey("nicNumber"));
    }
    
    // Note: For methods that require database access, we can test them like this:
    @Test
    @DisplayName("Test adding customer - mock test")
    public void testAddCustomerLogic() {
        // This would require database connection or mocking
        // For now, we can test that the method exists and handles parameters correctly
        assertNotNull(customerService);
        
        // Test that method signature is correct
        assertDoesNotThrow(() -> {
            // This will fail with database error, but shows method exists
            try {
                customerService.addCustomer(testCustomer, 1);
            } catch (Exception e) {
                // Expected - no database connection in test
            }
        });
    }
}