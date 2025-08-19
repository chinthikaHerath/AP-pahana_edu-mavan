package com.pahanaedu.controller;

import com.pahanaedu.service.interfaces.ReportService;
import com.pahanaedu.service.impl.ReportServiceImpl;
import com.pahanaedu.dto.DashboardDTO;
import com.pahanaedu.model.User;
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
 * Servlet for handling simplified dashboard display
 * Focuses on summary statistics and quick actions only
 */
@WebServlet(name = "DashboardController", urlPatterns = {"/dashboard", "/home"})
public class DashboardController extends HttpServlet {
    
    private ReportService reportService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        reportService = new ReportServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User loggedUser = SessionUtil.getLoggedInUser(session);
        
        // Check if user is logged in
        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            // Generate simplified dashboard statistics
            DashboardDTO dashboardStats = reportService.generateDashboardStatistics();
            
            // Set attributes for JSP
            request.setAttribute("currentUser", loggedUser);
            request.setAttribute("dashboardStats", dashboardStats);
            
            // Log dashboard access (optional)
            log("Dashboard accessed by user: " + loggedUser.getUsername());
            
            // Forward to simplified dashboard JSP
            request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            log("Error loading dashboard for user " + loggedUser.getUsername() + ": " + e.getMessage(), e);
            
            // Create a minimal dashboard if service fails
            DashboardDTO fallbackStats = createFallbackDashboard();
            request.setAttribute("currentUser", loggedUser);
            request.setAttribute("dashboardStats", fallbackStats);
            
            // Set error message
            SessionUtil.setWarningMessage(session, 
                "Dashboard loaded with limited data. Some statistics may be unavailable.");
            
            // Still show dashboard even with limited data
            request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Simply redirect to GET for any POST requests
        doGet(request, response);
    }
    
    /**
     * Create a fallback dashboard with default values
     * Used when the service layer fails to provide data
     */
    private DashboardDTO createFallbackDashboard() {
        DashboardDTO dashboard = new DashboardDTO();
        
        // Set all values to 0 or empty
        dashboard.setTotalCustomers(0);
        dashboard.setActiveCustomers(0);
        dashboard.setTotalItems(0);
        dashboard.setActiveItems(0);
        dashboard.setTodaysSales(0.0);
        dashboard.setTodaysBills(0);
        dashboard.setLowStockItems(0);
        dashboard.setOutOfStockItems(0);
        dashboard.setAverageBillValue(0.0);
        dashboard.setNewCustomersToday(0);
        
        return dashboard;
    }
    
    @Override
    public String getServletInfo() {
        return "Simplified Dashboard Controller - Displays summary statistics and quick actions";
    }
}