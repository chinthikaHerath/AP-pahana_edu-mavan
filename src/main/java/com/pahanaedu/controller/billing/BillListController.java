package com.pahanaedu.controller.billing;

import com.pahanaedu.service.interfaces.BillingService;
import com.pahanaedu.service.impl.BillingServiceImpl;
import com.pahanaedu.model.Bill;
import com.pahanaedu.constant.PaymentStatus;
import com.pahanaedu.constant.PaymentMethod;
import com.pahanaedu.util.SessionUtil;
import com.pahanaedu.util.DateUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Servlet for displaying and managing bill list
 */
@WebServlet(name = "BillListController", urlPatterns = {"/bill/list", "/billing", "/bills"})
public class BillListController extends HttpServlet {
    
    private BillingService billingService;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        super.init();
        billingService = new BillingServiceImpl();
        gson = new Gson();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            // Get filter parameters
            String filterType = request.getParameter("filter");
            String searchTerm = request.getParameter("search");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String paymentStatus = request.getParameter("status");
            String paymentMethod = request.getParameter("method");
            String customerIdStr = request.getParameter("customerId");
            String exportFormat = request.getParameter("export");
            
            // Pagination parameters
            String pageStr = request.getParameter("page");
            String pageSizeStr = request.getParameter("pageSize");
            int page = pageStr != null ? Integer.parseInt(pageStr) : 1;
            int pageSize = pageSizeStr != null ? Integer.parseInt(pageSizeStr) : 20;
            
            List<Bill> bills;
            String pageTitle = "All Bills";
            
