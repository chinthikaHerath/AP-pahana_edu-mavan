package com.pahanaedu.dto;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ItemDTOTest {
    
    @Test
    @DisplayName("Test ItemDTO creation and calculations")
    public void testItemDTOCalculations() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemId(1);
        itemDTO.setItemCode("TB001");
        itemDTO.setItemName("Test Book");
        itemDTO.setQuantity(3);
        itemDTO.setUnitPrice(100.00);
        
        // Calculate total price
        double expectedTotal = itemDTO.getQuantity() * itemDTO.getUnitPrice();
        itemDTO.setTotalPrice(expectedTotal);
        
        assertEquals(300.00, itemDTO.getTotalPrice(), 0.01);
    }
    
    @Test
    @DisplayName("Test ItemDTO with discount")
    public void testItemDTOWithDiscount() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItemId(1);
        itemDTO.setQuantity(5);
        itemDTO.setUnitPrice(50.00);
        itemDTO.setDiscountPercentage(10.0);
        
        // Calculate totals
        double subtotal = itemDTO.getQuantity() * itemDTO.getUnitPrice();
        double discountAmount = subtotal * (itemDTO.getDiscountPercentage() / 100);
        double totalPrice = subtotal - discountAmount;
        
        itemDTO.setDiscountAmount(discountAmount);
        itemDTO.setTotalPrice(totalPrice);
        
        assertEquals(25.00, itemDTO.getDiscountAmount(), 0.01);
        assertEquals(225.00, itemDTO.getTotalPrice(), 0.01);
    }
}