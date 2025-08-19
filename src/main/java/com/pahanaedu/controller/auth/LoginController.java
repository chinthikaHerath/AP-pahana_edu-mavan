package com.pahanaedu.controller.auth;

import com.pahanaedu.service.interfaces.AuthService;
import com.pahanaedu.service.impl.AuthServiceImpl;
import com.pahanaedu.model.User;
import com.pahanaedu.exception.AuthenticationException;
import com.pahanaedu.exception.DatabaseException;
import com.pahanaedu.constant.SystemConstants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for handling user login
 */
@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {
    
    private AuthService authService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        authService = new AuthServiceImpl();
    }
    
    /**
     * Display login page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (authService.isLoggedIn(session)) {
            response.sendRedirect(request.getContextPath() + SystemConstants.URL_DASHBOARD);
            return;
        }
        
        // Forward to login page
        request.getRequestDispatcher(SystemConstants.PAGE_LOGIN).forward(request, response);
    }
    
    /**
     * Process login request
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get login credentials
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");
        
        try {
            // Attempt login
            HttpSession session = request.getSession(true);
            User user = authService.login(username, password, session);
            
            // Handle remember me (optional - can implement cookie-based remember me)
            if ("on".equals(rememberMe)) {
                // TODO: Implement remember me functionality with cookies
            }
            
            // Set success message
            session.setAttribute(SystemConstants.REQUEST_SUCCESS_MESSAGE, 
                "Welcome back, " + user.getFullName() + "!");
            
            // Redirect to dashboard or requested page
            String redirectUrl = (String) session.getAttribute("redirectUrl");
            if (redirectUrl != null) {
                session.removeAttribute("redirectUrl");
                response.sendRedirect(redirectUrl);
            } else {
                response.sendRedirect(request.getContextPath() + SystemConstants.URL_DASHBOARD);
            }
            
        } catch (AuthenticationException e) {
            // Set error message
            request.setAttribute(SystemConstants.REQUEST_ERROR_MESSAGE, e.getMessage());
            
            // Preserve username for convenience
            request.setAttribute("username", username);
            
            // Forward back to login page
            request.getRequestDispatcher(SystemConstants.PAGE_LOGIN).forward(request, response);
            
        } catch (DatabaseException e) {
            // Log error
            log("Database error during login: " + e.getMessage(), e);
            
            // Set error message
            request.setAttribute(SystemConstants.REQUEST_ERROR_MESSAGE, 
                SystemConstants.ERROR_DATABASE_CONNECTION);
            
            // Forward back to login page
            request.getRequestDispatcher(SystemConstants.PAGE_LOGIN).forward(request, response);
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Login Controller - Handles user authentication";
    }
}