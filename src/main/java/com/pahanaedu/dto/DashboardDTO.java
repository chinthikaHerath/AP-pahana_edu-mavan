package com.pahanaedu.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Simplified DTO for dashboard statistics
 * Focuses only on essential summary data
 */
public class DashboardDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // User statistics
    private int totalUsers;
    private int activeUsers;
    
    // Customer statistics
    private int totalCustomers;
    private int activeCustomers;
    private int newCustomersToday;
    
    // Item/Inventory statistics
    private int totalItems;
    private int activeItems;
    private int lowStockItems;
    private int outOfStockItems;
    
    // Sales statistics
    private int todaysBills;
    private double todaysSales;
    private int monthlyBills;
    private double monthlySales;
    private double averageBillValue;
    
    // Legacy fields - kept for backward compatibility but set to empty
    // These can be fully removed once you're sure no JSP references them
    private List<Map<String, Object>> recentBills;
    private List<Map<String, Object>> recentCustomers;
    private List<String> salesChartLabels;
    private List<Double> salesChartData;
    private List<Map<String, Object>> lowStockAlerts;
    
    // Metadata
    private Date lastUpdated;
    
    // Constructor
    public DashboardDTO() {
        this.lastUpdated = new Date();
        // Initialize lists to empty to avoid null pointer exceptions
        this.recentBills = new ArrayList<>();
        this.recentCustomers = new ArrayList<>();
        this.salesChartLabels = new ArrayList<>();
        this.salesChartData = new ArrayList<>();
        this.lowStockAlerts = new ArrayList<>();
    }
    
    // Essential Getters and Setters
    public int getTotalUsers() {
        return totalUsers;
    }
    
    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }
    
    public int getActiveUsers() {
        return activeUsers;
    }
    
    public void setActiveUsers(int activeUsers) {
        this.activeUsers = activeUsers;
    }
    
    public int getTotalCustomers() {
        return totalCustomers;
    }
    
    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }
    
    public int getActiveCustomers() {
        return activeCustomers;
    }
    
    public void setActiveCustomers(int activeCustomers) {
        this.activeCustomers = activeCustomers;
    }
    
    public int getNewCustomersToday() {
        return newCustomersToday;
    }
    
    public void setNewCustomersToday(int newCustomersToday) {
        this.newCustomersToday = newCustomersToday;
    }
    
    public int getTotalItems() {
        return totalItems;
    }
    
    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
    
    public int getActiveItems() {
        return activeItems;
    }
    
    public void setActiveItems(int activeItems) {
        this.activeItems = activeItems;
    }
    
    public int getLowStockItems() {
        return lowStockItems;
    }
    
    public void setLowStockItems(int lowStockItems) {
        this.lowStockItems = lowStockItems;
    }
    
    public int getOutOfStockItems() {
        return outOfStockItems;
    }
    
    public void setOutOfStockItems(int outOfStockItems) {
        this.outOfStockItems = outOfStockItems;
    }
    
    public int getTodaysBills() {
        return todaysBills;
    }
    
    public void setTodaysBills(int todaysBills) {
        this.todaysBills = todaysBills;
    }
    
    public double getTodaysSales() {
        return todaysSales;
    }
    
    public void setTodaysSales(double todaysSales) {
        this.todaysSales = todaysSales;
    }
    
    public int getMonthlyBills() {
        return monthlyBills;
    }
    
    public void setMonthlyBills(int monthlyBills) {
        this.monthlyBills = monthlyBills;
    }
    
    public double getMonthlySales() {
        return monthlySales;
    }
    
    public void setMonthlySales(double monthlySales) {
        this.monthlySales = monthlySales;
    }
    
    public double getAverageBillValue() {
        return averageBillValue;
    }
    
    public void setAverageBillValue(double averageBillValue) {
        this.averageBillValue = averageBillValue;
    }
    
    public Date getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    // Legacy getters/setters - Keep for backward compatibility
    // Remove these once you're sure no JSP pages reference them
    public List<Map<String, Object>> getRecentBills() {
        return recentBills != null ? recentBills : new ArrayList<>();
    }
    
    public void setRecentBills(List<Map<String, Object>> recentBills) {
        this.recentBills = recentBills;
    }
    
    public List<Map<String, Object>> getRecentCustomers() {
        return recentCustomers != null ? recentCustomers : new ArrayList<>();
    }
    
    public void setRecentCustomers(List<Map<String, Object>> recentCustomers) {
        this.recentCustomers = recentCustomers;
    }
    
    public List<String> getSalesChartLabels() {
        return salesChartLabels != null ? salesChartLabels : new ArrayList<>();
    }
    
    public void setSalesChartLabels(List<String> salesChartLabels) {
        this.salesChartLabels = salesChartLabels;
    }
    
    public List<Double> getSalesChartData() {
        return salesChartData != null ? salesChartData : new ArrayList<>();
    }
    
    public void setSalesChartData(List<Double> salesChartData) {
        this.salesChartData = salesChartData;
    }
    
    public List<Map<String, Object>> getLowStockAlerts() {
        return lowStockAlerts != null ? lowStockAlerts : new ArrayList<>();
    }
    
    public void setLowStockAlerts(List<Map<String, Object>> lowStockAlerts) {
        this.lowStockAlerts = lowStockAlerts;
    }
    
    // Utility methods
    public boolean hasLowStockAlerts() {
        return lowStockItems > 0 || outOfStockItems > 0;
    }
    
    public boolean hasCriticalAlerts() {
        return outOfStockItems > 0;
    }
    
    public String getStockStatus() {
        if (outOfStockItems > 0) {
            return "Critical";
        } else if (lowStockItems > 0) {
            return "Warning";
        }
        return "Good";
    }
    
    public String getStockStatusClass() {
        if (outOfStockItems > 0) {
            return "danger";
        } else if (lowStockItems > 0) {
            return "warning";
        }
        return "success";
    }
}