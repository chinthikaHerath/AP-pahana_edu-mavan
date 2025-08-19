package com.pahanaedu.controller.report;

import com.pahanaedu.service.interfaces.ReportService;
import com.pahanaedu.service.impl.ReportServiceImpl;
import com.pahanaedu.dto.ReportDTO;
import com.pahanaedu.constant.SystemConstants;
import com.pahanaedu.util.SessionUtil;
import com.pahanaedu.dao.interfaces.ItemDAO;
import com.pahanaedu.dao.impl.ItemDAOImpl;
import com.pahanaedu.model.Item;
import com.pahanaedu.model.Category;
import com.pahanaedu.dao.interfaces.CategoryDAO;
import com.pahanaedu.dao.impl.CategoryDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * Servlet for generating combined stock reports
 */
@WebServlet(name = "StockReportController", urlPatterns = {"/report/stock", "/report/inventory"})
public class StockReportController extends HttpServlet {
    
    private ReportService reportService;
    private ItemDAO itemDAO;
    private CategoryDAO categoryDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        reportService = new ReportServiceImpl();
        itemDAO = new ItemDAOImpl();
        categoryDAO = new CategoryDAOImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            // Generate combined report with both full stock and low stock data
            ReportDTO report = generateCombinedStockReport(request);
            
            // Add username to report
            report.setGeneratedBy(SessionUtil.getLoggedInUsername(session));
            
            // Set attributes
            request.setAttribute("report", report);
            
            // Check if export is requested
            String export = request.getParameter("export");
            if ("csv".equalsIgnoreCase(export)) {
                exportToCSV(request, response, report);
                return;
            }
            
