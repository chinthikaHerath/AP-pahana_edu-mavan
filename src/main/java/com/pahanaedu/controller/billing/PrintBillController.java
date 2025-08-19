package com.pahanaedu.controller.billing;

import com.pahanaedu.service.interfaces.BillingService;
import com.pahanaedu.service.impl.BillingServiceImpl;
import com.pahanaedu.model.Bill;
import com.pahanaedu.model.BillItem;
import com.pahanaedu.util.SessionUtil;
import com.pahanaedu.constant.SystemConstants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Servlet for printing bills
 * Provides a print-friendly view of the bill
 */
@WebServlet(name = "PrintBillController", urlPatterns = {"/bill/print/*"})
public class PrintBillController extends HttpServlet {
    
    private BillingService billingService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        billingService = new BillingServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            // Extract bill ID from URL
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.length() <= 1) {
                throw new IllegalArgumentException("Bill ID is required");
            }
            
            String billIdStr = pathInfo.substring(1); // Remove leading "/"
            int billId = Integer.parseInt(billIdStr);
            
            // Get bill details
            Bill bill = billingService.getBillById(billId);
            
            if (bill == null) {
                SessionUtil.setErrorMessage(session, "Bill not found");
                response.sendRedirect(request.getContextPath() + "/bill/list");
                return;
            }
            
            // Set attributes for JSP
            request.setAttribute("bill", bill);
            request.setAttribute("currentDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
            
            // Company info (you can load this from database/settings)
            request.setAttribute("companyName", "Pahana Edu Bookshop");
            request.setAttribute("companyAddress", "123 Main Street, Colombo 07");
            request.setAttribute("companyPhone", "011-2345678");
            request.setAttribute("companyEmail", "info@pahanaedu.lk");
            
            // Forward to print view
            request.getRequestDispatcher("/views/billing/print-bill.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            SessionUtil.setErrorMessage(session, "Invalid bill ID format");
            response.sendRedirect(request.getContextPath() + "/bill/list");
        } catch (Exception e) {
            log("Error loading bill for printing: " + e.getMessage(), e);
            SessionUtil.setErrorMessage(session, "Error loading bill: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/bill/list");
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Print Bill Controller - Displays print-friendly bill view";
    }
}