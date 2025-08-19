package com.pahanaedu.controller.item;

import com.pahanaedu.service.interfaces.ItemService;
import com.pahanaedu.model.Item;
import com.pahanaedu.service.impl.ItemServiceImpl;
import com.pahanaedu.util.SessionUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/item/view")
public class ItemViewController extends HttpServlet {
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
        
        try {
            int itemId = Integer.parseInt(itemIdStr);
            Item item = itemService.getItemById(itemId);
            
            if (item == null) {
                SessionUtil.setErrorMessage(session, "Item not found");
                response.sendRedirect(request.getContextPath() + "/item");
                return;
            }
            
            request.setAttribute("item", item);
            request.getRequestDispatcher("/views/item/view-item.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            SessionUtil.setErrorMessage(session, "Invalid item ID");
            response.sendRedirect(request.getContextPath() + "/item");
        } catch (Exception e) {
            SessionUtil.setErrorMessage(session, "Error loading item: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/item");
        }
    }
}