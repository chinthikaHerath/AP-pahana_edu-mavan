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
 * Servlet for viewing customer details
 */
@WebServlet(name = "ViewCustomerController", urlPatterns = {"/customer/view"})
public class ViewCustomerController extends HttpServlet {
    
    private CustomerService customerService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        customerService = new CustomerServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String customerIdStr = request.getParameter("id");
        HttpSession session = request.getSession();
        
        // Validate customer ID parameter
        if (customerIdStr == null || customerIdStr.trim().isEmpty()) {
            SessionUtil.setErrorMessage(session, "Customer ID is required");
            response.sendRedirect(request.getContextPath() + "/customer/list");
            return;
        }
        
        try {
            int customerId = Integer.parseInt(customerIdStr);
            
            // Get customer with purchase summary
            Customer customer = customerService.getCustomerWithPurchaseSummary(customerId);
            
            if (customer == null) {
                SessionUtil.setErrorMessage(session, "Customer not found");
                response.sendRedirect(request.getContextPath() + "/customer/list");
                return;
            }
            
            // Set customer in request
            request.setAttribute("customer", customer);
            
            // TODO: When billing module is implemented, add these:
            // - Recent bills for this customer
            // - Total purchase statistics
            // - Outstanding payments if any
            
            // Forward to view page
            request.getRequestDispatcher("/views/customer/view-customer.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            SessionUtil.setErrorMessage(session, "Invalid customer ID format");
            response.sendRedirect(request.getContextPath() + "/customer/list");
            
        } catch (Exception e) {
            log("Error loading customer details: " + e.getMessage(), e);
            SessionUtil.setErrorMessage(session, "Error loading customer details: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/customer/list");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirect POST requests to GET
        doGet(request, response);
    }
    
    @Override
    public String getServletInfo() {
        return "View Customer Controller - Displays detailed customer information";
    }
}