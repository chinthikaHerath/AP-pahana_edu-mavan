package com.pahanaedu.controller.customer;

import com.pahanaedu.service.interfaces.CustomerService;
import com.pahanaedu.service.impl.CustomerServiceImpl;
import com.pahanaedu.model.Customer;
import com.pahanaedu.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for toggling customer status (activate/deactivate)
 * Replaces the old DeleteCustomerController
 */
@WebServlet(name = "ToggleCustomerStatusController", urlPatterns = {"/customer/toggle-status"})
public class ToggleCustomerStatusController extends HttpServlet {
    
    private CustomerService customerService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        customerService = new CustomerServiceImpl();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String customerIdStr = request.getParameter("id");
        String action = request.getParameter("action"); // "activate" or "deactivate"
        
        // Validate parameters
        if (customerIdStr == null || customerIdStr.trim().isEmpty()) {
            SessionUtil.setErrorMessage(session, "Customer ID is required");
            response.sendRedirect(request.getContextPath() + "/customer/list");
            return;
        }
        
        try {
            int customerId = Integer.parseInt(customerIdStr);
            
            // Get current customer to check status
            Customer customer = customerService.getCustomerById(customerId);
            
            if (customer == null) {
                SessionUtil.setErrorMessage(session, "Customer not found");
                response.sendRedirect(request.getContextPath() + "/customer/list");
                return;
            }
            
            // Determine action if not specified
            if (action == null || action.trim().isEmpty()) {
                // Toggle based on current status
                action = customer.isActive() ? "deactivate" : "activate";
            }
            
            boolean success = false;
            String successMessage = "";
            
            if ("activate".equalsIgnoreCase(action)) {
                success = customerService.activateCustomer(customerId);
                successMessage = "Customer activated successfully";
            } else if ("deactivate".equalsIgnoreCase(action)) {
                success = customerService.deactivateCustomer(customerId);
                successMessage = "Customer deactivated successfully";
            } else {
                SessionUtil.setErrorMessage(session, "Invalid action specified");
                response.sendRedirect(request.getContextPath() + "/customer/list");
                return;
            }
            
            if (success) {
                SessionUtil.setSuccessMessage(session, successMessage);
            } else {
                SessionUtil.setErrorMessage(session, "Failed to update customer status");
            }
            
        } catch (NumberFormatException e) {
            SessionUtil.setErrorMessage(session, "Invalid customer ID format");
        } catch (Exception e) {
            log("Error toggling customer status: " + e.getMessage(), e);
            SessionUtil.setErrorMessage(session, "Error updating customer status: " + e.getMessage());
        }
        
        // Redirect back to customer list
        response.sendRedirect(request.getContextPath() + "/customer/list");
    }
    
    @Override
    public String getServletInfo() {
        return "Toggle Customer Status Controller - Activates or deactivates customers";
    }
}