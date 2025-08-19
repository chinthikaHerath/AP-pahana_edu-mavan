package com.pahanaedu.controller.item;

import com.pahanaedu.service.interfaces.ItemService;
import com.pahanaedu.service.impl.ItemServiceImpl;
import com.pahanaedu.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for displaying stock status
 * Shows ALL items including inactive ones for complete inventory visibility
 */
@WebServlet("/item/stock")
public class ItemStockController extends HttpServlet {
    
    private ItemService itemService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.itemService = new ItemServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String statusFilter = request.getParameter("status");
            
            if (statusFilter != null && !statusFilter.isEmpty()) {
                switch (statusFilter) {
                    case "low":
                        request.setAttribute("items", itemService.getLowStockItems());
                        break;
                    case "out":
                        request.setAttribute("items", itemService.getOutOfStockItems());
                        break;
                    default:
                        // CHANGE: Show ALL items including inactive
                        request.setAttribute("items", itemService.getAllItems(true));
                }
            } else {
                // MAIN CHANGE: Show ALL items including inactive by default
                request.setAttribute("items", itemService.getAllItems(true));
            }
            
            request.getRequestDispatcher("/views/item/stock-status.jsp").forward(request, response);
            
        } catch (Exception e) {
            SessionUtil.setErrorMessage(request.getSession(), "Error loading stock status: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/item");
        }
    }
}