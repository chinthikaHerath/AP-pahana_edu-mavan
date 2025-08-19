package com.pahanaedu.controller.billing;

import com.pahanaedu.service.interfaces.BillingService;
import com.pahanaedu.service.interfaces.CustomerService;
import com.pahanaedu.service.interfaces.ItemService;
import com.pahanaedu.service.impl.BillingServiceImpl;
import com.pahanaedu.service.impl.CustomerServiceImpl;
import com.pahanaedu.service.impl.ItemServiceImpl;
import com.pahanaedu.model.Bill;
import com.pahanaedu.model.BillItem;
import com.pahanaedu.model.Customer;
import com.pahanaedu.model.Item;
import com.pahanaedu.dto.BillDTO;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.exception.BusinessException;
import com.pahanaedu.constant.SystemConstants;
import com.pahanaedu.constant.PaymentMethod;
import com.pahanaedu.util.SessionUtil;
import com.pahanaedu.util.ValidationUtil;
import com.pahanaedu.dto.ItemDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet for creating new bills
 */
@WebServlet(name = "CreateBillController", urlPatterns = {"/bill/create", "/billing/new"})
public class CreateBillController extends HttpServlet {
    
    private BillingService billingService;
    private CustomerService customerService;
    private ItemService itemService;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        super.init();
        billingService = new BillingServiceImpl();
        customerService = new CustomerServiceImpl();
        itemService = new ItemServiceImpl();
        gson = new Gson();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            // Load data for the form
            List<Customer> activeCustomers = customerService.getActiveCustomers();
            List<Item> activeItems = itemService.getActiveItems();
            String nextBillNumber = billingService.getNextBillNumber();
            
            // Get payment methods for dropdown
            PaymentMethod[] paymentMethods = PaymentMethod.values();
            
            
            // Check if customer is pre-selected
            String customerId = request.getParameter("customerId");
            if (customerId != null && !customerId.isEmpty()) {
                try {
                    int custId = Integer.parseInt(customerId);
                    Customer selectedCustomer = customerService.getCustomerById(custId);
                    request.setAttribute("selectedCustomer", selectedCustomer);
                } catch (Exception e) {
                    log("Error loading customer: " + e.getMessage(), e);
                }
            }
            
            // Set attributes
            request.setAttribute("customers", activeCustomers);
            request.setAttribute("items", activeItems);
            request.setAttribute("nextBillNumber", nextBillNumber);
            request.setAttribute("paymentMethods", paymentMethods);
            request.setAttribute("defaultTaxRate", SystemConstants.DEFAULT_TAX_RATE);
            
