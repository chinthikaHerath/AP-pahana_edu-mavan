<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.pahanaedu.util.SessionUtil" %>
<%@ page import="com.pahanaedu.model.User" %>
<%
    User currentUser = SessionUtil.getLoggedInUser(session);
    boolean canManageItems = currentUser != null && 
        ("ADMIN".equals(currentUser.getRole()) || "MANAGER".equals(currentUser.getRole()));
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Pahana Edu Online Billing System</title>
    
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    
    <style>
        :root {
            --primary-dark: #049B55;
            --primary: #325788;
            --primary-medium: #55F989;
            --primary-light: #84FEBD;
            --primary-lighter: #D7FFEB;
            --primary-pale: #EAFFF2;
            --primary-bg: #f5f3ff;
            --cream: #fefbff;
            --accent-green: #10b981;
            --accent-orange: #ea580c;
            --accent-red: #dc2626;
            --accent-blue: #0284c7;
            --accent-yellow: #ca8a04;
        }
        
        body {
            background-color: var(--primary-bg) !important;
            color: var(--primary-dark);
        }
        
        .navbar-ocean {
            background: linear-gradient(135deg, var(--primary-dark) 0%, var(--primary) 100%) !important;
            box-shadow: 0 2px 10px rgba(124, 58, 237, 0.15);
        }
        
        .bg-primary {
            background-color: var(--primary) !important;
        }
        
        .bg-primary-medium {
            background-color: var(--primary-medium) !important;
        }
        
        .bg-primary-light {
            background-color: var(--primary-light) !important;
        }
        
        .bg-primary-pale {
            background-color: var(--primary-pale) !important;
        }
        
        .bg-cream {
            background-color: var(--cream) !important;
        }
        
        .text-primary-dark {
            color: #0C1610 !important;
        }
        
        .text-primary-medium {
            color: #1C2620 !important;
        }
        
        .text-primary-light {
            color: var(--primary-light) !important;
        }
        
        .card-stat {
            border: none;
            border-left: 4px solid;
            transition: all 0.3s ease;
            background: white;
            box-shadow: 0 2px 10px rgba(124, 58, 237, 0.08);
        }
        
        .card-stat:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(124, 58, 237, 0.15);
        }
        
        .card-stat-customers {
            border-left-color: var(--primary);
            background: linear-gradient(135deg, white 0%, var(--cream) 100%);
        }
        
        .card-stat-items {
            border-left-color: var(--accent-green);
            background: linear-gradient(135deg, white 0%, #ecfdf5 100%);
        }
        
        .card-stat-sales {
            border-left-color: var(--accent-orange);
            background: linear-gradient(135deg, white 0%, #fff7ed 100%);
        }
        
        .card-stat-alerts {
            border-left-color: var(--accent-red);
            background: linear-gradient(135deg, white 0%, #fef2f2 100%);
        }
        
        .icon-circle {
            width: 45px;
            height: 45px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            box-shadow: 0 2px 8px rgba(124, 58, 237, 0.1);
        }
        
        .border-primary {
            border-color: var(--primary-pale) !important;
        }
        
        .welcome-section {
            background: linear-gradient(135deg, var(--primary-medium) 0%, var(--primary-light) 100%);
            color: white;
        }
        
        .action-card {
            background: white;
            border: none;
            transition: all 0.3s ease;
            overflow: hidden;
            position: relative;
            box-shadow: 0 4px 15px rgba(124, 58, 237, 0.08);
        }
        
        .action-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 15px 40px rgba(124, 58, 237, 0.2) !important;
        }
        
        .action-card .icon-box {
            width: 60px;
            height: 60px;
            border-radius: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 1rem;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        }
        
        .action-card-bill {
            border-top: 3px solid var(--primary);
        }
        
        .action-card-bill .icon-box {
            background: linear-gradient(135deg, var(--primary) 0%, var(--primary-medium) 100%);
        }
        
        .action-card-customer {
            border-top: 3px solid var(--accent-green);
        }
        
        .action-card-customer .icon-box {
            background: linear-gradient(135deg, var(--accent-green) 0%, #10b981 100%);
        }
        
        .action-card-item {
            border-top: 3px solid var(--accent-orange);
        }
        
        .action-card-item .icon-box {
            background: linear-gradient(135deg, var(--accent-orange) 0%, #f59e0b 100%);
        }
        
        .action-link {
            text-decoration: none;
            color: inherit;
        }
        
        .action-link:hover {
            color: inherit;
        }
        
        /* Badge Styling */
        .badge {
            padding: 0.4rem 0.8rem;
            font-weight: 600;
            border-radius: 20px;
        }
        
        /* Alert Styling */
        .alert {
            border-radius: 12px;
            border: none;
        }
        
        /* Enhanced Visual Effects */
        .stat-number {
            background: linear-gradient(135deg, var(--primary-dark), var(--primary));
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            font-weight: 800;
        }
        
        /* Responsive adjustments for staff view */
        .staff-grid-adjustment {
            display: flex;
            justify-content: center;
        }
        
        /* Modern shadows and glows */
        .glow-purple {
            box-shadow: 0 0 20px rgba(124, 58, 237, 0.3);
        }
        
        .glow-green {
            box-shadow: 0 0 20px rgba(5, 150, 105, 0.3);
        }
        
        .glow-orange {
            box-shadow: 0 0 20px rgba(234, 88, 12, 0.3);
        }
        
        .glow-red {
            box-shadow: 0 0 20px rgba(220, 38, 38, 0.3);
        }
        
        /* Action card hover effects */
        .action-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(124, 58, 237, 0.1), transparent);
            transition: left 0.5s;
        }
        
        .action-card:hover::before {
            left: 100%;
        }
    </style>
</head>
<body>
    <!-- Include Navbar -->
    <jsp:include page="/includes/navbar.jsp" />
    
    <div class="container-fluid">  
        <!-- Main Content -->
        <main class="px-md-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom border-primary">
                <h1 class="h2 text-primary-dark fw-bold">
                    <i class="fas fa-tachometer-alt me-2" style="color: #037D44;"></i>
                    DASHBOARD
                </h1>
                <div class="btn-toolbar mb-2 mb-md-0">
                    <span class="text-primary-medium">
                        <i class="fas fa-clock me-1"></i> 
                        <fmt:formatDate value="<%=new java.util.Date()%>" pattern="EEEE, dd MMMM yyyy"/>
                    </span>
                </div>
            </div>
            
            <!-- Include Messages -->
            <jsp:include page="/includes/messages.jsp" />
            
            <!-- Statistics Cards -->
            <div class="row g-3 mb-4">
                <!-- Total Customers Card -->
                <div class="col-xl-3 col-md-6">
                    <div class="card card-stat card-stat-customers shadow-sm h-100">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <div class="icon-circle" style="background-color: var(--primary-pale);">
                                    <i class="fas fa-users" style="color: var(--primary); font-size: 1.2rem;"></i>
                                </div>
                                <span class="badge rounded-pill" style="background-color: var(--primary); color: white;">
                                    Customers
                                </span>
                            </div>
                            <h3 class="fw-bold text-primary-dark mb-1 stat-number">${dashboardStats.totalCustomers}</h3>
                            <p class="text-primary-medium small mb-0">
                                <i class="fas fa-check-circle" style="color: var(--accent-green);"></i> 
                                ${dashboardStats.activeCustomers} active
                            </p>
                        </div>
                    </div>
                </div>
                
                <!-- Total Items Card -->
                <div class="col-xl-3 col-md-6">
                    <div class="card card-stat card-stat-items shadow-sm h-100">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <div class="icon-circle" style="background-color: #dcfce7;">
                                    <i class="fas fa-box" style="color: var(--accent-green); font-size: 1.2rem;"></i>
                                </div>
                                <span class="badge rounded-pill" style="background-color: var(--accent-green); color: white;">
                                    Items
                                </span>
                            </div>
                            <h3 class="fw-bold text-primary-dark mb-1">${dashboardStats.totalItems}</h3>
                            <p class="text-primary-medium small mb-0">
                                <i class="fas fa-check-circle" style="color: var(--accent-green);"></i> 
                                ${dashboardStats.activeItems} active
                            </p>
                        </div>
                    </div>
                </div>
                
                <!-- Today's Sales Card -->
                <div class="col-xl-3 col-md-6">
                    <div class="card card-stat card-stat-sales shadow-sm h-100">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <div class="icon-circle" style="background-color: #fed7aa;">
                                    <i class="fas fa-dollar-sign" style="color: var(--accent-orange); font-size: 1.2rem;"></i>
                                </div>
                                <span class="badge rounded-pill" style="background-color: var(--accent-orange); color: white;">
                                    Sales
                                </span>
                            </div>
                            <h3 class="fw-bold text-primary-dark mb-1">
                                <small class="text-primary-medium fw-normal">LKR</small> 
                                <fmt:formatNumber value="${dashboardStats.todaysSales}" pattern="#,##0.00"/>
                            </h3>
                            <p class="text-primary-medium small mb-0">
                                <i class="fas fa-file-invoice" style="color: var(--accent-orange);"></i> 
                                ${dashboardStats.todaysBills} bills today
                            </p>
                        </div>
                    </div>
                </div>
                
                <!-- Stock Alert Card -->
                <div class="col-xl-3 col-md-6">
                    <div class="card card-stat card-stat-alerts shadow-sm h-100">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <div class="icon-circle" style="background-color: #fecaca;">
                                    <i class="fas fa-exclamation-triangle" style="color: var(--accent-red); font-size: 1.2rem;"></i>
                                </div>
                                <span class="badge rounded-pill" style="background-color: var(--accent-red); color: white;">
                                    Alerts
                                </span>
                            </div>
                            <h3 class="fw-bold text-primary-dark mb-1">${dashboardStats.lowStockItems}</h3>
                            <p class="${dashboardStats.outOfStockItems > 0 ? 'text-danger' : 'text-primary-medium'} small mb-0">
                                <c:if test="${dashboardStats.outOfStockItems > 0}">
                                    <i class="fas fa-exclamation-circle"></i>
                                </c:if>
                                ${dashboardStats.outOfStockItems} out of stock
                            </p>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Daily Operations Section - Modern Cards -->
            <div class="mb-4">
                <h5 class="text-primary-dark fw-bold mb-3">
                    <i class="fas fa-tasks me-2" style="color: var(--primary-medium);"></i>
                    Daily Operations
                </h5>
                <div class="row g-4 <%= !canManageItems ? "justify-content-center" : "" %>">
                    <!-- Create Bill Card -->
                    <div class="col-lg-<%= canManageItems ? "4" : "6" %> col-md-6">
                        <a href="${pageContext.request.contextPath}/bill/create" class="action-link">
                            <div class="card action-card action-card-bill shadow h-100">
                                <div class="card-body text-center p-4">
                                    <div class="icon-box mx-auto">
                                        <i class="fas fa-file-invoice text-white fs-3"></i>
                                    </div>
                                    <h5 class="fw-bold text-primary-dark mb-2">Create New Bill</h5>
                                    <p class="text-primary-medium small mb-3">
                                        Generate invoices for customer purchases
                                    </p>
                                    <div class="d-flex justify-content-center align-items-center">
                                        <span class="fw-bold" style="color: var(--primary);">
                                            Start Billing <i class="fas fa-arrow-right ms-2"></i>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </a>
                    </div>
                    
                    <!-- Add Customer Card -->
                    <div class="col-lg-<%= canManageItems ? "4" : "6" %> col-md-6">
                        <a href="${pageContext.request.contextPath}/customer/add" class="action-link">
                            <div class="card action-card action-card-customer shadow h-100">
                                <div class="card-body text-center p-4">
                                    <div class="icon-box mx-auto">
                                        <i class="fas fa-user-plus text-white fs-3"></i>
                                    </div>
                                    <h5 class="fw-bold text-primary-dark mb-2">Add Customer</h5>
                                    <p class="text-primary-medium small mb-3">
                                        Register new customers to the system
                                    </p>
                                    <div class="d-flex justify-content-center align-items-center">
                                        <span style="color: var(--accent-green); font-weight: bold;">
                                            Add Now <i class="fas fa-arrow-right ms-2"></i>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </a>
                    </div>
                    
                    <!-- Add Item Card - Only for Admin and Manager -->
                    <% if (canManageItems) { %>
                        <div class="col-lg-4 col-md-6">
                            <a href="${pageContext.request.contextPath}/item/add" class="action-link">
                                <div class="card action-card action-card-item shadow h-100">
                                    <div class="card-body text-center p-4">
                                        <div class="icon-box mx-auto">
                                            <i class="fas fa-box-open text-white fs-3"></i>
                                        </div>
                                        <h5 class="fw-bold text-primary-dark mb-2">Add Item</h5>
                                        <p class="text-primary-medium small mb-3">
                                            Add new products to inventory
                                        </p>
                                        <div class="d-flex justify-content-center align-items-center">
                                            <span style="color: var(--accent-orange); font-weight: bold;">
                                                Add Item <i class="fas fa-arrow-right ms-2"></i>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </a>
                        </div>
                    <% } %>
                </div>
                
                <!-- Additional Operations for Staff - Optional -->
                <% if (!canManageItems) { %>
                    <div class="row g-4 mt-2">
                        <div class="col-12">
                            <div class="alert border-0 shadow-sm" style="background-color: var(--primary-pale); border-left: 4px solid var(--primary) !important;">
                                <div class="d-flex align-items-center">
                                    <i class="fas fa-info-circle me-3" style="color: var(--primary); font-size: 1.2rem;"></i>
                                    <div>
                                        <strong class="text-primary-dark">Quick Links:</strong>
                                        <span class="ms-2">
                                            <a href="${pageContext.request.contextPath}/bill/list" class="text-decoration-none" style="color: var(--primary);">
                                                <i class="fas fa-list"></i> View Bills
                                            </a>
                                        </span>
                                        <span class="ms-3">
                                            <a href="${pageContext.request.contextPath}/customer/list" class="text-decoration-none" style="color: var(--primary);">
                                                <i class="fas fa-users"></i> Customer List
                                            </a>
                                        </span>
                                        <span class="ms-3">
                                            <a href="${pageContext.request.contextPath}/item/list" class="text-decoration-none" style="color: var(--primary);">
                                                <i class="fas fa-boxes"></i> View Items
                                            </a>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                <% } %>
            </div>
            
            <!-- Critical Alerts (if any) -->
            <c:if test="${dashboardStats.outOfStockItems > 0}">
                <div class="row justify-content-center mt-4">
                    <div class="col-lg-12">
                        <div class="alert border-0 shadow-sm glow-red" role="alert" 
                             style="background-color: #fef2f2; border-left: 4px solid var(--accent-red) !important;">
                            <div class="d-flex align-items-center">
                                <div class="flex-shrink-0">
                                    <i class="fas fa-exclamation-circle fs-4" style="color: var(--accent-red);"></i>
                                </div>
                                <div class="flex-grow-1 ms-3">
                                    <h6 class="alert-heading mb-1 text-primary-dark fw-bold">Critical Stock Alert</h6>
                                    <p class="mb-0 text-primary-medium">
                                        <strong style="color: var(--accent-red);">${dashboardStats.outOfStockItems}</strong> items are currently out of stock. 
                                        <% if (canManageItems) { %>
                                            <a href="${pageContext.request.contextPath}/report/stock?type=low-stock" 
                                               class="alert-link fw-bold" style="color: var(--accent-red);">
                                                View Stock Report →
                                            </a>
                                        <% } else { %>
                                            Please inform a manager about this issue.
                                        <% } %>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            
        </main>
    </div>
    
    <!-- Include Footer -->
    <jsp:include page="/includes/footer.jsp" />
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/common.js"></script>
    
    <script>
        // Add smooth animations to stat cards
        $(document).ready(function() {
            $('.card-stat').each(function(index) {
                $(this).delay(100 * index).animate({opacity: 1}, 300);
            });
            
            $('.action-card').each(function(index) {
                $(this).delay(150 * index).animate({opacity: 1}, 400);
            });
        });
        
        // Initialize cards with opacity 0 for animation
        $('.card-stat, .action-card').css('opacity', 0);
    </script>
</body>
</html>