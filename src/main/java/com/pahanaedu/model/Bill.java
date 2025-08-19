package com.pahanaedu.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Bill model class representing the bills table
 */
public class Bill implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int billId;
    private String billNumber;
    private int customerId;
    private int userId;
    private Date billDate;
    private Date billTime;
    private double subtotal;
    private double discountPercentage;
    private double discountAmount;
    private double taxPercentage;
    private double taxAmount;
    private double totalAmount;
    private String paymentMethod;
    private String paymentStatus;
    private String notes;
    private Date createdAt;
    
    // Transient fields for display purposes
    private transient Customer customer;
    private transient String customerName;
    private transient String customerAccountNumber;
    private transient String userName;
    private transient List<BillItem> billItems;
    private transient int itemCount;
    
    // Default constructor
    public Bill() {
        this.billDate = new Date();
        this.billTime = new Date();
        this.paymentMethod = "CASH";
        this.paymentStatus = "PAID";
        this.discountPercentage = 0.0;
        this.discountAmount = 0.0;
        this.taxPercentage = 0.0;
        this.taxAmount = 0.0;
        this.billItems = new ArrayList<>();
    }
    
    // Constructor with required fields
    public Bill(String billNumber, int customerId, int userId, double totalAmount) {
        this();
        this.billNumber = billNumber;
        this.customerId = customerId;
        this.userId = userId;
        this.totalAmount = totalAmount;
    }
    
    // Full constructor
    public Bill(int billId, String billNumber, int customerId, int userId,
                Date billDate, Date billTime, double subtotal, double discountPercentage,
                double discountAmount, double taxPercentage, double taxAmount,
                double totalAmount, String paymentMethod, String paymentStatus,
                String notes, Date createdAt) {
        this.billId = billId;
        this.billNumber = billNumber;
        this.customerId = customerId;
        this.userId = userId;
        this.billDate = billDate;
        this.billTime = billTime;
        this.subtotal = subtotal;
        this.discountPercentage = discountPercentage;
        this.discountAmount = discountAmount;
        this.taxPercentage = taxPercentage;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.notes = notes;
        this.createdAt = createdAt;
        this.billItems = new ArrayList<>();
    }
    
    // Getters and Setters
    public int getBillId() {
        return billId;
    }
    
    public void setBillId(int billId) {
        this.billId = billId;
    }
    
    public String getBillNumber() {
        return billNumber;
    }
    
    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public Date getBillDate() {
        return billDate;
    }
    
    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }
    
    public Date getBillTime() {
        return billTime;
    }
    
    public void setBillTime(Date billTime) {
        this.billTime = billTime;
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    public double getDiscountPercentage() {
        return discountPercentage;
    }
    
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    
    public double getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    public double getTaxPercentage() {
        return taxPercentage;
    }
    
    public void setTaxPercentage(double taxPercentage) {
        this.taxPercentage = taxPercentage;
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
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    // Transient field getters and setters
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (customer != null) {
            this.customerName = customer.getCustomerName();
            this.customerAccountNumber = customer.getAccountNumber();
        }
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerAccountNumber() {
        return customerAccountNumber;
    }
    
    public void setCustomerAccountNumber(String customerAccountNumber) {
        this.customerAccountNumber = customerAccountNumber;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public List<BillItem> getBillItems() {
        if (billItems == null) {
            billItems = new ArrayList<>();
        }
        return billItems;
    }
    
    public void setBillItems(List<BillItem> billItems) {
        this.billItems = billItems;
    }
    
    public int getItemCount() {
        if (billItems != null) {
            return billItems.size();
        }
        return itemCount;
    }
    
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
    
    // Utility methods
    public void addBillItem(BillItem item) {
        if (billItems == null) {
            billItems = new ArrayList<>();
        }
        billItems.add(item);
        recalculateTotals();
    }
    
    public void removeBillItem(BillItem item) {
        if (billItems != null) {
            billItems.remove(item);
            recalculateTotals();
        }
    }
    
    public void recalculateTotals() {
        if (billItems == null || billItems.isEmpty()) {
            this.subtotal = 0.0;
            this.totalAmount = 0.0;
            return;
        }
        
        // Calculate subtotal
        this.subtotal = billItems.stream()
            .mapToDouble(BillItem::getTotalPrice)
            .sum();
        
        // Calculate discount amount if percentage is set
        if (this.discountPercentage > 0) {
            this.discountAmount = this.subtotal * (this.discountPercentage / 100);
        }
        
        // Calculate tax amount if percentage is set
        double afterDiscount = this.subtotal - this.discountAmount;
        if (this.taxPercentage > 0) {
            this.taxAmount = afterDiscount * (this.taxPercentage / 100);
        }
        
        // Calculate total
        this.totalAmount = afterDiscount + this.taxAmount;
    }
    
    public String getPaymentStatusClass() {
        switch (paymentStatus) {
            case "PAID":
                return "success";
            case "PENDING":
                return "warning";
            case "PARTIAL":
                return "info";
            case "CANCELLED":
                return "danger";
            default:
                return "secondary";
        }
    }
    
    public String getPaymentMethodIcon() {
        switch (paymentMethod) {
            case "CASH":
                return "fa-money-bill";
            case "CARD":
                return "fa-credit-card";
            case "CHEQUE":
                return "fa-file-invoice";
            case "BANK_TRANSFER":
                return "fa-university";
            default:
                return "fa-question";
        }
    }
    
    public boolean isPaid() {
        return "PAID".equals(paymentStatus);
    }
    
    public boolean isPending() {
        return "PENDING".equals(paymentStatus);
    }
    
    public boolean isCancelled() {
        return "CANCELLED".equals(paymentStatus);
    }
    
    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", billNumber='" + billNumber + '\'' +
                ", customerId=" + customerId +
                ", totalAmount=" + totalAmount +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", billDate=" + billDate +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Bill bill = (Bill) o;
        
        if (billId != bill.billId) return false;
        return billNumber != null ? billNumber.equals(bill.billNumber) : bill.billNumber == null;
    }
    
    @Override
    public int hashCode() {
        int result = billId;
        result = 31 * result + (billNumber != null ? billNumber.hashCode() : 0);
        return result;
    }
}