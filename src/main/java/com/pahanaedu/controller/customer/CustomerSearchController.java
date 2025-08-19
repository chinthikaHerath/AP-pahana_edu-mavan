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
import java.io.PrintWriter;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet for searching customers (AJAX endpoint)
 */
@WebServlet(name = "CustomerSearchController", urlPatterns = {"/customer/search"})
public class CustomerSearchController extends HttpServlet {
    
    private CustomerService customerService;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        super.init();
        customerService = new CustomerServiceImpl();
        gson = new Gson();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Set response type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();
        
        try {
            String searchTerm = request.getParameter("term");
            String searchType = request.getParameter("type"); // account, name, phone
            
            List<Customer> customers;
            
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                customers = customerService.getActiveCustomers();
            } else {
                customers = customerService.searchCustomers(searchTerm);
            }
            
            // Convert to JSON
            jsonResponse.addProperty("success", true);
            jsonResponse.add("customers", gson.toJsonTree(customers));
            jsonResponse.addProperty("count", customers.size());
            
        } catch (Exception e) {
            log("Error searching customers: " + e.getMessage(), e);
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", "Error searching customers: " + e.getMessage());
        }
        
        out.print(jsonResponse.toString());
        out.flush();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    @Override
    public String getServletInfo() {
        return "Customer Search Controller - AJAX endpoint for customer search";
    }
}