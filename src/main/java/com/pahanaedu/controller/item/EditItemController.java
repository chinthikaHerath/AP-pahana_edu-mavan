package com.pahanaedu.controller.item;

import com.pahanaedu.model.Item;
import com.pahanaedu.service.interfaces.ItemService;
import com.pahanaedu.service.impl.ItemServiceImpl;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.exception.BusinessException;
import com.pahanaedu.util.SessionUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/item/edit")
public class EditItemController extends HttpServlet {
    private ItemService itemService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.itemService = new ItemServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String itemIdStr = request.getParameter("id");
        HttpSession session = request.getSession();
        
        if (itemIdStr == null || itemIdStr.trim().isEmpty()) {
            SessionUtil.setErrorMessage(session, "Item ID is required");
            response.sendRedirect(request.getContextPath() + "/items");
            return;
        }
        
        try {
            int itemId = Integer.parseInt(itemIdStr);
            Item item = itemService.getItemById(itemId);
            
            if (item == null) {
                SessionUtil.setErrorMessage(session, "Item not found");
                response.sendRedirect(request.getContextPath() + "/items");
                return;
            }
            
            request.setAttribute("item", item);
            request.setAttribute("categories", itemService.getAllCategories());
            request.getRequestDispatcher("/views/item/edit-item.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            SessionUtil.setErrorMessage(session, "Invalid item ID");
            response.sendRedirect(request.getContextPath() + "/items");
        } catch (Exception e) {
            SessionUtil.setErrorMessage(session, "Error loading item: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/items");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String itemIdStr = request.getParameter("itemId");
        
        if (itemIdStr == null || itemIdStr.trim().isEmpty()) {
            SessionUtil.setErrorMessage(session, "Item ID is required");
            response.sendRedirect(request.getContextPath() + "/items");
            return;
        }
        
        try {
            int itemId = Integer.parseInt(itemIdStr);
            Item existingItem = itemService.getItemById(itemId);
            
            if (existingItem == null) {
                SessionUtil.setErrorMessage(session, "Item not found");
                response.sendRedirect(request.getContextPath() + "/items");
                return;
            }
            
            // Update item data
            existingItem.setItemName(request.getParameter("itemName"));
            existingItem.setDescription(request.getParameter("description"));
            existingItem.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
            existingItem.setAuthor(request.getParameter("author"));
            existingItem.setPublisher(request.getParameter("publisher"));
            existingItem.setIsbn(request.getParameter("isbn"));
            existingItem.setUnitPrice(Double.parseDouble(request.getParameter("unitPrice")));
            existingItem.setSellingPrice(Double.parseDouble(request.getParameter("sellingPrice")));
            existingItem.setQuantityInStock(Integer.parseInt(request.getParameter("quantityInStock")));
            existingItem.setReorderLevel(Integer.parseInt(request.getParameter("reorderLevel")));
            
            boolean success = itemService.updateItem(existingItem);
            
            if (success) {
                SessionUtil.setSuccessMessage(session, "Item updated successfully!");
                response.sendRedirect(request.getContextPath() + "/item/list");
            } else {
                throw new BusinessException("Failed to update item");
            }
            
        } catch (ValidationException e) {
            request.setAttribute("validationErrors", e.getFieldErrors());
            request.setAttribute("errorMessage", "Please correct the errors below");
            
            try {
                int itemId = Integer.parseInt(itemIdStr);
                Item item = itemService.getItemById(itemId);
                request.setAttribute("item", item);
                request.setAttribute("categories", itemService.getAllCategories());
            } catch (Exception ex) {
                log("Error reloading item: " + ex.getMessage(), ex);
            }
            
            request.getRequestDispatcher("/views/item/edit-item.jsp").forward(request, response);
            
        } catch (Exception e) {
            SessionUtil.setErrorMessage(session, "Error updating item: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/items");
        }
    }
}