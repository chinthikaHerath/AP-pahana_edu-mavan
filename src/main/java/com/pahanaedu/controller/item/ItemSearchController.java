package com.pahanaedu.controller.item;

import com.pahanaedu.service.interfaces.ItemService;
import com.pahanaedu.service.impl.ItemServiceImpl;
import com.pahanaedu.util.SessionUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/item/search")
public class ItemSearchController extends HttpServlet {
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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();
        
        try {
            String searchTerm = request.getParameter("term");
            String searchType = request.getParameter("type"); // code, name, isbn
            
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                jsonResponse.add("items", gson.toJsonTree(itemService.getAllActiveItems()));
            } else {
                jsonResponse.add("items", gson.toJsonTree(itemService.searchItems(searchTerm)));
            }
            
            jsonResponse.addProperty("success", true);
            
        } catch (Exception e) {
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", "Error searching items: " + e.getMessage());
        }
        
        out.print(jsonResponse.toString());
        out.flush();
    }
}