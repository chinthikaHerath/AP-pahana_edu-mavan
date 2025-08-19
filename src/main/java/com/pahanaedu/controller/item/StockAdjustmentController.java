package com.pahanaedu.controller.item;

import com.pahanaedu.service.interfaces.ItemService;
import com.pahanaedu.service.impl.ItemServiceImpl;
import com.pahanaedu.model.Item;
import com.pahanaedu.model.StockMovement;
import com.pahanaedu.model.User;
import com.pahanaedu.constant.MovementType;
import com.pahanaedu.constant.SystemConstants;
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
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet for handling stock adjustments
 * Provides both page-based and AJAX-based stock adjustment functionality
 */
@WebServlet(name = "StockAdjustmentController", urlPatterns = {"/item/adjust-stock", "/item/ajax/adjust-stock"})
public class StockAdjustmentController extends HttpServlet {
    
    private ItemService itemService;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.itemService = new ItemServiceImpl();
        this.gson = new Gson();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check if user is logged in
        if (!SessionUtil.isLoggedIn(session)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String itemIdStr = request.getParameter("id");
        
        if (itemIdStr == null || itemIdStr.trim().isEmpty()) {
            SessionUtil.setErrorMessage(session, "Item ID is required");
            response.sendRedirect(request.getContextPath() + "/item/stock");
            return;
        }
        
        try {
            int itemId = Integer.parseInt(itemIdStr);
            
            // Get item details
            Item item = itemService.getItemById(itemId);
            
            if (item == null) {
                SessionUtil.setErrorMessage(session, "Item not found");
                response.sendRedirect(request.getContextPath() + "/item/stock");
                return;
            }
            
            // Get stock history for this item (last 10 movements)
            List<StockMovement> stockHistory = itemService.getStockHistory(itemId);
            if (stockHistory != null && stockHistory.size() > 10) {
                stockHistory = stockHistory.subList(0, 10);
            }
            
            // Set attributes for JSP
            request.setAttribute("item", item);
            request.setAttribute("movementTypes", MovementType.values());
            request.setAttribute("stockHistory", stockHistory);
            
            // Forward to adjustment page
            request.getRequestDispatcher("/views/item/adjust-stock.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            SessionUtil.setErrorMessage(session, "Invalid item ID");
            response.sendRedirect(request.getContextPath() + "/item/stock");
            
        } catch (BusinessException e) {
            log("Error loading item for stock adjustment: " + e.getMessage(), e);
            SessionUtil.setErrorMessage(session, "Error: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/item/stock");
            
        } catch (Exception e) {
            log("Unexpected error in stock adjustment: " + e.getMessage(), e);
            SessionUtil.setErrorMessage(session, "An unexpected error occurred");
            response.sendRedirect(request.getContextPath() + "/item/stock");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check if user is logged in
        if (!SessionUtil.isLoggedIn(session)) {
            // Check if it's an AJAX request
            if (isAjaxRequest(request)) {
                sendJsonError(response, "Session expired. Please login again.");
            } else {
                response.sendRedirect(request.getContextPath() + "/login");
            }
            return;
        }
        
        // Check if it's an AJAX request
        if (isAjaxRequest(request)) {
            handleAjaxStockAdjustment(request, response);
        } else {
            handleFormStockAdjustment(request, response);
        }
    }
    
    /**
     * Handle form-based stock adjustment
     */
    private void handleFormStockAdjustment(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String itemIdStr = request.getParameter("itemId");
        
        try {
            // Extract and validate parameters
            int itemId = Integer.parseInt(itemIdStr);
            String movementTypeStr = request.getParameter("movementType");
            String quantityStr = request.getParameter("quantity");
            String reason = request.getParameter("reason");
            
            // Validate inputs
            if (ValidationUtil.isNullOrEmpty(movementTypeStr)) {
                throw new BusinessException("Movement type is required");
            }
            
            if (ValidationUtil.isNullOrEmpty(quantityStr)) {
                throw new BusinessException("Quantity is required");
            }
            
            if (ValidationUtil.isNullOrEmpty(reason)) {
                throw new BusinessException("Reason is required");
            }
            
            // Parse values
            MovementType movementType = MovementType.valueOf(movementTypeStr);
            int quantity = Integer.parseInt(quantityStr);
            
            // Validate quantity
            if (quantity <= 0) {
                throw new BusinessException("Quantity must be positive");
            }
            
            // Get logged-in user ID
            int userId = SessionUtil.getLoggedInUserId(session);
            if (userId <= 0) {
                throw new BusinessException("User session invalid");
            }
            
            // Perform stock adjustment
            boolean success = itemService.adjustStockWithMovement(
                itemId, movementType, quantity, reason.trim(), userId
            );
            
            if (success) {
                SessionUtil.setSuccessMessage(session, 
                    "Stock adjusted successfully. " + movementType.getDisplayName() + 
                    " of " + quantity + " units recorded.");
                    
                // Redirect to stock status page
                response.sendRedirect(request.getContextPath() + "/item/stock");
            } else {
                throw new BusinessException("Failed to adjust stock");
            }
            
        } catch (NumberFormatException e) {
            SessionUtil.setErrorMessage(session, "Invalid number format");
            redirectBackToForm(request, response, itemIdStr);
            
        } catch (IllegalArgumentException e) {
            SessionUtil.setErrorMessage(session, "Invalid movement type");
            redirectBackToForm(request, response, itemIdStr);
            
        } catch (BusinessException e) {
            SessionUtil.setErrorMessage(session, e.getMessage());
            redirectBackToForm(request, response, itemIdStr);
            
        } catch (Exception e) {
            log("Unexpected error during stock adjustment: " + e.getMessage(), e);
            SessionUtil.setErrorMessage(session, "An unexpected error occurred");
            redirectBackToForm(request, response, itemIdStr);
        }
    }
    
    /**
     * Handle AJAX-based stock adjustment (for modal implementation)
     */
    private void handleAjaxStockAdjustment(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();
        HttpSession session = request.getSession();
        
        try {
            // Extract parameters
            int itemId = Integer.parseInt(request.getParameter("itemId"));
            MovementType movementType = MovementType.valueOf(request.getParameter("movementType"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String reason = request.getParameter("reason");
            
            // Validate reason
            if (ValidationUtil.isNullOrEmpty(reason)) {
                throw new BusinessException("Reason is required");
            }
            
            // Get user ID
            int userId = SessionUtil.getLoggedInUserId(session);
            if (userId <= 0) {
                throw new BusinessException("User session invalid");
            }
            
            // Perform adjustment
            boolean success = itemService.adjustStockWithMovement(
                itemId, movementType, quantity, reason.trim(), userId
            );
            
            if (success) {
                // Get updated item info
                Item updatedItem = itemService.getItemById(itemId);
                
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "Stock adjusted successfully");
                jsonResponse.addProperty("newQuantity", updatedItem.getQuantityInStock());
                jsonResponse.addProperty("stockStatus", updatedItem.getStockStatus());
                jsonResponse.addProperty("stockStatusClass", updatedItem.getStockStatusClass());
            } else {
                jsonResponse.addProperty("success", false);
                jsonResponse.addProperty("error", "Failed to adjust stock");
            }
            
        } catch (NumberFormatException e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", "Invalid number format");
            
        } catch (IllegalArgumentException e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", "Invalid movement type");
            
        } catch (BusinessException e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", e.getMessage());
            
        } catch (Exception e) {
            log("AJAX stock adjustment error: " + e.getMessage(), e);
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", "An unexpected error occurred");
        }
        
        out.print(jsonResponse.toString());
        out.flush();
    }
    
    /**
     * Redirect back to adjustment form with item ID
     */
    private void redirectBackToForm(HttpServletRequest request, HttpServletResponse response, 
                                   String itemId) throws IOException {
        if (itemId != null && !itemId.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/item/adjust-stock?id=" + itemId);
        } else {
            response.sendRedirect(request.getContextPath() + "/item/stock");
        }
    }
    
    /**
     * Check if request is AJAX
     */
    private boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        String contentType = request.getContentType();
        String pathInfo = request.getServletPath();
        
        return "XMLHttpRequest".equals(requestedWith) || 
               (contentType != null && contentType.contains("application/json")) ||
               (pathInfo != null && pathInfo.contains("/ajax/"));
    }
    
    /**
     * Send JSON error response
     */
    private void sendJsonError(HttpServletResponse response, String error) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("success", false);
        jsonResponse.addProperty("error", error);
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }
    
    @Override
    public String getServletInfo() {
        return "Stock Adjustment Controller - Handles stock movements and adjustments";
    }
}