            // Forward to JSP
            request.getRequestDispatcher("/views/reports/stock-report.jsp").forward(request, response);
            
        } catch (Exception e) {
            log("Error generating stock report: " + e.getMessage(), e);
            SessionUtil.setErrorMessage(session, "Error generating report: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
    
    /**
     * Generate a combined report with both full stock and low stock information
     */
    private ReportDTO generateCombinedStockReport(HttpServletRequest request) throws Exception {
        ReportDTO report = new ReportDTO("Complete Stock Report", "STOCK");
        report.setGeneratedDate(new Date());
        
        // Get all active items
        List<Item> allItems = itemDAO.getAllActiveItems();
        
        // Statistics
        int totalItems = allItems.size();
        int lowStockCount = 0;
        int outOfStockCount = 0;
        double totalStockValue = 0.0;
        
        // Lists for the report
        List<Map<String, Object>> fullStockList = new ArrayList<>();
        List<Map<String, Object>> lowStockList = new ArrayList<>();
        
        // Process each item
        for (Item item : allItems) {
            // Calculate stock value
            double stockValue = item.getSellingPrice() * item.getQuantityInStock();
            totalStockValue += stockValue;
            
            // Check stock status
            boolean isOutOfStock = item.getQuantityInStock() == 0;
            boolean isLowStock = item.getQuantityInStock() <= item.getReorderLevel();
            
            if (isOutOfStock) {
                outOfStockCount++;
            }
            if (isLowStock) {
                lowStockCount++;
            }
            
            // Create item data map
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("itemCode", item.getItemCode());
            itemData.put("itemName", item.getItemName());
            itemData.put("category", getCategoryName(item.getCategoryId()));
            itemData.put("currentStock", item.getQuantityInStock());
            itemData.put("reorderLevel", item.getReorderLevel());
            itemData.put("unitPrice", item.getSellingPrice());
            itemData.put("stockValue", stockValue);
            
            // Add to full stock list
            fullStockList.add(itemData);
            
            // Add to low stock list if applicable
            if (isLowStock) {
                lowStockList.add(itemData);
            }
        }
        
        // Set basic statistics
        report.setTotalItems(totalItems);
        report.setLowStockItems(lowStockCount);
        report.setOutOfStockItems(outOfStockCount);
        report.setTotalStockValue(totalStockValue);
        
        // Set the lists - these will be used by the JSP
        report.setLowStockList(lowStockList);
        
        // Add fullStockList as a custom property since ReportDTO doesn't have this field
        // We'll use stockItems or add it to request attributes separately
        request.setAttribute("fullStockList", fullStockList);
        
        // Stock by category
        List<Map<String, Object>> stockByCategory = new ArrayList<>();
        List<Category> categories = categoryDAO.getActiveCategories();
        
        for (Category category : categories) {
            Map<String, Object> catData = new HashMap<>();
            catData.put("category", category.getCategoryName());
            
            // Get items for this category
            List<Item> categoryItems = itemDAO.getItemsByCategory(category.getCategoryId());
            int itemCount = categoryItems.size();
            double categoryStockValue = 0.0;
            
            for (Item item : categoryItems) {
                categoryStockValue += (item.getSellingPrice() * item.getQuantityInStock());
            }
            
            catData.put("itemCount", itemCount);
            catData.put("stockValue", categoryStockValue);
            stockByCategory.add(catData);
        }
        report.setStockByCategory(stockByCategory);
        
        return report;
    }
    
    /**
     * Helper method to get category name
     */
    private String getCategoryName(int categoryId) {
        try {
            Category category = categoryDAO.getCategoryById(categoryId);
            return category != null ? category.getCategoryName() : "Unknown";
        } catch (Exception e) {
            return "Unknown";
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    /**
     * Export report to CSV
     */
    private void exportToCSV(HttpServletRequest request, HttpServletResponse response, ReportDTO report) 
            throws IOException {
        
        StringBuilder csv = new StringBuilder();
        
        // Header
        csv.append("Complete Stock Report\n");
        csv.append("Generated: ").append(new Date()).append("\n\n");
        
        // Summary
        csv.append("Summary\n");
        csv.append("Total Items,").append(report.getTotalItems()).append("\n");
        csv.append("Low Stock Items,").append(report.getLowStockItems()).append("\n");
        csv.append("Out of Stock Items,").append(report.getOutOfStockItems()).append("\n");
        csv.append("Total Stock Value,").append(report.getTotalStockValue()).append("\n\n");
        
        // Stock by category
        if (report.getStockByCategory() != null && !report.getStockByCategory().isEmpty()) {
            csv.append("Stock by Category\n");
            csv.append("Category,Item Count,Stock Value\n");
            
            for (Map<String, Object> cat : report.getStockByCategory()) {
                csv.append(cat.get("category")).append(",");
                csv.append(cat.get("itemCount")).append(",");
                csv.append(cat.get("stockValue")).append("\n");
            }
            csv.append("\n");
        }
        
        // All items
        List<Map<String, Object>> fullStockList = (List<Map<String, Object>>) request.getAttribute("fullStockList");
        if (fullStockList != null && !fullStockList.isEmpty()) {
            csv.append("All Items\n");
            csv.append("Item Code,Item Name,Category,Current Stock,Reorder Level,Unit Price,Stock Value\n");
            
            for (Map<String, Object> item : fullStockList) {
                csv.append(item.get("itemCode")).append(",");
                csv.append(item.get("itemName")).append(",");
                csv.append(item.get("category")).append(",");
                csv.append(item.get("currentStock")).append(",");
                csv.append(item.get("reorderLevel")).append(",");
                csv.append(item.get("unitPrice")).append(",");
                csv.append(item.get("stockValue")).append("\n");
            }
            csv.append("\n");
        }
        
        // Low stock items
        if (report.getLowStockList() != null && !report.getLowStockList().isEmpty()) {
            csv.append("Low Stock Items\n");
            csv.append("Item Code,Item Name,Current Stock,Reorder Level\n");
            
            for (Map<String, Object> item : report.getLowStockList()) {
                csv.append(item.get("itemCode")).append(",");
                csv.append(item.get("itemName")).append(",");
                csv.append(item.get("currentStock")).append(",");
                csv.append(item.get("reorderLevel")).append("\n");
            }
        }
        
        // Set response headers
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", 
            "attachment; filename=\"stock-report-" + 
            new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".csv\"");
        
        // Write CSV
        response.getWriter().write(csv.toString());
    }
    
    @Override
    public String getServletInfo() {
        return "Stock Report Controller - Generates combined stock and inventory reports";
    }
}