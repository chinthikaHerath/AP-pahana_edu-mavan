package com.pahanaedu.dto;

import com.pahanaedu.model.BillItem;
import com.pahanaedu.model.Customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Transfer Object for Bill information
 * Used for transferring bill data between layers
 */
public class BillDTO {
    private String billNumber;
    private int customerId;
    private Customer customer;
    private Date billDate;
    private List<BillItem> items;
    private List<ItemDTO> itemDTOs;
    private double subtotal;
    private double discountPercentage;
    private double discountAmount;
    private double taxPercentage;
    private double taxAmount;
    private double totalAmount;
    private double totalSavings;
    private String paymentMethod;
    private String paymentStatus;
    private String notes;
    
 // Default constructor
    public BillDTO() {
        this.discountPercentage = 0.0;
        this.taxPercentage = 0.0;
        this.itemDTOs = new ArrayList<>();
    }
    
 // Constructor with required fields
    public BillDTO(String billNumber, Customer customer, List<BillItem> items) {
        this();
        this.billNumber = billNumber;
        this.customer = customer;
        this.customerId = customer != null ? customer.getCustomerId() : 0;
        this.items = items;
        this.billDate = new Date();
    }
    
    // Getters and Setters
    
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public List<ItemDTO> getItemDTOs() {
        return itemDTOs;
    }
    
    public void setItemDTOs(List<ItemDTO> itemDTOs) {
        this.itemDTOs = itemDTOs;
    }
    
    public double getDiscountPercentage() {
        return discountPercentage;
    }
    
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    
    public double getTaxPercentage() {
        return taxPercentage;
    }
    
    public void setTaxPercentage(double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }
    
    public double getTotalSavings() {
        return totalSavings;
    }
    
    public void setTotalSavings(double totalSavings) {
        this.totalSavings = totalSavings;
    }
    
    
    public String getBillNumber() {
        return billNumber;
    }
    
    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Date getBillDate() {
        return billDate;
    }
    
    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }
    
    public List<BillItem> getItems() {
        return items;
    }
    
    public void setItems(List<BillItem> items) {
        this.items = items;
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    public double getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    public double getTaxAmount() {
        return taxAmount;
    }
    
    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    /**
     * Calculates the totals based on items
     */
    public void calculateTotals() {
        if (items == null || items.isEmpty()) {
            this.subtotal = 0.0;
            this.totalAmount = 0.0;
            this.totalSavings = 0.0;
            return;
        }
        
        // Calculate subtotal
        this.subtotal = items.stream()
            .mapToDouble(BillItem::getTotalPrice)
            .sum();
        
        // Calculate discount amount
        this.discountAmount = (this.subtotal * this.discountPercentage) / 100;
        
        // Calculate tax amount
        double amountAfterDiscount = this.subtotal - this.discountAmount;
        this.taxAmount = (amountAfterDiscount * this.taxPercentage) / 100;
        
        // Calculate total amount
        this.totalAmount = amountAfterDiscount + this.taxAmount;
        
        // Calculate total savings (item level discounts + bill level discount)
        double itemLevelSavings = 0.0;
        if (items != null) {
            itemLevelSavings = items.stream()
                .mapToDouble(item -> item.getDiscountAmount())
                .sum();
        }
        this.totalSavings = itemLevelSavings + this.discountAmount;
    }
    
    /**
     * Calculates totals based on ItemDTOs (for AJAX operations)
     */
    public void calculateTotalsFromDTOs() {
        if (itemDTOs == null || itemDTOs.isEmpty()) {
            this.subtotal = 0.0;
            this.totalAmount = 0.0;
            this.totalSavings = 0.0;
            return;
        }
        
        // Calculate subtotal from DTOs
        this.subtotal = itemDTOs.stream()
            .mapToDouble(ItemDTO::getTotalPrice)
            .sum();
        
        // Calculate discount amount
        this.discountAmount = (this.subtotal * this.discountPercentage) / 100;
        
        // Calculate tax amount
        double amountAfterDiscount = this.subtotal - this.discountAmount;
        this.taxAmount = (amountAfterDiscount * this.taxPercentage) / 100;
        
        // Calculate total amount
        this.totalAmount = amountAfterDiscount + this.taxAmount;
        
        // Calculate total savings
        double itemLevelSavings = itemDTOs.stream()
            .mapToDouble(ItemDTO::getDiscountAmount)
            .sum();
        this.totalSavings = itemLevelSavings + this.discountAmount;
    }
    
    /**
     * Recalculates totals - decides which calculation method to use
     */
    public void recalculateTotals() {
        if (itemDTOs != null && !itemDTOs.isEmpty()) {
            calculateTotalsFromDTOs();
        } else {
            calculateTotals();
        }
    }
    /**
     * Gets the count of items in the bill
     * @return Number of items
     */
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
    
    /**
     * Gets the total quantity of all items
     * @return Total quantity
     */
    public int getTotalQuantity() {
        if (items == null) return 0;
        return items.stream()
            .mapToInt(BillItem::getQuantity)
            .sum();
    }
    
    @Override
    public String toString() {
        return "BillDTO{" +
                "billNumber='" + billNumber + '\'' +
                ", customer=" + customer +
                ", billDate=" + billDate +
                ", itemCount=" + getItemCount() +
                ", totalAmount=" + totalAmount +
                '}';
    }
}