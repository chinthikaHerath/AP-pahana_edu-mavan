package com.pahanaedu.controller.customer;

import com.pahanaedu.service.interfaces.CustomerService;
import com.pahanaedu.service.impl.CustomerServiceImpl;
import com.pahanaedu.model.Customer;
import com.pahanaedu.constant.SystemConstants;
import com.pahanaedu.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for displaying customer list
 */
@WebServlet(name = "CustomerListController", urlPatterns = {"/customer/list", "/customer"})
public class CustomerListController extends HttpServlet {
    
    private CustomerService customerService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        customerService = new CustomerServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            // Get search parameter if any
            String searchTerm = request.getParameter("search");
            String city = request.getParameter("city");
            String statusFilter = request.getParameter("status"); // Optional: for future filtering
            
            List<Customer> customers;
            
            // Determine which list to get
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                customers = customerService.searchCustomers(searchTerm);
                request.setAttribute("searchTerm", searchTerm);
            } else if (city != null && !city.trim().isEmpty()) {
                customers = customerService.getCustomersByCity(city);
                request.setAttribute("selectedCity", city);
            } else {
                // IMPORTANT CHANGE: Use getAllCustomers() instead of getActiveCustomers()
                // This will show both active and inactive customers
                customers = customerService.getAllCustomers();
            }
            
            // Optional: If you want to add status filtering later
//            if (statusFilter != null && !statusFilter.trim().isEmpty()) {
//                if ("active".equalsIgnoreCase(statusFilter)) {
//                    customers = customerService.getActiveCustomers();
//                } else if ("inactive".equalsIgnoreCase(statusFilter)) {
//                    customers = customerService.getInactiveCustomers();
//                }
//                request.setAttribute("statusFilter", statusFilter);
//            }
            
            // Get all cities for filter dropdown
            List<String> cities = customerService.getAllCities();
            
            // Get customer statistics
            request.setAttribute("customerStats", customerService.getCustomerStatistics());
            
            // Set attributes
            request.setAttribute("customers", customers);
            request.setAttribute("cities", cities);
            request.setAttribute("customerCount", customers.size());
            
            // Forward to JSP
            request.getRequestDispatcher("/views/customer/customer-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            log("Error loading customer list: " + e.getMessage(), e);
            SessionUtil.setErrorMessage(session, "Error loading customer list: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("export".equals(action)) {
            exportCustomers(request, response);
        } else {
            doGet(request, response);
        }
    }
    
    /**
     * Export customers to CSV
     */
    private void exportCustomers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Export ALL customers, not just active ones
            List<Customer> customers = customerService.getAllCustomers();
            String csv = customerService.exportCustomersToCSV(customers);
            
            // Set response headers for CSV download
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=\"customers.csv\"");
            
            // Write CSV content
            response.getWriter().write(csv);
            
        } catch (Exception e) {
            log("Error exporting customers: " + e.getMessage(), e);
            HttpSession session = request.getSession();
            SessionUtil.setErrorMessage(session, "Error exporting customers: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/customer/list");
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Customer List Controller - Displays list of customers";
    }
}