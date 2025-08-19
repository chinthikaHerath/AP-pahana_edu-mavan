package com.pahanaedu.controller.billing;

import com.pahanaedu.service.interfaces.BillingService;
import com.pahanaedu.service.impl.BillingServiceImpl;
import com.pahanaedu.model.Bill;
import com.pahanaedu.model.User;
import com.pahanaedu.constant.PaymentStatus;
import com.pahanaedu.constant.PaymentMethod;
import com.pahanaedu.constant.SystemConstants;
import com.pahanaedu.exception.DatabaseException;
import com.pahanaedu.util.SessionUtil;
import com.pahanaedu.util.ValidationUtil;
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
import java.util.Map;

/**
 * Servlet for viewing individual bill details
 */
@WebServlet(name = "ViewBillController", urlPatterns = {"/bill/view", "/bill/detail"})
public class ViewBillController extends HttpServlet {
    
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
        String billIdStr = request.getParameter("id");
        String billNumber = request.getParameter("billNumber");
        
        if (ValidationUtil.isNullOrEmpty(billIdStr) && ValidationUtil.isNullOrEmpty(billNumber)) {
            SessionUtil.setErrorMessage(session, "Bill ID or Bill Number is required");
            response.sendRedirect(request.getContextPath() + "/bill/list");
            return;
        }
        
