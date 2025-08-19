package com.pahanaedu.controller.item;

import com.pahanaedu.service.interfaces.ItemService;
import com.pahanaedu.service.impl.ItemServiceImpl;
import com.pahanaedu.util.SessionUtil;
import com.pahanaedu.constant.SystemConstants;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for displaying item list
 * Shows ALL items - both active and inactive
 */
@WebServlet(name = "ItemListController", urlPatterns = {"/item/list", "/item", "/items"})
public class ItemListController extends HttpServlet {
    
    private ItemService itemService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.itemService = new ItemServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            String searchTerm = request.getParameter("search");
            String categoryId = request.getParameter("category");
            
            // CHANGE: Use getAllItems(true) to include inactive items
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                // For search, we need to update the search method to include inactive items
                request.setAttribute("items", itemService.searchItems(searchTerm));
                request.setAttribute("searchTerm", searchTerm);
            } else if (categoryId != null && !categoryId.trim().isEmpty()) {
                // For category filter, we need to get items including inactive ones
                request.setAttribute("items", itemService.getItemsByCategory(Integer.parseInt(categoryId)));
                request.setAttribute("selectedCategory", categoryId);
            } else {
                // MAIN CHANGE: Get ALL items (both active and inactive)
                request.setAttribute("items", itemService.getAllItems(true));
            }
            
            request.setAttribute("categories", itemService.getAllCategories());
            request.getRequestDispatcher("/views/item/item-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            SessionUtil.setErrorMessage(session, "Error loading items: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
}