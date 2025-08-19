package com.pahanaedu.service;

import com.pahanaedu.model.Bill;
import com.pahanaedu.model.BillItem;
import com.pahanaedu.model.Item;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class BillingCalculationTest {
    
    @Test
    @DisplayName("Test bill total calculation without discount")
    public void testBillCalculationNoDiscount() {
        Bill bill = new Bill();
        
        // Add items
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
        
        // Recalculate should set subtotal to 350
        bill.recalculateTotals();
        
        assertEquals(350.00, bill.getSubtotal(), 0.01);
        assertEquals(350.00, bill.getTotalAmount(), 0.01);
    }
    
    @Test
    @DisplayName("Test bill calculation with discount percentage")
    public void testBillCalculationWithDiscount() {
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
    }
    
    @Test
    @DisplayName("Test bill calculation with tax")
    public void testBillCalculationWithTax() {
        Bill bill = new Bill();
        bill.setTaxPercentage(15.0);
        
        BillItem item = new BillItem();
        item.setQuantity(2);
        item.setUnitPrice(100.00);
        item.setTotalPrice(200.00);
        
        bill.addBillItem(item);
        bill.recalculateTotals();
        
        assertEquals(200.00, bill.getSubtotal(), 0.01);
        assertEquals(30.00, bill.getTaxAmount(), 0.01);
        assertEquals(230.00, bill.getTotalAmount(), 0.01);
    }
    
    @Test
    @DisplayName("Test bill calculation with discount and tax")
    public void testBillCalculationWithDiscountAndTax() {
        Bill bill = new Bill();
        bill.setDiscountPercentage(10.0); // 10% discount
        bill.setTaxPercentage(15.0); // 15% tax
        
        BillItem item = new BillItem();
        item.setQuantity(10);
        item.setUnitPrice(100.00);
        item.setTotalPrice(1000.00);
        
        bill.addBillItem(item);
        bill.recalculateTotals();
        
        // Subtotal: 1000
        // Discount: 100 (10% of 1000)
        // After discount: 900
        // Tax: 135 (15% of 900)
        // Total: 1035
        
        assertEquals(1000.00, bill.getSubtotal(), 0.01);
        assertEquals(100.00, bill.getDiscountAmount(), 0.01);
        assertEquals(135.00, bill.getTaxAmount(), 0.01);
        assertEquals(1035.00, bill.getTotalAmount(), 0.01);
    }
    
    @Test
    @DisplayName("Test empty bill calculation")
    public void testEmptyBillCalculation() {
        Bill bill = new Bill();
        bill.recalculateTotals();
        
        assertEquals(0.00, bill.getSubtotal(), 0.01);
        assertEquals(0.00, bill.getTotalAmount(), 0.01);
    }
}