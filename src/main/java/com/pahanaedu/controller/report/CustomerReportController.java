package com.pahanaedu.controller.report;

import com.pahanaedu.service.interfaces.ReportService;
import com.pahanaedu.service.impl.ReportServiceImpl;
import com.pahanaedu.dto.ReportDTO;
import com.pahanaedu.constant.SystemConstants;
import com.pahanaedu.util.SessionUtil;
import com.pahanaedu.util.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * Servlet for generating customer reports
 */
@WebServlet(name = "CustomerReportController", urlPatterns = {"/report/customer", "/report/customers"})
public class CustomerReportController extends HttpServlet {
    
    private ReportService reportService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        reportService = new ReportServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            // Get date range parameters
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            
            Date startDate = null;
            Date endDate = null;
            
            // Parse dates if provided
            if (startDateStr != null && !startDateStr.trim().isEmpty()) {
                try {
                    startDate = DateUtil.parseDate(startDateStr);
                } catch (ParseException e) {
                    SessionUtil.setWarningMessage(session, "Invalid start date format");
                }
            }
            
            if (endDateStr != null && !endDateStr.trim().isEmpty()) {
                try {
                    endDate = DateUtil.parseDate(endDateStr);
                } catch (ParseException e) {
                    SessionUtil.setWarningMessage(session, "Invalid end date format");
                }
            }
            
            // Generate report
            ReportDTO report = reportService.generateCustomerReport(startDate, endDate);
            
            // Add username to report
            report.setGeneratedBy(SessionUtil.getLoggedInUsername(session));
            
            // Set attributes
            request.setAttribute("report", report);
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            
            // Check if export is requested
            String export = request.getParameter("export");
            if ("csv".equalsIgnoreCase(export)) {
                exportToCSV(response, report);
                return;
            }
            
            // Forward to JSP
            request.getRequestDispatcher("/views/reports/customer-report.jsp").forward(request, response);
            
        } catch (Exception e) {
            log("Error generating customer report: " + e.getMessage(), e);
            SessionUtil.setErrorMessage(session, "Error generating report: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Handle date range selection
        doGet(request, response);
    }
    
    /**
     * Export report to CSV
     */
    private void exportToCSV(HttpServletResponse response, ReportDTO report) 
            throws IOException {
        
        StringBuilder csv = new StringBuilder();
        
        // Header
        csv.append("Customer Report\n");
        csv.append("Generated: ").append(new Date()).append("\n");
        if (report.getStartDate() != null && report.getEndDate() != null) {
            csv.append("Period: ").append(report.getDateRangeText()).append("\n");
        }
        csv.append("\n");
        
        // Summary
        csv.append("Summary\n");
        csv.append("Total Customers,").append(report.getTotalCustomers()).append("\n");
        csv.append("Active Customers,").append(report.getActiveCustomers()).append("\n");
        csv.append("Inactive Customers,").append(report.getInactiveCustomers()).append("\n\n");
        
        // Customers by city
        if (report.getCustomersByCity() != null && !report.getCustomersByCity().isEmpty()) {
            csv.append("Customers by City\n");
            csv.append("City,Customer Count,Percentage\n");
            
            for (java.util.Map<String, Object> city : report.getCustomersByCity()) {
                csv.append(city.get("city")).append(",");
                csv.append(city.get("customerCount")).append(",");
                csv.append(String.format("%.2f%%", city.get("percentage"))).append("\n");
            }
            csv.append("\n");
        }
        
        // Top customers
        if (report.getTopCustomers() != null && !report.getTopCustomers().isEmpty()) {
            csv.append("Top Customers\n");
            csv.append("Account Number,Customer Name,Total Purchases,Bill Count\n");
            
            for (java.util.Map<String, Object> customer : report.getTopCustomers()) {
                csv.append(customer.get("accountNumber")).append(",");
                csv.append(customer.get("customerName")).append(",");
                csv.append(customer.get("totalPurchases")).append(",");
                csv.append(customer.get("billCount")).append("\n");
            }
        }
        
        // Set response headers
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", 
            "attachment; filename=\"customer-report-" + 
            new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".csv\"");
        
        // Write CSV
        response.getWriter().write(csv.toString());
    }
    
    @Override
    public String getServletInfo() {
        return "Customer Report Controller - Generates customer analysis reports";
    }
}