            // Determine which bills to display
            if ("today".equals(filterType)) {
                bills = billingService.getTodaysBills();
                pageTitle = "Today's Bills";
            } else if ("pending".equals(filterType)) {
                bills = billingService.getPendingBills();
                pageTitle = "Pending Bills";
            } else if ("overdue".equals(filterType)) {
                bills = billingService.getOverdueBills(30);
                pageTitle = "Overdue Bills";
            } else if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                bills = billingService.searchBills(searchTerm);
                pageTitle = "Search Results for: " + searchTerm;
            } else if (startDateStr != null && endDateStr != null) {
                Date startDate = DateUtil.parseDate(startDateStr);
                Date endDate = DateUtil.parseDate(endDateStr);
                bills = billingService.getBillsByDateRange(startDate, endDate);
                pageTitle = "Bills from " + startDateStr + " to " + endDateStr;
            } else if (paymentStatus != null && !paymentStatus.isEmpty()) {
                bills = filterByPaymentStatus(paymentStatus);
                pageTitle = PaymentStatus.fromString(paymentStatus).getDisplayName() + " Bills";
            } else if (paymentMethod != null && !paymentMethod.isEmpty()) {
                bills = filterByPaymentMethod(paymentMethod);
                pageTitle = PaymentMethod.fromString(paymentMethod).getDisplayName() + " Payment Bills";
            } else if (customerIdStr != null && !customerIdStr.isEmpty()) {
                int customerId = Integer.parseInt(customerIdStr);
                bills = billingService.getCustomerBills(customerId);
                pageTitle = "Customer Bills";
            } else {
                // Default: show recent bills with pagination
                bills = billingService.getBillsWithPagination(page, pageSize);
                pageTitle = "Recent Bills";
            }
            
            // Handle export if requested
            if ("csv".equals(exportFormat)) {
                exportBillsAsCSV(response, bills);
                return;
            } else if ("pdf".equals(exportFormat)) {
                // PDF export would be implemented here
                SessionUtil.setWarningMessage(session, "PDF export is not yet implemented");
            }
            
            System.out.println("===== BILL LIST DEBUG START =====");
            System.out.println("Total bills retrieved: " + bills.size());
            for (Bill bill : bills) {
                System.out.println("Bill: " + bill.getBillNumber() + 
                                 " | ItemCount: " + bill.getItemCount() + 
                                 " | BillItems List: " + (bill.getBillItems() != null ? bill.getBillItems().size() : "null"));
                
                // If item count is 0, try to get more info
                if (bill.getItemCount() == 0) {
                    System.out.println("  -> Item count is 0 for bill " + bill.getBillNumber());
                    // Try to load items directly
                    try {
                        Bill fullBill = billingService.getBillById(bill.getBillId());
                        System.out.println("  -> After loading full bill, item count: " + fullBill.getItemCount());
                        System.out.println("  -> Bill items list size: " + (fullBill.getBillItems() != null ? fullBill.getBillItems().size() : "null"));
                        
                        // Update the bill in the list
                        bill.setItemCount(fullBill.getItemCount());
                        if (fullBill.getBillItems() != null) {
                            bill.setBillItems(fullBill.getBillItems());
                        }
                    } catch (Exception e) {
                        System.out.println("  -> Error loading full bill: " + e.getMessage());
                    }
                }
            }
            System.out.println("===== BILL LIST DEBUG END =====");
            
            // Calculate statistics
            Map<String, Object> statistics = calculateStatistics(bills);
            
            // Get filter options
            PaymentStatus[] statusOptions = PaymentStatus.values();
            PaymentMethod[] methodOptions = PaymentMethod.values();
            
            // Set attributes
            request.setAttribute("bills", bills);
            request.setAttribute("pageTitle", pageTitle);
            request.setAttribute("statistics", statistics);
            request.setAttribute("statusOptions", statusOptions);
            request.setAttribute("methodOptions", methodOptions);
            request.setAttribute("currentPage", page);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("totalBills", bills.size());
            
            // Preserve filter parameters
            request.setAttribute("filterType", filterType);
            request.setAttribute("searchTerm", searchTerm);
            request.setAttribute("startDate", startDateStr);
            request.setAttribute("endDate", endDateStr);
            request.setAttribute("selectedStatus", paymentStatus);
            request.setAttribute("selectedMethod", paymentMethod);
            
            // Forward to bill list JSP
            request.getRequestDispatcher("/views/billing/bill-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            log("Error loading bill list: " + e.getMessage(), e);
            SessionUtil.setErrorMessage(session, "Error loading bills: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("ajax".equals(action)) {
            handleAjaxRequest(request, response);
        } else {
            // Handle bulk actions
            handleBulkAction(request, response);
        }
    }
    
    /**
     * Handle AJAX requests
     */
    private void handleAjaxRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();
        
        String ajaxAction = request.getParameter("ajaxAction");
        
        try {
            switch (ajaxAction) {
                case "updateStatus":
                    handleUpdateStatus(request, jsonResponse);
                    break;
                    
                case "quickView":
                    handleQuickView(request, jsonResponse);
                    break;
                    
                case "getStatistics":
                    handleGetStatistics(request, jsonResponse);
                    break;
                    
                default:
                    jsonResponse.addProperty("success", false);
                    jsonResponse.addProperty("error", "Unknown action: " + ajaxAction);
            }
            
        } catch (Exception e) {
            log("Error in AJAX request: " + e.getMessage(), e);
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", e.getMessage());
        }
        
        out.print(jsonResponse.toString());
        out.flush();
    }
    
    /**
     * Handle bulk actions (cancel multiple bills, etc.)
     */
    private void handleBulkAction(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String bulkAction = request.getParameter("bulkAction");
        String[] billIds = request.getParameterValues("billIds[]");
        
        if (billIds == null || billIds.length == 0) {
            SessionUtil.setWarningMessage(session, "No bills selected for bulk action");
            response.sendRedirect(request.getContextPath() + "/bill/list");
            return;
        }
        
        try {
            int successCount = 0;
            int failCount = 0;
            
            for (String billIdStr : billIds) {
                try {
                    int billId = Integer.parseInt(billIdStr);
                    
                    switch (bulkAction) {
                        case "cancel":
                            int userId = SessionUtil.getLoggedInUserId(session);
                            if (billingService.cancelBill(billId, "Bulk cancellation", userId)) {
                                successCount++;
                            } else {
                                failCount++;
                            }
                            break;
                            
                        case "export":
                            // Add to export list
                            break;
                            
                        default:
                            failCount++;
                    }
                    
                } catch (Exception e) {
                    failCount++;
                    log("Error processing bill " + billIdStr + ": " + e.getMessage(), e);
                }
            }
            
            // Set result message
            if (successCount > 0 && failCount == 0) {
                SessionUtil.setSuccessMessage(session, 
                    successCount + " bill(s) processed successfully");
            } else if (successCount > 0 && failCount > 0) {
                SessionUtil.setWarningMessage(session, 
                    successCount + " bill(s) processed successfully, " + failCount + " failed");
            } else {
                SessionUtil.setErrorMessage(session, 
                    "Failed to process selected bills");
            }
            
        } catch (Exception e) {
            log("Error in bulk action: " + e.getMessage(), e);
            SessionUtil.setErrorMessage(session, "Error processing bulk action: " + e.getMessage());
        }
        
        response.sendRedirect(request.getContextPath() + "/bill/list");
    }
    
    /**
     * Handle update status AJAX request
     */
    private void handleUpdateStatus(HttpServletRequest request, JsonObject response) 
            throws Exception {
        
        int billId = Integer.parseInt(request.getParameter("billId"));
        String newStatus = request.getParameter("newStatus");
        String notes = request.getParameter("notes");
        
        boolean success = billingService.updatePaymentStatus(billId, newStatus, notes);
        
        response.addProperty("success", success);
        if (success) {
            response.addProperty("message", "Payment status updated successfully");
            
            // Return updated bill info
            Bill updatedBill = billingService.getBillById(billId);
            response.addProperty("newStatus", updatedBill.getPaymentStatus());
            response.addProperty("statusClass", updatedBill.getPaymentStatusClass());
        } else {
            response.addProperty("error", "Failed to update payment status");
        }
    }
    
    /**
     * Handle quick view AJAX request
     */
    private void handleQuickView(HttpServletRequest request, JsonObject response) 
            throws Exception {
        
        int billId = Integer.parseInt(request.getParameter("billId"));
        Bill bill = billingService.getBillById(billId);
        
        if (bill != null) {
            response.addProperty("success", true);
            response.add("bill", gson.toJsonTree(bill));
            
            // Add formatted values for display
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            
            response.addProperty("formattedDate", dateFormat.format(bill.getBillDate()));
            response.addProperty("formattedTime", timeFormat.format(bill.getBillTime()));
            response.addProperty("itemCount", bill.getItemCount());
        } else {
            response.addProperty("success", false);
            response.addProperty("error", "Bill not found");
        }
    }
    
    /**
     * Handle get statistics AJAX request
     */
    private void handleGetStatistics(HttpServletRequest request, JsonObject response) 
            throws Exception {
        
        Map<String, Object> stats = billingService.getBillStatistics();
        
        response.addProperty("success", true);
        response.add("statistics", gson.toJsonTree(stats));
    }
    
    /**
     * Filter bills by payment status
     */
    private List<Bill> filterByPaymentStatus(String status) throws Exception {
        // This would ideally be a direct DAO method
        List<Bill> allBills = billingService.getAllBills();
        List<Bill> filtered = new ArrayList<>();
        
        for (Bill bill : allBills) {
            if (status.equals(bill.getPaymentStatus())) {
                filtered.add(bill);
            }
        }
        
        return filtered;
    }
    
    /**
     * Filter bills by payment method
     */
    private List<Bill> filterByPaymentMethod(String method) throws Exception {
        // This would ideally be a direct DAO method
        List<Bill> allBills = billingService.getAllBills();
        List<Bill> filtered = new ArrayList<>();
        
        for (Bill bill : allBills) {
            if (method.equals(bill.getPaymentMethod())) {
                filtered.add(bill);
            }
        }
        
        return filtered;
    }
    
    /**
     * Calculate statistics for displayed bills
     */
    private Map<String, Object> calculateStatistics(List<Bill> bills) {
        Map<String, Object> stats = new HashMap<>();
        
        double totalAmount = 0;
        double paidAmount = 0;
        double pendingAmount = 0;
        int paidCount = 0;
        int pendingCount = 0;
        int cancelledCount = 0;
        
        for (Bill bill : bills) {
            totalAmount += bill.getTotalAmount();
            
            switch (bill.getPaymentStatus()) {
                case "PAID":
                    paidAmount += bill.getTotalAmount();
                    paidCount++;
                    break;
                case "PENDING":
                case "PARTIAL":
                    pendingAmount += bill.getTotalAmount();
                    pendingCount++;
                    break;
                case "CANCELLED":
                    cancelledCount++;
                    break;
            }
        }
        
        stats.put("totalBills", bills.size());
        stats.put("totalAmount", totalAmount);
        stats.put("paidAmount", paidAmount);
        stats.put("pendingAmount", pendingAmount);
        stats.put("paidCount", paidCount);
        stats.put("pendingCount", pendingCount);
        stats.put("cancelledCount", cancelledCount);
        
        return stats;
    }
    
    /**
     * Export bills as CSV
     */
    private void exportBillsAsCSV(HttpServletResponse response, List<Bill> bills) 
            throws IOException {
        
        String csv = billingService.exportBillsToCSV(bills);
        
        // Set response headers
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", 
            "attachment; filename=\"bills_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv\"");
        
        // Write CSV content
        response.getWriter().write(csv);
    }
    
    @Override
    public String getServletInfo() {
        return "Bill List Controller - Displays and manages bill listings";
    }
}