package com.pahanaedu.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DTO for handling various report data
 */
public class ReportDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Common report fields
    private String reportTitle;
    private String reportType;
    private Date startDate;
    private Date endDate;
    private Date generatedDate;
    private String generatedBy;
    
    // Daily Sales Report fields
    private double totalSales;
    private int totalBills;
    private int totalCustomers;
    private double averageBillValue;
    private List<Map<String, Object>> salesByCategory;
    private List<Map<String, Object>> topSellingItems;
    private List<Map<String, Object>> hourlyBreakdown;
    
    // Stock Report fields
    private int totalItems;
    private int lowStockItems;
    private int outOfStockItems;
    private double totalStockValue;
    private List<Map<String, Object>> stockByCategory;
    private List<Map<String, Object>> lowStockList;
    
    // Customer Report fields
    private int newCustomers;
    private int activeCustomers;
    private int inactiveCustomers;
    private List<Map<String, Object>> topCustomers;
    private List<Map<String, Object>> customersByCity;
    
    // Chart data for visualization
    private List<String> chartLabels;
    private List<Double> chartData;
    private Map<String, List<Double>> multiSeriesData;
    
    // Constructors
    public ReportDTO() {
        this.generatedDate = new Date();
    }
    
    public ReportDTO(String reportTitle, String reportType) {
        this();
        this.reportTitle = reportTitle;
        this.reportType = reportType;
    }
    
    // Getters and Setters
    public String getReportTitle() {
        return reportTitle;
    }
    
    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }
    
    public String getReportType() {
        return reportType;
    }
    
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public Date getGeneratedDate() {
        return generatedDate;
    }
    
    public void setGeneratedDate(Date generatedDate) {
        this.generatedDate = generatedDate;
    }
    
    public String getGeneratedBy() {
        return generatedBy;
    }
    
    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }
    
    public double getTotalSales() {
        return totalSales;
    }
    
    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }
    
    public int getTotalBills() {
        return totalBills;
    }
    
    public void setTotalBills(int totalBills) {
        this.totalBills = totalBills;
    }
    
    public int getTotalCustomers() {
        return totalCustomers;
    }
    
    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }
    
    public double getAverageBillValue() {
        return averageBillValue;
    }
    
    public void setAverageBillValue(double averageBillValue) {
        this.averageBillValue = averageBillValue;
    }
    
    public List<Map<String, Object>> getSalesByCategory() {
        return salesByCategory;
    }
    
    public void setSalesByCategory(List<Map<String, Object>> salesByCategory) {
        this.salesByCategory = salesByCategory;
    }
    
    public List<Map<String, Object>> getTopSellingItems() {
        return topSellingItems;
    }
    
    public void setTopSellingItems(List<Map<String, Object>> topSellingItems) {
        this.topSellingItems = topSellingItems;
    }
    
    public List<Map<String, Object>> getHourlyBreakdown() {
        return hourlyBreakdown;
    }
    
    public void setHourlyBreakdown(List<Map<String, Object>> hourlyBreakdown) {
        this.hourlyBreakdown = hourlyBreakdown;
    }
    
    public int getTotalItems() {
        return totalItems;
    }
    
    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
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
    
    public double getTotalStockValue() {
        return totalStockValue;
    }
    
    public void setTotalStockValue(double totalStockValue) {
        this.totalStockValue = totalStockValue;
    }
    
    public List<Map<String, Object>> getStockByCategory() {
        return stockByCategory;
    }
    
    public void setStockByCategory(List<Map<String, Object>> stockByCategory) {
        this.stockByCategory = stockByCategory;
    }
    
    public List<Map<String, Object>> getLowStockList() {
        return lowStockList;
    }
    
    public void setLowStockList(List<Map<String, Object>> lowStockList) {
        this.lowStockList = lowStockList;
    }
    
    public int getNewCustomers() {
        return newCustomers;
    }
    
    public void setNewCustomers(int newCustomers) {
        this.newCustomers = newCustomers;
    }
    
    public int getActiveCustomers() {
        return activeCustomers;
    }
    
    public void setActiveCustomers(int activeCustomers) {
        this.activeCustomers = activeCustomers;
    }
    
    public int getInactiveCustomers() {
        return inactiveCustomers;
    }
    
    public void setInactiveCustomers(int inactiveCustomers) {
        this.inactiveCustomers = inactiveCustomers;
    }
    
    public List<Map<String, Object>> getTopCustomers() {
        return topCustomers;
    }
    
    public void setTopCustomers(List<Map<String, Object>> topCustomers) {
        this.topCustomers = topCustomers;
    }
    
    public List<Map<String, Object>> getCustomersByCity() {
        return customersByCity;
    }
    
    public void setCustomersByCity(List<Map<String, Object>> customersByCity) {
        this.customersByCity = customersByCity;
    }
    
    public List<String> getChartLabels() {
        return chartLabels;
    }
    
    public void setChartLabels(List<String> chartLabels) {
        this.chartLabels = chartLabels;
    }
    
    public List<Double> getChartData() {
        return chartData;
    }
    
    public void setChartData(List<Double> chartData) {
        this.chartData = chartData;
    }
    
    public Map<String, List<Double>> getMultiSeriesData() {
        return multiSeriesData;
    }
    
    public void setMultiSeriesData(Map<String, List<Double>> multiSeriesData) {
        this.multiSeriesData = multiSeriesData;
    }
    
    // Utility methods
    public boolean hasData() {
        return (totalBills > 0 || totalItems > 0 || totalCustomers > 0);
    }
    
    public String getDateRangeText() {
        if (startDate != null && endDate != null) {
            return String.format("%tF to %tF", startDate, endDate);
        }
        return "All Time";
    }
}