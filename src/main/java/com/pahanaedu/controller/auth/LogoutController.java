package com.pahanaedu.controller.auth;

import com.pahanaedu.service.interfaces.AuthService;
import com.pahanaedu.service.impl.AuthServiceImpl;
import com.pahanaedu.constant.SystemConstants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for handling user logout
 */
@WebServlet(name = "LogoutController", urlPatterns = {"/logout"})
public class LogoutController extends HttpServlet {
    
    private AuthService authService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        authService = new AuthServiceImpl();
    }
    
    /**
     * Process logout request (both GET and POST)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processLogout(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processLogout(request, response);
    }
    
    /**
     * Process logout
     */
    private void processLogout(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get current session
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // Log logout action (optional)
            String username = (String) session.getAttribute(SystemConstants.SESSION_USERNAME);
            if (username != null) {
                log("User logged out: " + username);
            }
            
            // Logout user and invalidate session
            authService.logout(session);
        }
        
        // Create new session for message
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute(SystemConstants.REQUEST_SUCCESS_MESSAGE, 
            SystemConstants.SUCCESS_LOGOUT);
        
        // Redirect to login page
        response.sendRedirect(request.getContextPath() + SystemConstants.URL_LOGIN);
    }
    
    @Override
    public String getServletInfo() {
        return "Logout Controller - Handles user logout";
    }
}