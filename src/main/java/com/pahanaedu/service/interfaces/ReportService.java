package com.pahanaedu.service.interfaces;

import com.pahanaedu.dto.ReportDTO;
import com.pahanaedu.dto.DashboardDTO;
import com.pahanaedu.exception.DatabaseException;
import com.pahanaedu.exception.BusinessException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Interface for Report Service
 * Handles business logic for generating various reports
 */
public interface ReportService {
    
    /**
     * Generate sales report for any date range (daily, monthly, or custom)
     * @param startDate Start date
     * @param endDate End date
     * @return ReportDTO with sales data
     * @throws DatabaseException if database operation fails
     * @throws BusinessException if date range is invalid
     */
    ReportDTO generateSalesReport(Date startDate, Date endDate) 
            throws DatabaseException, BusinessException;
    
    /**
     * Generate stock report
     * @return ReportDTO with current stock data
     * @throws DatabaseException if database operation fails
     */
    ReportDTO generateStockReport() throws DatabaseException;
    
    /**
     * Generate low stock report
     * @param threshold Stock level threshold
     * @return ReportDTO with low stock items
     * @throws DatabaseException if database operation fails
     */
    ReportDTO generateLowStockReport(int threshold) throws DatabaseException;
    
    /**
     * Generate customer report
     * @param startDate Start date (null for all time)
     * @param endDate End date (null for all time)
     * @return ReportDTO with customer data
     * @throws DatabaseException if database operation fails
     */
    ReportDTO generateCustomerReport(Date startDate, Date endDate) 
            throws DatabaseException;
    
    /**
     * Generate dashboard statistics
     * @return DashboardDTO with current statistics
     * @throws DatabaseException if database operation fails
     */
    DashboardDTO generateDashboardStatistics() throws DatabaseException;
    
    /**
     * Get sales breakdown by payment method
     * @param startDate Start date
     * @param endDate End date
     * @return Map with payment method breakdown
     * @throws DatabaseException if database operation fails
     */
    Map<String, Double> getSalesBreakdownByPaymentMethod(Date startDate, Date endDate) 
            throws DatabaseException;
    
    /**
     * Get daily sales breakdown for a date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of daily sales data
     * @throws DatabaseException if database operation fails
     */
    List<Map<String, Object>> getDailySalesBreakdown(Date startDate, Date endDate) 
            throws DatabaseException;
    
    /**
     * Export report to CSV
     * @param report ReportDTO to export
     * @return CSV string
     */
    String exportReportToCSV(ReportDTO report);
    
    /**
     * Validate date range for reports
     * @param startDate Start date
     * @param endDate End date
     * @throws BusinessException if validation fails
     */
    void validateDateRange(Date startDate, Date endDate) throws BusinessException;
}