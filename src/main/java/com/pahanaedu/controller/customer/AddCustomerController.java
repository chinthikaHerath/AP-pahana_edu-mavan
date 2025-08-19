package com.pahanaedu.controller.customer;

import com.pahanaedu.service.interfaces.CustomerService;
import com.pahanaedu.service.impl.CustomerServiceImpl;
import com.pahanaedu.model.Customer;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.exception.BusinessException;
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

/**
 * Servlet for adding new customers
 */
@WebServlet(name = "AddCustomerController", urlPatterns = {"/customer/add"})
public class AddCustomerController extends HttpServlet {
    
    private CustomerService customerService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        customerService = new CustomerServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get cities for dropdown
        try {
            request.setAttribute("cities", customerService.getAllCities());
        } catch (Exception e) {
            log("Error loading cities: " + e.getMessage(), e);
        }
        
        // Forward to add customer form
        request.getRequestDispatcher("/views/customer/add-customer.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Customer customer = new Customer();
        
        try {
            // Extract customer data from request
            extractCustomerData(request, customer);
            
            // Get logged in user ID
            int createdBy = SessionUtil.getLoggedInUserId(session);
            
            // Add customer
            int customerId = customerService.addCustomer(customer, createdBy);
            
            // Set success message
            SessionUtil.setSuccessMessage(session, 
                "Customer added successfully! Account Number: " + customer.getAccountNumber());
            
            // Redirect to customer list
            response.sendRedirect(request.getContextPath() + "/customer/list");
            
        } catch (ValidationException e) {
            // Set validation errors
            request.setAttribute("validationErrors", e.getFieldErrors());
            request.setAttribute("errorMessage", "Please correct the errors below");
            
            // Preserve form data
            request.setAttribute("customer", customer);
            
            // Get cities for dropdown
            try {
                request.setAttribute("cities", customerService.getAllCities());
            } catch (Exception ex) {
                log("Error loading cities: " + ex.getMessage(), ex);
            }
            
            // Forward back to form
            request.getRequestDispatcher("/views/customer/add-customer.jsp").forward(request, response);
            
        } catch (BusinessException e) {
            // Set business error
            request.setAttribute("errorMessage", e.getMessage());
            
            // Preserve form data
            request.setAttribute("customer", customer);
            
            // Get cities for dropdown
            try {
                request.setAttribute("cities", customerService.getAllCities());
            } catch (Exception ex) {
                log("Error loading cities: " + ex.getMessage(), ex);
            }
            
            // Forward back to form
            request.getRequestDispatcher("/views/customer/add-customer.jsp").forward(request, response);
            
        } catch (Exception e) {
            log("Error adding customer: " + e.getMessage(), e);
            SessionUtil.setErrorMessage(session, "Error adding customer: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/customer/list");
        }
    }
    
    /**
     * Extract customer data from request
     */
    private void extractCustomerData(HttpServletRequest request, Customer customer) 
            throws ParseException {
        
        // Set customer data
        customer.setCustomerName(request.getParameter("customerName"));
        customer.setAddress(request.getParameter("address"));
        customer.setCity(request.getParameter("city"));
        customer.setPostalCode(request.getParameter("postalCode"));
        customer.setTelephone(request.getParameter("telephone"));
        customer.setMobile(request.getParameter("mobile"));
        customer.setEmail(request.getParameter("email"));
        customer.setNicNumber(request.getParameter("nicNumber"));
        
        // Set registration date if provided
        String regDate = request.getParameter("registrationDate");
        if (regDate != null && !regDate.trim().isEmpty()) {
            customer.setRegistrationDate(DateUtil.parseDate(regDate));
        }
        
        // Set active status (default true for new customers)
        customer.setActive(true);
    }
    
    @Override
    public String getServletInfo() {
        return "Add Customer Controller - Handles new customer creation";
    }
}