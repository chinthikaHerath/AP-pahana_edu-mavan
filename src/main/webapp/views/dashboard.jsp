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
            --brown-dark: #8B00FF;      /* Violet */
            --brown-primary: #4B0082;   /* Indigo */
            --brown-medium: #0000FF;    /* Blue */
            --brown-light: #00FF00;     /* Green */
            --brown-lighter: #FFFF00;   /* Yellow */
            --brown-pale: #FF7F00;      /* Orange */
            --brown-bg: #FF0000;        /* Red */
            --cream: #FFE4E1;           /* Soft pinkish (light tone for background) */
            --accent-green: #00FF00;    /* Bright green */
            --accent-orange: #FFA500;   /* Bright orange */
            --accent-red: #FF0000;      /* Bright red */
           
        }
        
        body {
            background-color: var(--brown-bg) !important;
            color: var(--brown-dark);
        }
        
        .navbar-brown {
            background-color: var(--brown-dark) !important;
        }
        
        .bg-brown-primary {
            background-color: var(--brown-primary) !important;
        }
        
        .bg-brown-medium {
            background-color: var(--brown-medium) !important;
        }
        
        .bg-brown-light {
            background-color: var(--brown-light) !important;
        }
        
        .bg-brown-pale {
            background-color: var(--brown-pale) !important;
        }
        
        .bg-cream {
            background-color: var(--cream) !important;
        }
        
        .text-brown-dark {
            color: var(--brown-dark) !important;
        }
        
        .text-brown-medium {
            color: var(--brown-medium) !important;
        }
        
        .text-brown-light {
            color: var(--brown-light) !important;
        }
        
        .card-stat {
            border: none;
            border-left: 4px solid;
            transition: transform 0.2s ease;
        }
        
        .card-stat:hover {
            transform: translateY(-2px);
        }
        
        .card-stat-customers {
            border-left-color: var(--brown-primary);
            background: linear-gradient(135deg, white 0%, var(--cream) 100%);
        }
        
        .card-stat-items {
            border-left-color: var(--accent-green);
            background: linear-gradient(135deg, white 0%, #F1F8E9 100%);
        }
        
        .card-stat-sales {
            border-left-color: var(--accent-orange);
            background: linear-gradient(135deg, white 0%, #FFF3E0 100%);
        }
        
        .card-stat-alerts {
            border-left-color: var(--accent-red);
            background: linear-gradient(135deg, white 0%, #FFEBEE 100%);
        }
        
        .icon-circle {
            width: 45px;
            height: 45px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
        }
        
        .border-brown {
            border-color: var(--brown-pale) !important;
        }
        
        .welcome-section {
            background: linear-gradient(135deg, var(--brown-medium) 0%, var(--brown-light) 100%);
            color: white;
        }
        
        .action-card {
            background: white;
            border: none;
            transition: all 0.3s ease;
            overflow: hidden;
            position: relative;
        }
        
        .action-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0,0,0,0.15) !important;
        }
        
        .action-card .icon-box {
            width: 60px;
            height: 60px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 1rem;
        }
        
        .action-card-bill {
            border-top: 3px solid var(--brown-primary);
        }
        
        .action-card-bill .icon-box {
            background: linear-gradient(135deg, var(--brown-primary) 0%, var(--brown-medium) 100%);
        }
        
        .action-card-customer {
            border-top: 3px solid var(--accent-green);
        }
        
        .action-card-customer .icon-box {
            background: linear-gradient(135deg, var(--accent-green) 0%, #8BC34A 100%);
        }
        
        .action-card-item {
            border-top: 3px solid var(--accent-orange);
        }
        
        .action-card-item .icon-box {
            background: linear-gradient(135deg, var(--accent-orange) 0%, #FFA726 100%);
        }
        
        .action-link {
            text-decoration: none;
            color: inherit;
        }
        
        .action-link:hover {
            color: inherit;
        }
        
        /* Responsive adjustments for staff view */
        .staff-grid-adjustment {
            display: flex;
            justify-content: center;
        }
    </style>
</head>
<body>
    <!-- Include Navbar -->
    <jsp:include page="/includes/navbar.jsp" />
    
    <div class="container-fluid">  
        <!-- Main Content -->
        <main class="px-md-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom border-brown">
                <h1 class="h2 text-brown-dark fw-bold">Dashboard</h1>
                <div class="btn-toolbar mb-2 mb-md-0">
                    <span class="text-brown-medium">
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
                                <div class="icon-circle" style="background-color: var(--brown-pale);">
                                    <i class="fas fa-users" style="color: var(--brown-primary); font-size: 1.2rem;"></i>
                                </div>
                                <span class="badge rounded-pill" style="background-color: var(--brown-primary); color: white;">
                                    Customers
                                </span>
                            </div>
                            <h3 class="fw-bold text-brown-dark mb-1">${dashboardStats.totalCustomers}</h3>
                            <p class="text-brown-medium small mb-0">
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
                                <div class="icon-circle" style="background-color: #DCEDC8;">
                                    <i class="fas fa-box" style="color: var(--accent-green); font-size: 1.2rem;"></i>
                                </div>
                                <span class="badge rounded-pill" style="background-color: var(--accent-green); color: white;">
                                    Items
                                </span>
                            </div>
                            <h3 class="fw-bold text-brown-dark mb-1">${dashboardStats.totalItems}</h3>
                            <p class="text-brown-medium small mb-0">
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
                                <div class="icon-circle" style="background-color: #FFE0B2;">
                                    <i class="fas fa-dollar-sign" style="color: var(--accent-orange); font-size: 1.2rem;"></i>
                                </div>
                                <span class="badge rounded-pill" style="background-color: var(--accent-orange); color: white;">
                                    Sales
                                </span>
                            </div>
                            <h3 class="fw-bold text-brown-dark mb-1">
                                <small class="text-brown-medium fw-normal">LKR</small> 
                                <fmt:formatNumber value="${dashboardStats.todaysSales}" pattern="#,##0.00"/>
                            </h3>
                            <p class="text-brown-medium small mb-0">
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
                                <div class="icon-circle" style="background-color: #FFCDD2;">
                                    <i class="fas fa-exclamation-triangle" style="color: var(--accent-red); font-size: 1.2rem;"></i>
                                </div>
                                <span class="badge rounded-pill" style="background-color: var(--accent-red); color: white;">
                                    Alerts
                                </span>
                            </div>
                            <h3 class="fw-bold text-brown-dark mb-1">${dashboardStats.lowStockItems}</h3>
                            <p class="${dashboardStats.outOfStockItems > 0 ? 'text-danger' : 'text-brown-medium'} small mb-0">
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
                <h5 class="text-brown-dark fw-bold mb-3">
                    <i class="fas fa-tasks me-2" style="color: var(--brown-medium);"></i>
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
                                    <h5 class="fw-bold text-brown-dark mb-2">Create New Bill</h5>
                                    <p class="text-brown-medium small mb-3">
                                        Generate invoices for customer purchases
                                    </p>
                                    <div class="d-flex justify-content-center align-items-center">
                                        <span class="text-brown-primary fw-bold">
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
                                    <h5 class="fw-bold text-brown-dark mb-2">Add Customer</h5>
                                    <p class="text-brown-medium small mb-3">
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
                                        <h5 class="fw-bold text-brown-dark mb-2">Add Item</h5>
                                        <p class="text-brown-medium small mb-3">
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
                            <div class="alert alert-info border-0 shadow-sm" style="background-color: #E3F2FD; border-left: 4px solid #1976D2;">
                                <div class="d-flex align-items-center">
                                    <i class="fas fa-info-circle me-3" style="color: #1976D2; font-size: 1.2rem;"></i>
                                    <div>
                                        <strong>Quick Links:</strong>
                                        <span class="ms-2">
                                            <a href="${pageContext.request.contextPath}/bill/list" class="text-decoration-none">
                                                <i class="fas fa-list"></i> View Bills
                                            </a>
                                        </span>
                                        <span class="ms-3">
                                            <a href="${pageContext.request.contextPath}/customer/list" class="text-decoration-none">
                                                <i class="fas fa-users"></i> Customer List
                                            </a>
                                        </span>
                                        <span class="ms-3">
                                            <a href="${pageContext.request.contextPath}/item/list" class="text-decoration-none">
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
                        <div class="alert border-0 shadow-sm" role="alert" 
                             style="background-color: #FFEBEE; border-left: 4px solid var(--accent-red);">
                            <div class="d-flex align-items-center">
                                <div class="flex-shrink-0">
                                    <i class="fas fa-exclamation-circle fs-4" style="color: var(--accent-red);"></i>
                                </div>
                                <div class="flex-grow-1 ms-3">
                                    <h6 class="alert-heading mb-1 text-brown-dark">Critical Stock Alert</h6>
                                    <p class="mb-0 text-brown-medium">
                                        <strong>${dashboardStats.outOfStockItems}</strong> items are currently out of stock. 
                                        <% if (canManageItems) { %>
                                            <a href="${pageContext.request.contextPath}/report/stock?type=low-stock" 
                                               class="alert-link" style="color: var(--accent-red); font-weight: bold;">
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
</body>
</html>