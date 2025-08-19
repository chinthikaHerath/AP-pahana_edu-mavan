package com.pahanaedu.controller.customer;

import com.pahanaedu.service.interfaces.CustomerService;
import com.pahanaedu.service.impl.CustomerServiceImpl;
import com.pahanaedu.model.Customer;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.exception.BusinessException;
import com.pahanaedu.constant.SystemConstants;
import com.pahanaedu.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for editing existing customers
 */
@WebServlet(name = "EditCustomerController", urlPatterns = {"/customer/edit"})
public class EditCustomerController extends HttpServlet {
    
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
        
        if (customerIdStr == null || customerIdStr.trim().isEmpty()) {
            SessionUtil.setErrorMessage(session, "Customer ID is required");
            response.sendRedirect(request.getContextPath() + "/customer/list");
            return;
        }
        
        try {
            int customerId = Integer.parseInt(customerIdStr);
            Customer customer = customerService.getCustomerById(customerId);
            
            if (customer == null) {
                SessionUtil.setErrorMessage(session, "Customer not found");
                response.sendRedirect(request.getContextPath() + "/customer/list");
                return;
            }
            
            // Set customer and cities
            request.setAttribute("customer", customer);
            request.setAttribute("cities", customerService.getAllCities());
            
            // Forward to edit form
            request.getRequestDispatcher("/views/customer/edit-customer.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            SessionUtil.setErrorMessage(session, "Invalid customer ID");
            response.sendRedirect(request.getContextPath() + "/customer/list");
        } catch (Exception e) {
            log("Error loading customer for edit: " + e.getMessage(), e);
            SessionUtil.setErrorMessage(session, "Error loading customer: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/customer/list");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String customerIdStr = request.getParameter("customerId");
        
        if (customerIdStr == null || customerIdStr.trim().isEmpty()) {
            SessionUtil.setErrorMessage(session, "Customer ID is required");
            response.sendRedirect(request.getContextPath() + "/customer/list");
            return;
        }
        
        try {
            int customerId = Integer.parseInt(customerIdStr);
            
            // Get existing customer
            Customer existingCustomer = customerService.getCustomerById(customerId);
            if (existingCustomer == null) {
                SessionUtil.setErrorMessage(session, "Customer not found");
                response.sendRedirect(request.getContextPath() + "/customer/list");
                return;
            }
            
            // Update customer data
            updateCustomerData(request, existingCustomer);
            
            // Update customer
            boolean success = customerService.updateCustomer(existingCustomer);
            
            if (success) {
                SessionUtil.setSuccessMessage(session, "Customer updated successfully!");
                response.sendRedirect(request.getContextPath() + "/customer/list");
            } else {
                throw new BusinessException("Failed to update customer");
            }
            
        } catch (ValidationException e) {
            // Set validation errors
            request.setAttribute("validationErrors", e.getFieldErrors());
            request.setAttribute("errorMessage", "Please correct the errors below");
            
            // Reload customer data
            try {
                int customerId = Integer.parseInt(customerIdStr);
                Customer customer = customerService.getCustomerById(customerId);
                
                // Update with form data
                updateCustomerData(request, customer);
                
                request.setAttribute("customer", customer);
                request.setAttribute("cities", customerService.getAllCities());
            } catch (Exception ex) {
                log("Error reloading customer: " + ex.getMessage(), ex);
            }
            
            // Forward back to form
            request.getRequestDispatcher("/views/customer/edit-customer.jsp").forward(request, response);
            
        } catch (BusinessException e) {
            // Set business error
            request.setAttribute("errorMessage", e.getMessage());
            
            // Reload customer data
            try {
                int customerId = Integer.parseInt(customerIdStr);
                Customer customer = customerService.getCustomerById(customerId);
                request.setAttribute("customer", customer);
                request.setAttribute("cities", customerService.getAllCities());
            } catch (Exception ex) {
                log("Error reloading customer: " + ex.getMessage(), ex);
            }
            
            // Forward back to form
            request.getRequestDispatcher("/views/customer/edit-customer.jsp").forward(request, response);
            
        } catch (Exception e) {
            log("Error updating customer: " + e.getMessage(), e);
            SessionUtil.setErrorMessage(session, "Error updating customer: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/customer/list");
        }
    }
    
    /**
     * Update customer data from request
     */
    private void updateCustomerData(HttpServletRequest request, Customer customer) {
        customer.setCustomerName(request.getParameter("customerName"));
        customer.setAddress(request.getParameter("address"));
        customer.setCity(request.getParameter("city"));
        customer.setPostalCode(request.getParameter("postalCode"));
        customer.setTelephone(request.getParameter("telephone"));
        customer.setMobile(request.getParameter("mobile"));
        customer.setEmail(request.getParameter("email"));
        
        // Note: We don't update NIC number, account number, or registration date
    }
    
    @Override
    public String getServletInfo() {
        return "Edit Customer Controller - Handles customer updates";
    }
}