            // Forward to create bill JSP
            request.getRequestDispatcher("/views/billing/create-bill.jsp").forward(request, response);
            
        } catch (Exception e) {
            log("Error loading create bill page: " + e.getMessage(), e);
            SessionUtil.setErrorMessage(session, "Error loading billing page: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("ajax".equals(action)) {
            handleAjaxRequest(request, response);
        } else {
            handleFormSubmit(request, response);
        }
    }
    
    /**
     * Handle regular form submission
     */
    private void handleFormSubmit(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        int userId = SessionUtil.getLoggedInUserId(session);
        
        try {
            // Create Bill object from form data
            Bill bill = extractBillFromRequest(request);
            
            // Create bill through service
            int billId = billingService.createBill(bill, userId);
            
            // Set success message
            SessionUtil.setSuccessMessage(session, 
                "Bill created successfully! Bill Number: " + bill.getBillNumber());
            
            // Redirect based on submit button
            String submitAction = request.getParameter("submitAction");
            if ("saveAndPrint".equals(submitAction)) {
                response.sendRedirect(request.getContextPath() + "/bill/print?id=" + billId);
            } else if ("saveAndNew".equals(submitAction)) {
                response.sendRedirect(request.getContextPath() + "/bill/create");
            } else {
                response.sendRedirect(request.getContextPath() + "/bill/view?id=" + billId);
            }
            
        } catch (ValidationException e) {
            // Set validation errors
            request.setAttribute("validationErrors", e.getFieldErrors());
            request.setAttribute("errorMessage", "Please correct the errors below");
            
            // Reload form data
            reloadFormData(request);
            
            // Forward back to form
            request.getRequestDispatcher("/views/billing/create-bill.jsp").forward(request, response);
            
        } catch (BusinessException e) {
            // Set business error
            request.setAttribute("errorMessage", e.getMessage());
            
            // Reload form data
            reloadFormData(request);
            
            // Forward back to form
            request.getRequestDispatcher("/views/billing/create-bill.jsp").forward(request, response);
            
        } catch (Exception e) {
            log("Error creating bill: " + e.getMessage(), e);
            SessionUtil.setErrorMessage(session, "Error creating bill: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/billing");
        }
    }
    
    /**
     * Handle AJAX requests
     */
    private void handleAjaxRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();
        
        String ajaxAction = request.getParameter("ajaxAction");
        
        try {
            switch (ajaxAction) {
                case "getCustomer":
                    handleGetCustomer(request, jsonResponse);
                    break;
                    
                case "getItem":
                    handleGetItem(request, jsonResponse);
                    break;
                    
                case "calculateTotals":
                    handleCalculateTotals(request, jsonResponse);
                    break;
                    
                case "checkStock":
                    handleCheckStock(request, jsonResponse);
                    break;
                    
                case "saveDraft":
                    handleSaveDraft(request, jsonResponse);
                    break;
                    
                default:
                    jsonResponse.addProperty("success", false);
                    jsonResponse.addProperty("error", "Unknown action: " + ajaxAction);
            }
            
        } catch (Exception e) {
            log("Error in AJAX request: " + e.getMessage(), e);
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("error", e.getMessage());
        }
        
        out.print(jsonResponse.toString());
        out.flush();
    }
    
    /**
     * Handle get customer details AJAX request
     */
    private void handleGetCustomer(HttpServletRequest request, JsonObject response) 
            throws Exception {
        
        int customerId = Integer.parseInt(request.getParameter("customerId"));
        Customer customer = customerService.getCustomerById(customerId);
        
        if (customer != null) {
            response.addProperty("success", true);
            response.add("customer", gson.toJsonTree(customer));
            
            // Check if customer can be billed
            boolean canBeBilled = billingService.canCustomerBeBilled(customerId);
            response.addProperty("canBeBilled", canBeBilled);
            
            if (!canBeBilled) {
                response.addProperty("message", "Customer has too many pending bills");
            }
        } else {
            response.addProperty("success", false);
            response.addProperty("error", "Customer not found");
        }
    }
    
    /**
     * Handle get item details AJAX request
     */
    private void handleGetItem(HttpServletRequest request, JsonObject response) 
            throws Exception {
        
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        Item item = itemService.getItemById(itemId);
        
        if (item != null) {
            response.addProperty("success", true);
            response.add("item", gson.toJsonTree(item));
            response.addProperty("availableStock", item.getQuantityInStock());
        } else {
            response.addProperty("success", false);
            response.addProperty("error", "Item not found");
        }
    }
    
    /**
     * Handle calculate totals AJAX request
     */
    private void handleCalculateTotals(HttpServletRequest request, JsonObject response) 
            throws Exception {
        
        // Parse bill data from JSON
        String billDataJson = request.getParameter("billData");
        BillDTO billDTO = gson.fromJson(billDataJson, BillDTO.class);
        
        // Recalculate totals
        billDTO.recalculateTotals();
        
        response.addProperty("success", true);
        response.addProperty("subtotal", billDTO.getSubtotal());
        response.addProperty("discountAmount", billDTO.getDiscountAmount());
        response.addProperty("taxAmount", billDTO.getTaxAmount());
        response.addProperty("totalAmount", billDTO.getTotalAmount());
        response.addProperty("totalSavings", billDTO.getTotalSavings());
    }
    
    /**
     * Handle check stock AJAX request
     */
    private void handleCheckStock(HttpServletRequest request, JsonObject response) 
            throws Exception {
        
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        
        boolean isAvailable = billingService.isItemAvailable(itemId, quantity);
        Item item = itemService.getItemById(itemId);
        
        response.addProperty("success", true);
        response.addProperty("isAvailable", isAvailable);
        response.addProperty("currentStock", item != null ? item.getQuantityInStock() : 0);
        
        if (!isAvailable) {
            response.addProperty("message", "Insufficient stock. Available: " + 
                (item != null ? item.getQuantityInStock() : 0));
        }
    }
    
    /**
     * Handle save draft AJAX request
     */
    private void handleSaveDraft(HttpServletRequest request, JsonObject response) 
            throws Exception {
        
        // This is a placeholder for draft functionality
        // In a real implementation, you might save to session or a draft table
        
        String billDataJson = request.getParameter("billData");
        HttpSession session = request.getSession();
        session.setAttribute("billDraft", billDataJson);
        
        response.addProperty("success", true);
        response.addProperty("message", "Draft saved successfully");
    }
    
    /**
     * Extract Bill object from request
     */
    private Bill extractBillFromRequest(HttpServletRequest request) 
            throws ValidationException {
        
        Bill bill = new Bill();
        
        // Customer information
        String customerIdStr = request.getParameter("customerId");
        if (ValidationUtil.isNullOrEmpty(customerIdStr)) {
            throw new ValidationException("Customer is required");
        }
        bill.setCustomerId(Integer.parseInt(customerIdStr));
        
        // Payment information
        bill.setPaymentMethod(request.getParameter("paymentMethod"));
        bill.setPaymentStatus(request.getParameter("paymentStatus"));
        
        // Discount and tax
        String discountStr = request.getParameter("discountPercentage");
        if (!ValidationUtil.isNullOrEmpty(discountStr)) {
            bill.setDiscountPercentage(Double.parseDouble(discountStr));
        }
        
        String taxStr = request.getParameter("taxPercentage");
        if (!ValidationUtil.isNullOrEmpty(taxStr)) {
            bill.setTaxPercentage(Double.parseDouble(taxStr));
        }
        
        // Notes
        bill.setNotes(request.getParameter("notes"));
        
        // Extract bill items
        List<BillItem> billItems = extractBillItems(request);
        if (billItems.isEmpty()) {
            throw new ValidationException("At least one item is required");
        }
        bill.setBillItems(billItems);
        
        return bill;
    }
    
    /**
     * Extract bill items from request
     */
    private List<BillItem> extractBillItems(HttpServletRequest request) 
            throws ValidationException {
        
        List<BillItem> billItems = new ArrayList<>();
        
        // Get item arrays from request
        String[] itemIds = request.getParameterValues("itemId[]");
        String[] quantities = request.getParameterValues("quantity[]");
        String[] unitPrices = request.getParameterValues("unitPrice[]");
        String[] discounts = request.getParameterValues("itemDiscount[]");
        
        if (itemIds == null || itemIds.length == 0) {
            return billItems;
        }
        
        // Create bill items
        for (int i = 0; i < itemIds.length; i++) {
            if (ValidationUtil.isNullOrEmpty(itemIds[i])) {
                continue;
            }
            
            BillItem billItem = new BillItem();
            billItem.setItemId(Integer.parseInt(itemIds[i]));
            
            // Quantity
            if (quantities != null && i < quantities.length && !ValidationUtil.isNullOrEmpty(quantities[i])) {
                int qty = Integer.parseInt(quantities[i]);
                if (qty <= 0) {
                    throw new ValidationException("Quantity must be positive for all items");
                }
                billItem.setQuantity(qty);
            } else {
                billItem.setQuantity(1);
            }
            
            // Unit price
            if (unitPrices != null && i < unitPrices.length && !ValidationUtil.isNullOrEmpty(unitPrices[i])) {
                billItem.setUnitPrice(Double.parseDouble(unitPrices[i]));
            }
            
            // Discount
            if (discounts != null && i < discounts.length && !ValidationUtil.isNullOrEmpty(discounts[i])) {
                double discount = Double.parseDouble(discounts[i]);
                if (discount < 0 || discount > 100) {
                    throw new ValidationException("Item discount must be between 0 and 100");
                }
                billItem.setDiscountPercentage(discount);
            }
            
            billItems.add(billItem);
        }
        
        return billItems;
    }
    
    /**
     * Reload form data after error
     */
    private void reloadFormData(HttpServletRequest request) {
        try {
            List<Customer> activeCustomers = customerService.getActiveCustomers();
            List<Item> activeItems = itemService.getActiveItems();
            String nextBillNumber = billingService.getNextBillNumber();
            PaymentMethod[] paymentMethods = PaymentMethod.values();
            
            request.setAttribute("customers", activeCustomers);
            request.setAttribute("items", activeItems);
            request.setAttribute("nextBillNumber", nextBillNumber);
            request.setAttribute("paymentMethods", paymentMethods);
            request.setAttribute("defaultTaxRate", SystemConstants.DEFAULT_TAX_RATE);
            
            // Preserve form data
            preserveFormData(request);
            
        } catch (Exception e) {
            log("Error reloading form data: " + e.getMessage(), e);
        }
    }
    
    /**
     * Preserve form data for redisplay
     */
    private void preserveFormData(HttpServletRequest request) {
        // The form data is already in request parameters
        // JSP will use these to repopulate the form
    }
    
    @Override
    public String getServletInfo() {
        return "Create Bill Controller - Handles new bill creation";
    }
}