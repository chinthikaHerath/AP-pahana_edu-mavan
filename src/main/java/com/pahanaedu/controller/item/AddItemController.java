package com.pahanaedu.controller.item;

import com.pahanaedu.service.interfaces.ItemService;
import com.pahanaedu.service.impl.ItemServiceImpl;
import com.pahanaedu.model.Item;
import com.pahanaedu.model.Category;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.exception.BusinessException;
import com.pahanaedu.util.SessionUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/item/add")
public class AddItemController extends HttpServlet {
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
            request.setAttribute("categories", itemService.getAllCategories());
            request.getRequestDispatcher("/views/item/add-item.jsp").forward(request, response);
        } catch (Exception e) {
            SessionUtil.setErrorMessage(request.getSession(), "Error loading form: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/items");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Item item = new Item();
        List<Category> categories = null;
        
        // Load categories once at the beginning
        try {
            categories = itemService.getAllCategories();
        } catch (BusinessException e) {
            log("Error loading categories: " + e.getMessage(), e);
        }

        try {
            // Extract item data from request
            item.setItemCode(request.getParameter("itemCode"));
            item.setItemName(request.getParameter("itemName"));
            item.setDescription(request.getParameter("description"));
            item.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
            item.setAuthor(request.getParameter("author"));
            item.setPublisher(request.getParameter("publisher"));
            item.setIsbn(request.getParameter("isbn"));
            item.setUnitPrice(Double.parseDouble(request.getParameter("unitPrice")));
            item.setSellingPrice(Double.parseDouble(request.getParameter("sellingPrice")));
            item.setQuantityInStock(Integer.parseInt(request.getParameter("quantityInStock")));
            item.setReorderLevel(Integer.parseInt(request.getParameter("reorderLevel")));

            int createdBy = SessionUtil.getLoggedInUserId(session);
            int itemId = itemService.addItem(item, createdBy);

            SessionUtil.setSuccessMessage(session, "Item added successfully!");
            response.sendRedirect(request.getContextPath() + "/items");

        } catch (ValidationException e) {
            request.setAttribute("validationErrors", e.getFieldErrors());
            request.setAttribute("errorMessage", "Please correct the errors below");
            request.setAttribute("item", item);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/views/item/add-item.jsp").forward(request, response);

        } catch (BusinessException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("item", item);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/views/item/add-item.jsp").forward(request, response);

        } catch (Exception e) {
            SessionUtil.setErrorMessage(session, "Error adding item: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/items");
        }
    }
}