        try {
            Bill bill = null;
            
            // Get bill by ID or number
            if (!ValidationUtil.isNullOrEmpty(billIdStr)) {
                int billId = Integer.parseInt(billIdStr);
                bill = billingService.getBillById(billId);
            } else {
                bill = billingService.getBillByNumber(billNumber);
            }
            
            if (bill == null) {
                SessionUtil.setErrorMessage(session, "Bill not found");
                response.sendRedirect(request.getContextPath() + "/bill/list");
                return;
            }
            
            // Check if user has permission to view this bill
            if (!hasViewPermission(session, bill)) {
                SessionUtil.setErrorMessage(session, "You don't have permission to view this bill");
                response.sendRedirect(request.getContextPath() + "/bill/list");
                return;
            }
            
            // Get additional information
            Map<String, Object> customerPurchaseSummary = 
                billingService.getCustomerPurchaseSummary(bill.getCustomerId());
            
            // Check if bill can be edited or cancelled
            boolean isEditable = billingService.isBillEditable(bill.getBillId());
            boolean isCancellable = billingService.isBillCancellable(bill.getBillId());
            
            // Get payment status and method enums for display
            PaymentStatus paymentStatus = PaymentStatus.fromString(bill.getPaymentStatus());
            PaymentMethod paymentMethod = PaymentMethod.fromString(bill.getPaymentMethod());
            
            // Set attributes
            request.setAttribute("bill", bill);
            request.setAttribute("customerPurchaseSummary", customerPurchaseSummary);
            request.setAttribute("isEditable", isEditable);
            request.setAttribute("isCancellable", isCancellable);
            request.setAttribute("paymentStatus", paymentStatus);
            request.setAttribute("paymentMethod", paymentMethod);
            
            // Set action permissions based on user role
            User currentUser = SessionUtil.getLoggedInUser(session);
            setActionPermissions(request, currentUser, bill);
            
            // Forward to view bill JSP
            request.getRequestDispatcher("/views/billing/view-bill.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            SessionUtil.setErrorMessage(session, "Invalid bill ID format");
            response.sendRedirect(request.getContextPath() + "/bill/list");
        } catch (Exception e) {
            log("Error viewing bill: " + e.getMessage(), e);
            SessionUtil.setErrorMessage(session, "Error loading bill details: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/bill/list");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("ajax".equals(action)) {
            handleAjaxRequest(request, response);
        } else {
            handleActionRequest(request, response);
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
                case "processPayment":
                    handleProcessPayment(request, jsonResponse);
                    break;
                    
                case "updateNote":
                    handleUpdateNote(request, jsonResponse);
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
     * Handle action requests (cancel, edit, etc.)
     */
    private void handleActionRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String billAction = request.getParameter("billAction");
        String billIdStr = request.getParameter("billId");
        
        if (ValidationUtil.isNullOrEmpty(billIdStr)) {
            SessionUtil.setErrorMessage(session, "Bill ID is required");
            response.sendRedirect(request.getContextPath() + "/bill/list");
            return;
        }
        
        try {
            int billId = Integer.parseInt(billIdStr);
            int userId = SessionUtil.getLoggedInUserId(session);
            
            switch (billAction) {
                case "cancel":
                    handleCancelBill(request, response, billId, userId);
                    break;
                    
                case "edit":
                    response.sendRedirect(request.getContextPath() + "/bill/edit?id=" + billId);
                    break;
                    
                case "print":
                    response.sendRedirect(request.getContextPath() + "/bill/print?id=" + billId);
                    break;
                    
                default:
                    SessionUtil.setErrorMessage(session, "Unknown action: " + billAction);
                    response.sendRedirect(request.getContextPath() + "/bill/view?id=" + billId);
            }
            
        } catch (Exception e) {
            log("Error processing bill action: " + e.getMessage(), e);
            SessionUtil.setErrorMessage(session, "Error processing action: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/bill/list");
        }
    }
    
    /**
     * Handle cancel bill action
     */
    private void handleCancelBill(HttpServletRequest request, HttpServletResponse response, 
                                  int billId, int userId) throws Exception {
        
        HttpSession session = request.getSession();
        String reason = request.getParameter("cancellationReason");
        
        if (ValidationUtil.isNullOrEmpty(reason)) {
            SessionUtil.setErrorMessage(session, "Cancellation reason is required");
            response.sendRedirect(request.getContextPath() + "/bill/view?id=" + billId);
            return;
        }
        
        boolean success = billingService.cancelBill(billId, reason, userId);
        
        if (success) {
            SessionUtil.setSuccessMessage(session, "Bill cancelled successfully");
        } else {
            SessionUtil.setErrorMessage(session, "Failed to cancel bill");
        }
        
        response.sendRedirect(request.getContextPath() + "/bill/view?id=" + billId);
    }
    
    
    /**
     * Handle process payment AJAX request
     */
    private void handleProcessPayment(HttpServletRequest request, JsonObject response) 
            throws Exception {
        
        int billId = Integer.parseInt(request.getParameter("billId"));
        String paymentMethod = request.getParameter("paymentMethod");
        String paymentReference = request.getParameter("paymentReference");
        
        boolean success = billingService.processPayment(billId, paymentMethod, paymentReference);
        
        response.addProperty("success", success);
        if (success) {
            response.addProperty("message", "Payment processed successfully");
            
            // Return updated bill status
            Bill updatedBill = billingService.getBillById(billId);
            response.addProperty("newStatus", updatedBill.getPaymentStatus());
            response.addProperty("statusClass", updatedBill.getPaymentStatusClass());
        } else {
            response.addProperty("error", "Failed to process payment");
        }
    }
    
    /**
     * Handle update note AJAX request
     */
    private void handleUpdateNote(HttpServletRequest request, JsonObject response) 
            throws Exception {
        
        int billId = Integer.parseInt(request.getParameter("billId"));
        String newNote = request.getParameter("note");
        
        // This would require a specific DAO method to update notes
        Bill bill = billingService.getBillById(billId);
        if (bill != null) {
            bill.setNotes(newNote);
            boolean success = billingService.updateBill(bill);
            
            response.addProperty("success", success);
            response.addProperty("message", success ? "Note updated successfully" : "Failed to update note");
        } else {
            response.addProperty("success", false);
            response.addProperty("error", "Bill not found");
        }
    }
    
    
    /**
     * Check if user has permission to view bill
     */
    private boolean hasViewPermission(HttpSession session, Bill bill) {
        User currentUser = SessionUtil.getLoggedInUser(session);
        
        if (currentUser == null) {
            return false;
        }
        
        // Admin and Manager can view all bills
        if (currentUser.isAdmin() || currentUser.isManager()) {
            return true;
        }
        
        // Staff can only view bills they created
        if (currentUser.isStaff()) {
            return bill.getUserId() == currentUser.getUserId();
        }
        
        return false;
    }
    
    /**
     * Set action permissions based on user role
     */
    private void setActionPermissions(HttpServletRequest request, User currentUser, Bill bill) {
        boolean canEdit = false;
        boolean canCancel = false;
        boolean canProcessPayment = false;
        boolean canPrint = true;
        
        try {
            if (currentUser.isAdmin()) {
                // Admin can do everything
                canEdit = billingService.isBillEditable(bill.getBillId());
                canCancel = billingService.isBillCancellable(bill.getBillId());
                canProcessPayment = !"PAID".equals(bill.getPaymentStatus());
            } else if (currentUser.isManager()) {
                // Manager can edit and process payment
                canEdit = billingService.isBillEditable(bill.getBillId());
                canProcessPayment = !"PAID".equals(bill.getPaymentStatus());
                // Manager can only cancel bills created by staff
                canCancel = billingService.isBillCancellable(bill.getBillId()) && 
                           bill.getUserId() != currentUser.getUserId();
            } else if (currentUser.isStaff()) {
                // Staff can only edit their own pending bills
                canEdit = billingService.isBillEditable(bill.getBillId()) && 
                         bill.getUserId() == currentUser.getUserId();
                // Staff cannot cancel or process payment
                canCancel = false;
                canProcessPayment = false;
            }
        } catch (DatabaseException e) {
            log("Error checking permissions: " + e.getMessage(), e);
            // Set safe defaults on error
            canEdit = false;
            canCancel = false;
            canProcessPayment = false;
        }
        
        request.setAttribute("canEdit", canEdit);
        request.setAttribute("canCancel", canCancel);
        request.setAttribute("canProcessPayment", canProcessPayment);
        request.setAttribute("canPrint", canPrint);
    }
    
    @Override
    public String getServletInfo() {
        return "View Bill Controller - Displays individual bill details";
    }
}