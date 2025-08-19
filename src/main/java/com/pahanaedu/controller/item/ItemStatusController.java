package com.pahanaedu.controller.item;

import com.pahanaedu.service.interfaces.ItemService;
import com.pahanaedu.service.impl.ItemServiceImpl;
import com.pahanaedu.model.Item;
import com.pahanaedu.exception.BusinessException;
import com.pahanaedu.util.SessionUtil;
import com.pahanaedu.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import com.google.gson.JsonObject;

/**
 * Servlet for handling item activation and deactivation
 * Replaces the old DeleteItemController with more complete functionality
 */
@WebServlet(name = "ItemStatusController", urlPatterns = {
    "/item/activate", 
    "/item/deactivate", 
    "/item/toggle-status",
    "/item/delete" // Keep old URL for backward compatibility
})
public class ItemStatusController extends HttpServlet {
    
    private ItemService itemService;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.itemService = new ItemServiceImpl();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check if user is logged in
        if (!SessionUtil.isLoggedIn(session)) {
            if (isAjaxRequest(request)) {
                sendJsonError(response, "Session expired. Please login again.", 401);
            } else {
                response.sendRedirect(request.getContextPath() + "/login");
            }
            return;
        }
        
        String path = request.getServletPath();
        String itemIdStr = request.getParameter("id");
        
        if (ValidationUtil.isNullOrEmpty(itemIdStr)) {
            handleError(request, response, "Item ID is required");
            return;
        }
        
        try {
            int itemId = Integer.parseInt(itemIdStr);
            
            // Determine action based on URL path
            if (path.contains("/activate")) {
                handleActivation(request, response, itemId);
            } else if (path.contains("/deactivate") || path.contains("/delete")) {
                handleDeactivation(request, response, itemId);
            } else if (path.contains("/toggle-status")) {
                handleToggleStatus(request, response, itemId);
            } else {
                handleError(request, response, "Invalid action");
            }
            
        } catch (NumberFormatException e) {
            handleError(request, response, "Invalid item ID format");
        } catch (Exception e) {
            handleError(request, response, "Unexpected error: " + e.getMessage());
        }
    }
    
    /**
     * Handle item activation
     */
    private void handleActivation(HttpServletRequest request, HttpServletResponse response, int itemId) 
            throws IOException {
        
        HttpSession session = request.getSession();
        
        try {
            // Get item to check current status
            Item item = itemService.getItemById(itemId);
            
            if (item == null) {
                throw new BusinessException("Item not found");
            }
            
            if (item.isActive()) {
                throw new BusinessException("Item is already active");
            }
            
            // Activate the item
            boolean success = itemService.activateItem(itemId);
            
            if (success) {
                String message = String.format("Item '%s' activated successfully", item.getItemName());
                
                if (isAjaxRequest(request)) {
                    sendJsonSuccess(response, message, item);
                } else {
                    SessionUtil.setSuccessMessage(session, message);
                    redirectToReferrer(request, response);
                }
            } else {
                throw new BusinessException("Failed to activate item");
            }
            
        } catch (BusinessException e) {
            handleError(request, response, e.getMessage());
        }
    }
    
    /**
     * Handle item deactivation
     */
    private void handleDeactivation(HttpServletRequest request, HttpServletResponse response, int itemId) 
            throws IOException {
        
        HttpSession session = request.getSession();
        
        try {
            // Get item to check current status
            Item item = itemService.getItemById(itemId);
            
            if (item == null) {
                throw new BusinessException("Item not found");
            }
            
            if (!item.isActive()) {
                throw new BusinessException("Item is already inactive");
            }
            
            // Check if item has stock before deactivating
            if (item.getQuantityInStock() > 0) {
                String forceParam = request.getParameter("force");
                if (!"true".equals(forceParam)) {
                    throw new BusinessException(
                        String.format("Cannot deactivate item with existing stock (%d units). " +
                                    "Please adjust stock to zero first or use force=true parameter.", 
                                    item.getQuantityInStock()));
                }
            }
            
            // Deactivate the item
            boolean success = itemService.deactivateItem(itemId);
            
            if (success) {
                String message = String.format("Item '%s' deactivated successfully", item.getItemName());
                
                if (isAjaxRequest(request)) {
                    sendJsonSuccess(response, message, item);
                } else {
                    SessionUtil.setSuccessMessage(session, message);
                    redirectToReferrer(request, response);
                }
            } else {
                throw new BusinessException("Failed to deactivate item");
            }
            
        } catch (BusinessException e) {
            handleError(request, response, e.getMessage());
        }
    }
    
    /**
     * Handle toggle status (switch between active/inactive)
     */
    private void handleToggleStatus(HttpServletRequest request, HttpServletResponse response, int itemId) 
            throws IOException {
        
        try {
            Item item = itemService.getItemById(itemId);
            
            if (item == null) {
                throw new BusinessException("Item not found");
            }
            
            // Toggle based on current status
            if (item.isActive()) {
                handleDeactivation(request, response, itemId);
            } else {
                handleActivation(request, response, itemId);
            }
            
        } catch (BusinessException e) {
            handleError(request, response, e.getMessage());
        }
    }
    
    /**
     * Handle errors
     */
    private void handleError(HttpServletRequest request, HttpServletResponse response, String error) 
            throws IOException {
        
        if (isAjaxRequest(request)) {
            sendJsonError(response, error, 400);
        } else {
            HttpSession session = request.getSession();
            SessionUtil.setErrorMessage(session, error);
            redirectToReferrer(request, response);
        }
    }
    
    /**
     * Send JSON success response
     */
    private void sendJsonSuccess(HttpServletResponse response, String message, Item item) 
            throws IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", true);
        jsonResponse.addProperty("message", message);
        jsonResponse.addProperty("itemId", item.getItemId());
        jsonResponse.addProperty("itemName", item.getItemName());
        jsonResponse.addProperty("isActive", item.isActive());
        jsonResponse.addProperty("statusText", item.isActive() ? "Active" : "Inactive");
        jsonResponse.addProperty("statusClass", item.isActive() ? "success" : "secondary");
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }
    
    /**
     * Send JSON error response
     */
    private void sendJsonError(HttpServletResponse response, String error, int statusCode) 
            throws IOException {
        
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", false);
        jsonResponse.addProperty("error", error);
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }
    
    /**
     * Check if request is AJAX
     */
    private boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWith);
    }
    
    /**
     * Redirect to referrer or default page
     */
    private void redirectToReferrer(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String referrer = request.getHeader("Referer");
        String redirectUrl = request.getParameter("redirect");
        
        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            response.sendRedirect(request.getContextPath() + redirectUrl);
        } else if (referrer != null && !referrer.isEmpty()) {
            response.sendRedirect(referrer);
        } else {
            response.sendRedirect(request.getContextPath() + "/item/list");
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Item Status Controller - Handles item activation and deactivation";
    }
}