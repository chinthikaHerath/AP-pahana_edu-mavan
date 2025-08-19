package com.pahanaedu.service;

import com.pahanaedu.model.*;
import com.pahanaedu.dto.BillDTO;
import com.pahanaedu.dto.ItemDTO;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class BillingServiceTest {
    
    @Test
    @DisplayName("Test BillDTO calculation without discount")
    public void testBillDTOCalculationNoDiscount() {
        BillDTO billDTO = new BillDTO();
        billDTO.setCustomerId(1);
        
        // Create bill items
        List<BillItem> items = new ArrayList<>();
        
        BillItem item1 = new BillItem();
        item1.setItemId(1);
        item1.setQuantity(2);
        item1.setUnitPrice(100.00);
        item1.setTotalPrice(200.00);
        items.add(item1);
        
        BillItem item2 = new BillItem();
        item2.setItemId(2);
        item2.setQuantity(3);
        item2.setUnitPrice(50.00);
        item2.setTotalPrice(150.00);
        items.add(item2);
        
        // Set items using setItems() not setBillItems()
        billDTO.setItems(items);
        
        // Calculate totals
        billDTO.calculateTotals();
        
        // Test calculations
        assertEquals(350.00, billDTO.getSubtotal(), 0.01);
        assertEquals(350.00, billDTO.getTotalAmount(), 0.01);
        assertEquals(0.00, billDTO.getDiscountAmount(), 0.01);
    }
    
    @Test
    @DisplayName("Test BillDTO calculation with discount")
    public void testBillDTOCalculationWithDiscount() {
        BillDTO billDTO = new BillDTO();
        billDTO.setCustomerId(1);
        billDTO.setDiscountPercentage(10.0);
        
        List<BillItem> items = new ArrayList<>();
        BillItem item = new BillItem();
        item.setItemId(1);
        item.setQuantity(5);
        item.setUnitPrice(100.00);
        item.setTotalPrice(500.00);
        items.add(item);
        
        billDTO.setItems(items);
        billDTO.calculateTotals();
        
        // Expected: Subtotal = 500, Discount = 50, Total = 450
        assertEquals(500.00, billDTO.getSubtotal(), 0.01);
        assertEquals(50.00, billDTO.getDiscountAmount(), 0.01);
        assertEquals(450.00, billDTO.getTotalAmount(), 0.01);
    }
    
    @Test
    @DisplayName("Test BillDTO calculation with tax")
    public void testBillDTOCalculationWithTax() {
        BillDTO billDTO = new BillDTO();
        billDTO.setCustomerId(1);
        billDTO.setTaxPercentage(15.0);
        
        List<BillItem> items = new ArrayList<>();
        BillItem item = new BillItem();
        item.setItemId(1);
        item.setQuantity(2);
        item.setUnitPrice(100.00);
        item.setTotalPrice(200.00);
        items.add(item);
        
        billDTO.setItems(items);
        billDTO.calculateTotals();
        
        // Expected: Subtotal = 200, Tax = 30, Total = 230
        assertEquals(200.00, billDTO.getSubtotal(), 0.01);
        assertEquals(30.00, billDTO.getTaxAmount(), 0.01);
        assertEquals(230.00, billDTO.getTotalAmount(), 0.01);
    }
    
    @Test
    @DisplayName("Test BillDTO calculation with discount and tax")
    public void testBillDTOCalculationWithDiscountAndTax() {
        BillDTO billDTO = new BillDTO();
        billDTO.setCustomerId(1);
        billDTO.setDiscountPercentage(10.0);
        billDTO.setTaxPercentage(15.0);
        
        List<BillItem> items = new ArrayList<>();
        BillItem item = new BillItem();
        item.setItemId(1);
        item.setQuantity(10);
        item.setUnitPrice(100.00);
        item.setTotalPrice(1000.00);
        items.add(item);
        
        billDTO.setItems(items);
        billDTO.calculateTotals();
        
        // Expected: 
        // Subtotal = 1000
        // Discount = 100 (10% of 1000)
        // After discount = 900
        // Tax = 135 (15% of 900)
        // Total = 1035
        assertEquals(1000.00, billDTO.getSubtotal(), 0.01);
        assertEquals(100.00, billDTO.getDiscountAmount(), 0.01);
        assertEquals(135.00, billDTO.getTaxAmount(), 0.01);
        assertEquals(1035.00, billDTO.getTotalAmount(), 0.01);
    }
    
    @Test
    @DisplayName("Test BillDTO with ItemDTOs")
    public void testBillDTOWithItemDTOs() {
        BillDTO billDTO = new BillDTO();
        billDTO.setCustomerId(1);
        billDTO.setDiscountPercentage(5.0);
        
        // Create ItemDTOs instead of BillItems
        List<ItemDTO> itemDTOs = new ArrayList<>();
        
        ItemDTO itemDTO1 = new ItemDTO();
        itemDTO1.setItemId(1);
        itemDTO1.setQuantity(2);
        itemDTO1.setUnitPrice(100.00);
        itemDTO1.setTotalPrice(200.00);
        itemDTOs.add(itemDTO1);
        
        ItemDTO itemDTO2 = new ItemDTO();
        itemDTO2.setItemId(2);
        itemDTO2.setQuantity(1);
        itemDTO2.setUnitPrice(50.00);
        itemDTO2.setTotalPrice(50.00);
        itemDTOs.add(itemDTO2);
        
        billDTO.setItemDTOs(itemDTOs);
        billDTO.calculateTotalsFromDTOs();
        
        // Expected: Subtotal = 250, Discount = 12.50, Total = 237.50
        assertEquals(250.00, billDTO.getSubtotal(), 0.01);
        assertEquals(12.50, billDTO.getDiscountAmount(), 0.01);
        assertEquals(237.50, billDTO.getTotalAmount(), 0.01);
    }
    
    @Test
    @DisplayName("Test empty BillDTO")
    public void testEmptyBillDTO() {
        BillDTO billDTO = new BillDTO();
        billDTO.setCustomerId(1);
        billDTO.setItems(new ArrayList<>());
        
        billDTO.calculateTotals();
        
        assertEquals(0.00, billDTO.getSubtotal(), 0.01);
        assertEquals(0.00, billDTO.getTotalAmount(), 0.01);
        assertEquals(0, billDTO.getItemCount());
    }
    
    @Test
    @DisplayName("Test BillDTO item count and quantity")
    public void testBillDTOItemCountAndQuantity() {
        BillDTO billDTO = new BillDTO();
        
        List<BillItem> items = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            BillItem item = new BillItem();
            item.setItemId(i);
            item.setQuantity(i * 2); // 2, 4, 6
            items.add(item);
        }
        
        billDTO.setItems(items);
        
        assertEquals(3, billDTO.getItemCount());
        assertEquals(12, billDTO.getTotalQuantity()); // 2 + 4 + 6
    }
    
    @Test
    @DisplayName("Test bill number format validation")
    public void testBillNumberFormat() {
        // This tests the expected format of bill numbers
        String testBillNumber = "BILL-2024-001";
        
        assertTrue(testBillNumber.startsWith("BILL-"));
        assertTrue(testBillNumber.matches("BILL-\\d{4}-\\d{3,}"));
        
        // Test invalid formats
        assertFalse("INVOICE-2024-001".matches("BILL-\\d{4}-\\d{3,}"));
        assertFalse("BILL-24-001".matches("BILL-\\d{4}-\\d{3,}"));
    }
}