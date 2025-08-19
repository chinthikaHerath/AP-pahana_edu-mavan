package com.pahanaedu.service.impl;

import com.pahanaedu.service.interfaces.ReportService;
import com.pahanaedu.dao.interfaces.*;
import com.pahanaedu.dao.impl.*;
import com.pahanaedu.dto.ReportDTO;
import com.pahanaedu.dto.DashboardDTO;
import com.pahanaedu.exception.DatabaseException;
import com.pahanaedu.exception.BusinessException;
import com.pahanaedu.util.DateUtil;
import com.pahanaedu.model.Bill;
import com.pahanaedu.model.Item;
import com.pahanaedu.model.Category;

import java.util.*;
import java.text.SimpleDateFormat;

/**
 * Implementation of ReportService interface
 * Handles business logic for generating various reports
 */
public class ReportServiceImpl implements ReportService {
    
    private CustomerDAO customerDAO;
    private UserDAO userDAO;
    private ItemDAO itemDAO;
    private BillDAO billDAO;
    private CategoryDAO categoryDAO;
    private BillItemDAO billItemDAO;
    
    public ReportServiceImpl() {
        this.customerDAO = new CustomerDAOImpl();
        this.userDAO = new UserDAOImpl();
        this.itemDAO = new ItemDAOImpl();
        this.billDAO = new BillDAOImpl();
        this.categoryDAO = new CategoryDAOImpl();
        this.billItemDAO = new BillItemDAOImpl();
    }
    
    @Override
    public ReportDTO generateSalesReport(Date startDate, Date endDate) 
            throws DatabaseException, BusinessException {
        
        validateDateRange(startDate, endDate);
        
        // Create report with appropriate title
        String reportTitle = "Sales Report";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        
        // Check if it's a single day report
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(startDate);
        cal2.setTime(endDate);
        
        boolean isSameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                           cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        
        if (isSameDay) {
            reportTitle = "Daily Sales Report - " + sdf.format(startDate);
        } else {
            // Check if it's a full month
            cal1.setTime(startDate);
            cal2.setTime(endDate);
            if (cal1.get(Calendar.DAY_OF_MONTH) == 1 && 
                cal2.get(Calendar.DAY_OF_MONTH) == cal2.getActualMaximum(Calendar.DAY_OF_MONTH) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
                reportTitle = "Monthly Sales Report - " + new SimpleDateFormat("MMMM yyyy").format(startDate);
            } else {
                reportTitle = "Sales Report (" + sdf.format(startDate) + " to " + sdf.format(endDate) + ")";
            }
        }
        
        ReportDTO report = new ReportDTO(reportTitle, "SALES");
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        
        // Get all bills for the date range
        List<Bill> bills = billDAO.getBillsByDateRange(
            DateUtil.getStartOfDay(startDate), 
            DateUtil.getEndOfDay(endDate)
        );
        
        // Filter only PAID bills for statistics
        List<Bill> paidBills = new ArrayList<>();
        for (Bill bill : bills) {
            if ("PAID".equals(bill.getPaymentStatus())) {
                paidBills.add(bill);
            }
        }
        
        // Calculate statistics
        double totalSales = 0.0;
        Set<Integer> uniqueCustomers = new HashSet<>();
        
        for (Bill bill : paidBills) {
            totalSales += bill.getTotalAmount();
            uniqueCustomers.add(bill.getCustomerId());
        }
        
        report.setTotalSales(totalSales);
        report.setTotalBills(paidBills.size());
        report.setTotalCustomers(uniqueCustomers.size());
        report.setAverageBillValue(paidBills.size() > 0 ? totalSales / paidBills.size() : 0.0);
        
        // Get sales breakdown by payment method
        Map<String, Double> paymentBreakdown = getSalesBreakdownByPaymentMethod(startDate, endDate);
        List<Map<String, Object>> breakdownList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : paymentBreakdown.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("method", entry.getKey());
            item.put("amount", entry.getValue());
            breakdownList.add(item);
        }
        report.setSalesByCategory(breakdownList); // Reusing this field for payment breakdown
        
        // For monthly reports, add daily breakdown
        if (!isSameDay) {
            List<Map<String, Object>> dailyBreakdown = getDailySalesBreakdown(startDate, endDate);
            report.setChartLabels(new ArrayList<>());
            report.setChartData(new ArrayList<>());
            
            for (Map<String, Object> daily : dailyBreakdown) {
                report.getChartLabels().add((String) daily.get("date"));
                report.getChartData().add((Double) daily.get("sales"));
            }
            
            // Store daily breakdown for table display
            report.setTopSellingItems(dailyBreakdown); // Reusing this field for daily breakdown
        }
        
