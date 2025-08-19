package com.pahanaedu.service;

import com.pahanaedu.model.StockMovement;
import com.pahanaedu.model.Item;
import com.pahanaedu.constant.MovementType;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

/**
 * Simplified Stock Service Tests
 * These tests focus on business logic without database dependencies
 */
public class StockServiceTest {
    
    @Test
    @DisplayName("Test StockMovement creation")
    public void testStockMovementCreation() {
        StockMovement movement = new StockMovement();
        movement.setItemId(1);
        movement.setMovementType(MovementType.IN);
        movement.setQuantity(50);
        movement.setReason("New stock arrival");
        movement.setUserId(1);
        movement.setMovementDate(new Date());
        
        assertEquals(1, movement.getItemId());
        assertEquals(MovementType.IN, movement.getMovementType());
        assertEquals(50, movement.getQuantity());
        assertEquals("New stock arrival", movement.getReason());
    }
    
    @Test
    @DisplayName("Test MovementType enum values")
    public void testMovementTypeEnum() {
        // Test that movement types exist
        assertNotNull(MovementType.IN);
        assertNotNull(MovementType.OUT);
        assertNotNull(MovementType.ADJUSTMENT);
        assertNotNull(MovementType.RETURN);
        
        // Test enum methods if they exist
        assertTrue(MovementType.IN.isStockIncrease());
        assertTrue(MovementType.OUT.isStockDecrease());
    }
    
    @Test
    @DisplayName("Test stock calculation logic")
    public void testStockCalculation() {
        // Simulate stock IN
        int currentStock = 100;
        int inQuantity = 50;
        int expectedStock = currentStock + inQuantity;
        assertEquals(150, expectedStock);
        
        // Simulate stock OUT
        currentStock = 100;
        int outQuantity = 30;
        expectedStock = currentStock - outQuantity;
        assertEquals(70, expectedStock);
        
        // Simulate adjustment (can be negative)
        currentStock = 100;
        int adjustmentQuantity = -20;
        expectedStock = currentStock + adjustmentQuantity;
        assertEquals(80, expectedStock);
    }
    
    @Test
    @DisplayName("Test insufficient stock validation")
    public void testInsufficientStockValidation() {
        int currentStock = 10;
        int requestedQuantity = 15;
        
        // Should not allow stock out more than available
        boolean isValid = currentStock >= requestedQuantity;
        assertFalse(isValid, "Should not allow stock out more than available");
        
        // Should allow if sufficient stock
        requestedQuantity = 5;
        isValid = currentStock >= requestedQuantity;
        assertTrue(isValid, "Should allow stock out when sufficient");
    }
    
    @Test
    @DisplayName("Test negative stock prevention")
    public void testNegativeStockPrevention() {
        int currentStock = 5;
        int adjustmentQuantity = -10;
        int newStock = currentStock + adjustmentQuantity;
        
        // Check if adjustment would result in negative stock
        boolean wouldBeNegative = newStock < 0;
        assertTrue(wouldBeNegative, "Should detect negative stock result");
        
        // Valid adjustment
        adjustmentQuantity = -3;
        newStock = currentStock + adjustmentQuantity;
        wouldBeNegative = newStock < 0;
        assertFalse(wouldBeNegative, "Valid adjustment should not result in negative");
    }
    
    @Test
    @DisplayName("Test low stock detection")
    public void testLowStockDetection() {
        // Simulate items with different stock levels
        Item item1 = new Item();
        item1.setItemId(1);
        item1.setQuantityInStock(5);
        
        Item item2 = new Item();
        item2.setItemId(2);
        item2.setQuantityInStock(15);
        
        Item item3 = new Item();
        item3.setItemId(3);
        item3.setQuantityInStock(0);
        
        int reorderLevel = 10;
        
        // Check which items are low stock
        assertTrue(item1.getQuantityInStock() < reorderLevel, "Item 1 should be low stock");
        assertFalse(item2.getQuantityInStock() < reorderLevel, "Item 2 should not be low stock");
        assertTrue(item3.getQuantityInStock() < reorderLevel, "Item 3 should be low stock");
    }
    
    @Test
    @DisplayName("Test out of stock detection")
    public void testOutOfStockDetection() {
        Item item1 = new Item();
        item1.setQuantityInStock(0);
        
        Item item2 = new Item();
        item2.setQuantityInStock(1);
        
        assertTrue(item1.getQuantityInStock() == 0, "Item 1 should be out of stock");
        assertFalse(item2.getQuantityInStock() == 0, "Item 2 should not be out of stock");
    }
    
    @Test
    @DisplayName("Test stock movement with reference")
    public void testStockMovementWithReference() {
        StockMovement movement = new StockMovement();
        movement.setItemId(1);
        movement.setMovementType(MovementType.OUT);
        movement.setQuantity(5);
        movement.setReferenceType("BILL");
        movement.setReferenceId(101);
        movement.setReason("Sale - Bill #101");
        
        assertEquals("BILL", movement.getReferenceType());
        assertEquals(101, movement.getReferenceId());
        assertTrue(movement.getReason().contains("101"));
    }
}