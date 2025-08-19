package com.pahanaedu.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pahanaedu.model.User;
import com.pahanaedu.util.SessionUtil;

/**
 * Controller for displaying the Help page
 */
@WebServlet("/help")
public class HelpController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    /**
     * Displays the help page with user guidance
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = SessionUtil.getLoggedInUser(session);
        
        // Check if user is logged in
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // User is logged in, show the help page
        request.getRequestDispatcher("/views/help.jsp").forward(request, response);
    }
}