        return report;
    }
    
    @Override
    public Map<String, Double> getSalesBreakdownByPaymentMethod(Date startDate, Date endDate) 
            throws DatabaseException {
        
        Map<String, Double> breakdown = new LinkedHashMap<>();
        breakdown.put("CASH", 0.0);
        breakdown.put("CARD", 0.0);
        breakdown.put("CHEQUE", 0.0);
        breakdown.put("BANK_TRANSFER", 0.0);
        
        List<Bill> bills = billDAO.getBillsByDateRange(
            DateUtil.getStartOfDay(startDate), 
            DateUtil.getEndOfDay(endDate)
        );
        
        for (Bill bill : bills) {
            if ("PAID".equals(bill.getPaymentStatus())) {
                String method = bill.getPaymentMethod();
                if (method != null && breakdown.containsKey(method)) {
                    breakdown.put(method, breakdown.get(method) + bill.getTotalAmount());
                }
            }
        }
        
        return breakdown;
    }
    
    @Override
    public List<Map<String, Object>> getDailySalesBreakdown(Date startDate, Date endDate) 
            throws DatabaseException {
        
        List<Map<String, Object>> dailyBreakdown = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
        
        while (!cal.getTime().after(endDate)) {
            Date currentDate = cal.getTime();
            
            // Get sales for this day
            Map<String, Object> dailySummary = billDAO.getDailySalesSummary(currentDate);
            
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", sdf.format(currentDate));
            dayData.put("fullDate", DateUtil.formatDate(currentDate));
            dayData.put("bills", dailySummary.get("billCount"));
            dayData.put("sales", dailySummary.get("totalSales"));
            
            dailyBreakdown.add(dayData);
            
            // Move to next day
            cal.add(Calendar.DATE, 1);
        }
        
        return dailyBreakdown;
    }
    
    @Override
    public ReportDTO generateStockReport() throws DatabaseException {
        ReportDTO report = new ReportDTO("Stock Report", "STOCK");
        report.setGeneratedDate(new Date());
        
        // Get item statistics
        report.setTotalItems(itemDAO.getTotalItemCount());
        report.setLowStockItems(itemDAO.getLowStockItems().size());
        report.setOutOfStockItems(itemDAO.getOutOfStockItems().size());
        
        // Calculate total stock value
        List<Item> allItems = itemDAO.getAllActiveItems();
        double totalStockValue = 0.0;
        for (Item item : allItems) {
            totalStockValue += (item.getSellingPrice() * item.getQuantityInStock());
        }
        report.setTotalStockValue(totalStockValue);
        
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
    
    @Override
    public ReportDTO generateLowStockReport(int threshold) throws DatabaseException {
        if (threshold <= 0) {
            threshold = 10; // Default threshold
        }
        
        ReportDTO report = new ReportDTO("Low Stock Report", "LOW_STOCK");
        report.setGeneratedDate(new Date());
        
        // Get low stock items
        List<Item> lowStockItems = itemDAO.getLowStockItems();
        List<Map<String, Object>> lowStockList = new ArrayList<>();
        
        for (Item item : lowStockItems) {
            if (item.getQuantityInStock() <= threshold) {
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("itemCode", item.getItemCode());
                itemData.put("itemName", item.getItemName());
                itemData.put("currentStock", item.getQuantityInStock());
                itemData.put("reorderLevel", item.getReorderLevel());
                lowStockList.add(itemData);
            }
        }
        
        report.setLowStockList(lowStockList);
        report.setLowStockItems(lowStockList.size());
        
        return report;
    }
    
    @Override
    public ReportDTO generateCustomerReport(Date startDate, Date endDate) 
            throws DatabaseException {
        
        ReportDTO report = new ReportDTO("Customer Report", "CUSTOMER");
        report.setStartDate(startDate);
        report.setEndDate(endDate);
        
        // Get actual customer data
        int totalCustomers = customerDAO.getTotalCustomerCount();
        int activeCustomers = customerDAO.getActiveCustomerCount();
        
        report.setTotalCustomers(totalCustomers);
        report.setActiveCustomers(activeCustomers);
        report.setInactiveCustomers(totalCustomers - activeCustomers);
        
        // Calculate new customers this month
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date monthStart = cal.getTime();
        
        List<com.pahanaedu.model.Customer> allCustomers = customerDAO.getAllCustomers();
        int newCustomersThisMonth = 0;
        
        for (com.pahanaedu.model.Customer customer : allCustomers) {
            Date regDate = customer.getRegistrationDate();
            if (regDate != null && regDate.after(monthStart)) {
                newCustomersThisMonth++;
            }
        }
        
        report.setNewCustomers(newCustomersThisMonth);
        
        // BETTER APPROACH: Get customers by city - only show cities with actual customers
        List<Map<String, Object>> customersByCity = new ArrayList<>();
        
        // Get unique cities from existing customers (simple approach without new DAO method)
        Map<String, Integer> cityCount = new HashMap<>();
        
        for (com.pahanaedu.model.Customer customer : allCustomers) {
            String city = customer.getCity();
            if (city != null && !city.trim().isEmpty()) {
                cityCount.put(city, cityCount.getOrDefault(city, 0) + 1);
            }
        }
        
        // Convert to the format needed for the report
        for (Map.Entry<String, Integer> entry : cityCount.entrySet()) {
            Map<String, Object> cityData = new HashMap<>();
            cityData.put("city", entry.getKey());
            cityData.put("customerCount", entry.getValue());
            cityData.put("percentage", totalCustomers > 0 ? 
                (entry.getValue() * 100.0 / totalCustomers) : 0);
            customersByCity.add(cityData);
        }
        
        // Sort by customer count (highest first) for better presentation
        customersByCity.sort((a, b) -> 
            Integer.compare((Integer) b.get("customerCount"), (Integer) a.get("customerCount")));
        
        report.setCustomersByCity(customersByCity);
        
        // Get top customers based on purchase history - keep as original
        List<com.pahanaedu.model.Customer> allActiveCustomers = customerDAO.getActiveCustomers();
        List<Map<String, Object>> topCustomers = new ArrayList<>();
        
        // Sort customers by total purchases
        Map<Integer, Double> customerPurchases = new HashMap<>();
        Map<Integer, Integer> customerBillCounts = new HashMap<>();
        
        for (com.pahanaedu.model.Customer customer : allActiveCustomers) {
            Map<String, Object> purchaseSummary = billDAO.getCustomerPurchaseSummary(customer.getCustomerId());
            double totalSpent = (Double) purchaseSummary.get("totalSpent");
            int billCount = (Integer) purchaseSummary.get("totalBills");
            
            if (totalSpent > 0) {
                customerPurchases.put(customer.getCustomerId(), totalSpent);
                customerBillCounts.put(customer.getCustomerId(), billCount);
            }
        }
        
        // Sort by total purchases and get top 5
        List<Map.Entry<Integer, Double>> sortedCustomers = new ArrayList<>(customerPurchases.entrySet());
        sortedCustomers.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
        
        int count = 0;
        for (Map.Entry<Integer, Double> entry : sortedCustomers) {
            if (count >= 5) break;
            
            com.pahanaedu.model.Customer customer = customerDAO.getCustomerById(entry.getKey());
            Map<String, Object> custData = new HashMap<>();
            custData.put("customerId", customer.getCustomerId());
            custData.put("customerName", customer.getCustomerName());
            custData.put("accountNumber", customer.getAccountNumber());
            custData.put("totalPurchases", entry.getValue());
            custData.put("billCount", customerBillCounts.get(entry.getKey()));
            topCustomers.add(custData);
            count++;
        }
        
        report.setTopCustomers(topCustomers);
        
        return report;
    }
    
 // Replace the existing generateDashboardStatistics() method in ReportServiceImpl.java with this simplified version

    @Override
    public DashboardDTO generateDashboardStatistics() throws DatabaseException {
        DashboardDTO dashboard = new DashboardDTO();
        
        // User statistics (keep if needed for other purposes)
        dashboard.setTotalUsers(userDAO.getTotalUserCount());
        dashboard.setActiveUsers(userDAO.getActiveUserCount());
        
        // Customer statistics - Essential for summary cards
        dashboard.setTotalCustomers(customerDAO.getTotalCustomerCount());
        dashboard.setActiveCustomers(customerDAO.getActiveCustomerCount());
        
        // Calculate new customers today (simplified)
        List<com.pahanaedu.model.Customer> allCustomers = customerDAO.getAllCustomers();
        int newCustomersToday = 0;
        
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        
        for (com.pahanaedu.model.Customer customer : allCustomers) {
            if (customer.getCreatedAt() != null && customer.getCreatedAt().after(today.getTime())) {
                newCustomersToday++;
            }
        }
        dashboard.setNewCustomersToday(newCustomersToday);
        
        // Item statistics - Essential for summary cards
        dashboard.setTotalItems(itemDAO.getTotalItemCount());
        dashboard.setActiveItems(itemDAO.getActiveItemCount());
        dashboard.setLowStockItems(itemDAO.getLowStockItems().size());
        dashboard.setOutOfStockItems(itemDAO.getOutOfStockItems().size());
        
        // Today's sales statistics - Essential for summary cards
        List<Bill> todaysBills = billDAO.getTodaysBills();
        dashboard.setTodaysBills(todaysBills.size());
        dashboard.setTodaysSales(billDAO.getTodaysSalesTotal());
        
        // Calculate average bill value for today
        dashboard.setAverageBillValue(todaysBills.size() > 0 ? 
            dashboard.getTodaysSales() / todaysBills.size() : 0.0);
        
        // Monthly statistics (optional - for "Today's Performance" section)
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date monthStart = cal.getTime();
        
        List<Bill> monthlyBills = billDAO.getBillsByDateRange(monthStart, new Date());
        double monthlySales = 0.0;
        for (Bill bill : monthlyBills) {
            if ("PAID".equals(bill.getPaymentStatus())) {
                monthlySales += bill.getTotalAmount();
            }
        }
        dashboard.setMonthlyBills(monthlyBills.size());
        dashboard.setMonthlySales(monthlySales);
        
        // Set empty lists to avoid null pointer exceptions if JSP still references them
        dashboard.setRecentBills(new ArrayList<>());
        dashboard.setRecentCustomers(new ArrayList<>());
        dashboard.setSalesChartLabels(new ArrayList<>());
        dashboard.setSalesChartData(new ArrayList<>());
        dashboard.setLowStockAlerts(new ArrayList<>());
        
        return dashboard;
    }
    
    @Override
    public String exportReportToCSV(ReportDTO report) {
        StringBuilder csv = new StringBuilder();
        
        // Header
        csv.append("Report: ").append(report.getReportTitle()).append("\n");
        csv.append("Generated: ").append(new Date()).append("\n");
        if (report.getStartDate() != null && report.getEndDate() != null) {
            csv.append("Period: ").append(DateUtil.formatDate(report.getStartDate()))
               .append(" to ").append(DateUtil.formatDate(report.getEndDate())).append("\n");
        }
        csv.append("\n");
        
        // Content based on report type
        if ("SALES".equals(report.getReportType())) {
            csv.append("Summary\n");
            csv.append("Total Sales,").append(report.getTotalSales()).append("\n");
            csv.append("Total Bills,").append(report.getTotalBills()).append("\n");
            csv.append("Customers Served,").append(report.getTotalCustomers()).append("\n");
            csv.append("Average Bill Value,").append(report.getAverageBillValue()).append("\n\n");
            
            // Payment method breakdown
            if (report.getSalesByCategory() != null) {
                csv.append("Payment Method Breakdown\n");
                csv.append("Method,Amount\n");
                for (Map<String, Object> payment : report.getSalesByCategory()) {
                    csv.append(payment.get("method")).append(",");
                    csv.append(payment.get("amount")).append("\n");
                }
                csv.append("\n");
            }
            
            // Daily breakdown for monthly reports
            if (report.getTopSellingItems() != null) {
                csv.append("Daily Breakdown\n");
                csv.append("Date,Bills,Sales\n");
                for (Map<String, Object> daily : report.getTopSellingItems()) {
                    csv.append(daily.get("fullDate")).append(",");
                    csv.append(daily.get("bills")).append(",");
                    csv.append(daily.get("sales")).append("\n");
                }
            }
        } else if ("STOCK".equals(report.getReportType())) {
            csv.append("Total Items,").append(report.getTotalItems()).append("\n");
            csv.append("Low Stock Items,").append(report.getLowStockItems()).append("\n");
            csv.append("Out of Stock Items,").append(report.getOutOfStockItems()).append("\n");
            csv.append("Total Stock Value,").append(report.getTotalStockValue()).append("\n");
        } else if ("CUSTOMER".equals(report.getReportType())) {
            csv.append("Total Customers,").append(report.getTotalCustomers()).append("\n");
            csv.append("Active Customers,").append(report.getActiveCustomers()).append("\n");
            csv.append("Inactive Customers,").append(report.getInactiveCustomers()).append("\n");
        }
        
        return csv.toString();
    }
    
    @Override
    public void validateDateRange(Date startDate, Date endDate) throws BusinessException {
        if (startDate == null || endDate == null) {
            throw new BusinessException("Start date and end date are required");
        }
        
        if (startDate.after(endDate)) {
            throw new BusinessException("Start date cannot be after end date");
        }
        
        // Check if date range is not too large (e.g., more than 1 year)
        long diffInMillis = endDate.getTime() - startDate.getTime();
        long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);
        
        if (diffInDays > 365) {
            throw new BusinessException("Date range cannot exceed 365 days");
        }
    }
}