package com.pahanaedu.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Customer model class representing the customers table
 */
public class Customer implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int customerId;
    private String accountNumber;
    private String customerName;
    private String address;
    private String city;
    private String postalCode;
    private String telephone;
    private String mobile;
    private String email;
    private String nicNumber;
    private Date registrationDate;
    private boolean isActive;
    private int createdBy;
    private Date createdAt;
    private Date updatedAt;
    
    // Transient fields for display purposes
    private transient String createdByUsername;
    private transient double totalPurchases;
    private transient int purchaseCount;
    
    // Default constructor
    public Customer() {
        this.isActive = true;
        this.registrationDate = new Date();
}
    
    // Constructor with required fields
    public Customer(String accountNumber, String customerName, String address, 
                   String telephone, String nicNumber) {
        this();
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.address = address;
        this.telephone = telephone;
        this.nicNumber = nicNumber;
    }
    
    // Full constructor
    public Customer(int customerId, String accountNumber, String customerName,
                   String address, String city, String postalCode, String telephone,
                   String mobile, String email, String nicNumber, Date registrationDate,
                   boolean isActive, int createdBy, Date createdAt, Date updatedAt) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.telephone = telephone;
        this.mobile = mobile;
        this.email = email;
        this.nicNumber = nicNumber;
        this.registrationDate = registrationDate;
        this.isActive = isActive;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public String getMobile() {
        return mobile;
    }
    
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getNicNumber() {
        return nicNumber;
    }
    
    public void setNicNumber(String nicNumber) {
        this.nicNumber = nicNumber;
    }
    
    public Date getRegistrationDate() {
        return registrationDate;
    }
    
    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public int getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Transient field getters and setters
    public String getCreatedByUsername() {
        return createdByUsername;
    }
    
    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }
    
    public double getTotalPurchases() {
        return totalPurchases;
    }
    
    public void setTotalPurchases(double totalPurchases) {
        this.totalPurchases = totalPurchases;
    }
    
    public int getPurchaseCount() {
        return purchaseCount;
    }
    
    public void setPurchaseCount(int purchaseCount) {
        this.purchaseCount = purchaseCount;
    }
    
    // Utility methods
    public String getFullAddress() {
        StringBuilder fullAddress = new StringBuilder();
        if (address != null && !address.trim().isEmpty()) {
            fullAddress.append(address);
        }
        if (city != null && !city.trim().isEmpty()) {
            if (fullAddress.length() > 0) fullAddress.append(", ");
            fullAddress.append(city);
        }
        if (postalCode != null && !postalCode.trim().isEmpty()) {
            if (fullAddress.length() > 0) fullAddress.append(" - ");
            fullAddress.append(postalCode);
        }
        return fullAddress.toString();
    }
    
    public String getContactNumbers() {
        StringBuilder contacts = new StringBuilder();
        if (telephone != null && !telephone.trim().isEmpty()) {
            contacts.append("Tel: ").append(telephone);
        }
        if (mobile != null && !mobile.trim().isEmpty()) {
            if (contacts.length() > 0) contacts.append(", ");
            contacts.append("Mobile: ").append(mobile);
        }
        return contacts.toString();
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", accountNumber='" + accountNumber + '\'' +
                ", customerName='" + customerName + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", nicNumber='" + nicNumber + '\'' +
                ", isActive=" + isActive +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Customer customer = (Customer) o;
        
        if (customerId != customer.customerId) return false;
        return accountNumber != null ? accountNumber.equals(customer.accountNumber) : customer.accountNumber == null;
    }
    
    @Override
    public int hashCode() {
        int result = customerId;
        result = 31 * result + (accountNumber != null ? accountNumber.hashCode() : 0);
        return result;
    }
    
}