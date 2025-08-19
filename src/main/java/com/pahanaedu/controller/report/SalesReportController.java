package com.pahanaedu.controller.report;

import com.pahanaedu.service.interfaces.ReportService;
import com.pahanaedu.service.impl.ReportServiceImpl;
import com.pahanaedu.service.interfaces.BillingService;
import com.pahanaedu.service.impl.BillingServiceImpl;
import com.pahanaedu.dao.interfaces.BillDAO;
import com.pahanaedu.dao.impl.BillDAOImpl;
import com.pahanaedu.dto.ReportDTO;
import com.pahanaedu.model.Bill;
import com.pahanaedu.constant.SystemConstants;
import com.pahanaedu.util.SessionUtil;
import com.pahanaedu.util.DateUtil;
import com.pahanaedu.exception.BusinessException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Servlet for generating sales reports (daily, monthly, or custom range)
 */
@WebServlet(name = "SalesReportController", urlPatterns = {"/report/sales", "/report/sales-report"})
public class SalesReportController extends HttpServlet {
    
    private ReportService reportService;
    private BillDAO billDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        reportService = new ReportServiceImpl();
        billDAO = new BillDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            // Get report type parameter
            String reportType = request.getParameter("type");
            Date startDate = null;
            Date endDate = null;
            
            // Handle different report types
            if ("today".equalsIgnoreCase(reportType)) {
                // Today's report
                startDate = new Date();
                endDate = new Date();
            } else if ("month".equalsIgnoreCase(reportType)) {
                // Current month report
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, 1);
                startDate = cal.getTime();
                
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                endDate = cal.getTime();
            } else {
                // Custom date range
                String startDateStr = request.getParameter("startDate");
                String endDateStr = request.getParameter("endDate");
                
                if (startDateStr != null && !startDateStr.trim().isEmpty()) {
                    try {
                        startDate = DateUtil.parseDate(startDateStr);
                    } catch (ParseException e) {
                        SessionUtil.setWarningMessage(session, "Invalid start date format");
                        // Default to today
                        startDate = new Date();
                    }
                }
                
                if (endDateStr != null && !endDateStr.trim().isEmpty()) {
                    try {
                        endDate = DateUtil.parseDate(endDateStr);
                    } catch (ParseException e) {
                        SessionUtil.setWarningMessage(session, "Invalid end date format");
                        // Default to today
                        endDate = new Date();
                    }
                }
                
                // If no dates provided, default to today
                if (startDate == null && endDate == null) {
                    startDate = new Date();
                    endDate = new Date();
                }
            }
            
            // Generate report
            ReportDTO report = reportService.generateSalesReport(startDate, endDate);
            
            // Get actual bills for the table
            List<Bill> bills = billDAO.getBillsByDateRange(
                DateUtil.getStartOfDay(startDate), 
                DateUtil.getEndOfDay(endDate)
            );
            
            // Load bill items count for each bill
            for (Bill bill : bills) {
                int itemCount = billDAO.getBillWithItems(bill.getBillId()).getBillItems().size();
                bill.setItemCount(itemCount);
            }
            
            // Add username to report
            report.setGeneratedBy(SessionUtil.getLoggedInUsername(session));
            report.setGeneratedDate(new Date());
            
            // Set attributes
            request.setAttribute("report", report);
            request.setAttribute("bills", bills);
            request.setAttribute("startDate", startDate);
            request.setAttribute("endDate", endDate);
            request.setAttribute("reportType", reportType);
            
            // Check if export is requested
            String export = request.getParameter("export");
            if ("csv".equalsIgnoreCase(export)) {
                exportToCSV(response, report, bills);
                return;
            }
            
            // Forward to JSP
            request.getRequestDispatcher("/views/reports/sales-report.jsp").forward(request, response);
            
        } catch (BusinessException e) {
            SessionUtil.setErrorMessage(session, e.getMessage());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } catch (Exception e) {
            log("Error generating sales report: " + e.getMessage(), e);
            SessionUtil.setErrorMessage(session, "Error generating report: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Handle date range selection via POST
        doGet(request, response);
    }
    
    /**
     * Export report to CSV with bill details
     */
    private void exportToCSV(HttpServletResponse response, ReportDTO report, List<Bill> bills) 
            throws IOException {
        
        StringBuilder csv = new StringBuilder();
        
        // Report header
        csv.append(report.getReportTitle()).append("\n");
        csv.append("Generated: ").append(DateUtil.formatDateTime(new Date())).append("\n");
        csv.append("Generated By: ").append(report.getGeneratedBy()).append("\n");
        
        if (report.getStartDate() != null && report.getEndDate() != null) {
            csv.append("Period: ").append(DateUtil.formatDate(report.getStartDate()))
               .append(" to ").append(DateUtil.formatDate(report.getEndDate())).append("\n");
        }
        csv.append("\n");
        
        // Summary section
        csv.append("SUMMARY\n");
        csv.append("Total Sales,LKR ").append(String.format("%.2f", report.getTotalSales())).append("\n");
        csv.append("Total Bills,").append(report.getTotalBills()).append("\n");
        csv.append("Customers Served,").append(report.getTotalCustomers()).append("\n");
        csv.append("Average Bill Value,LKR ").append(String.format("%.2f", report.getAverageBillValue())).append("\n");
        csv.append("\n");
        
        // Payment method breakdown
        if (report.getSalesByCategory() != null && !report.getSalesByCategory().isEmpty()) {
            csv.append("PAYMENT METHOD BREAKDOWN\n");
            csv.append("Method,Amount\n");
            for (Map<String, Object> payment : report.getSalesByCategory()) {
                String method = (String) payment.get("method");
                Double amount = (Double) payment.get("amount");
                csv.append(method.replace("_", " ")).append(",LKR ").append(String.format("%.2f", amount)).append("\n");
            }
            csv.append("\n");
        }
        
        // Daily breakdown for monthly reports
        if (report.getTopSellingItems() != null && !report.getTopSellingItems().isEmpty()) {
            csv.append("DAILY BREAKDOWN\n");
            csv.append("Date,Number of Bills,Total Sales\n");
            for (Map<String, Object> daily : report.getTopSellingItems()) {
                csv.append(daily.get("fullDate")).append(",");
                csv.append(daily.get("bills")).append(",");
                csv.append("LKR ").append(String.format("%.2f", daily.get("sales"))).append("\n");
            }
            csv.append("\n");
        }
        
        // Detailed bills
        csv.append("BILL DETAILS\n");
        csv.append("Bill No,Date,Time,Customer,Items,Subtotal,Discount,Total,Payment Method,Status\n");
        
        for (Bill bill : bills) {
            csv.append(bill.getBillNumber()).append(",");
            csv.append(DateUtil.formatDate(bill.getBillDate())).append(",");
            csv.append(DateUtil.formatTimeForDisplay(bill.getBillTime())).append(",");
            csv.append(escapeCSV(bill.getCustomerName())).append(",");
            csv.append(bill.getItemCount()).append(",");
            csv.append(String.format("%.2f", bill.getSubtotal())).append(",");
            csv.append(String.format("%.2f", bill.getDiscountAmount())).append(",");
            csv.append(String.format("%.2f", bill.getTotalAmount())).append(",");
            csv.append(bill.getPaymentMethod().replace("_", " ")).append(",");
            csv.append(bill.getPaymentStatus()).append("\n");
        }
        
        // Set response headers
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", 
            "attachment; filename=\"sales-report-" + 
            DateUtil.formatDate(report.getStartDate()) + "-to-" +
            DateUtil.formatDate(report.getEndDate()) + ".csv\"");
        
        // Write CSV
        response.getWriter().write(csv.toString());
    }
    
    /**
     * Escape CSV field if it contains special characters
     */
    private String escapeCSV(String field) {
        if (field == null) return "";
        
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
    
    @Override
    public String getServletInfo() {
        return "Sales Report Controller - Generates unified sales reports";
    }
}