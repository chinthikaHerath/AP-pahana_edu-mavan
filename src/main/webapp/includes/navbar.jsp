<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.pahanaedu.util.SessionUtil" %>
<%@ page import="com.pahanaedu.model.User" %>
<%
    User currentUser = SessionUtil.getLoggedInUser(session);
%>

<style>
    :root {
        --brown-dark: #3E2723;
        --brown-primary: #5D4037;
        --brown-medium: #795548;
        --brown-light: #A1887F;
        --brown-lighter: #BCAAA4;
        --brown-pale: #D7CCC8;
        --brown-bg: #EFEBE9;
        --cream: #FFF8E1;
        --accent-green: #689F38;
        --accent-orange: #FF6F00;
        --accent-red: #D32F2F;
        --accent-blue: #1976D2;
        --accent-yellow: #FFA000;
    }

    .navbar-brown {
        background: linear-gradient(135deg, #049B55 0%, #2EB676 100%) !important;
        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    }

    .navbar-brand {
        font-weight: 600;
        letter-spacing: 0.5px;
         color: black !important;
        
    }

    .navbar-brand i {
        margin-right: 8px;
        
    }

    .nav-link {
        color: black !important;
        padding: 0.5rem 1rem;
        transition: all 0.2s ease;
    }

    .nav-link:hover {
        color: white !important;
        transform: translateY(-2px);
    }

    .nav-link.active {
        color: white !important;
        font-weight: 500;
        border-bottom: 2px solid var(--cream);
    }

    .dropdown-menu {
        background-color: white;
        border: none;
        box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        border-radius: 8px;
        margin-top: 8px;
    }

    .dropdown-item {
        color: var(--brown-dark);
        padding: 0.5rem 1rem;
        transition: all 0.2s ease;
    }

    .dropdown-item:hover {
        background-color: var(--cream);
        color: var(--brown-dark);
        transform: translateX(5px);
    }

    .dropdown-divider {
        border-color: var(--brown-pale);
    }

    .dropdown-item-text {
        color: var(--brown-dark);
    }

    .badge-role {
        background-color: var(--cream);
        color: var(--brown-medium);
        font-weight: 500;
        font-size: 0.7rem;
        padding: 0.25rem 0.5rem;
    }

    .navbar-toggler {
        border-color: var(--brown-lighter);
    }

    .navbar-toggler-icon {
        background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 30 30'%3e%3cpath stroke='rgba%28255, 255, 255, 0.8%29' stroke-linecap='round' stroke-miterlimit='10' stroke-width='2' d='M4 7h22M4 15h22M4 23h22'/%3e%3c/svg%3e");
    }
</style>

<nav class="navbar navbar-expand-lg navbar-brown fixed-top">
    <div class="container-fluid">
        <!-- Brand -->
        <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard">
            <i class="fas fa-graduation-cap"></i> Pahana Edu
        </a>
        
        <!-- Mobile Toggle -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" 
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        
        <!-- Navbar Content -->
        <div class="collapse navbar-collapse" id="navbarNav">
            <!-- Left Side Menu -->
            <ul class="navbar-nav me-auto">
                <li class="nav-item">
                    <a class="nav-link ${pageContext.request.servletPath == '/dashboard' ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/dashboard">
                        <i class="fas fa-tachometer-alt"></i> Dashboard
                    </a>
                </li>
                
                <!-- Billing Dropdown -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="billingDropdown" role="button" 
                       data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-file-invoice"></i> Billing
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="billingDropdown">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/bill/create">
                            <i class="fas fa-plus"></i> Create Bill</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/bill/list">
                            <i class="fas fa-list"></i> View Bills</a></li>
                    </ul>
                </li>
                
                <!-- Customers Dropdown -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="customerDropdown" role="button" 
                       data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-users"></i> Customers
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="customerDropdown">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/customer/add">
                            <i class="fas fa-user-plus"></i> Add Customer</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/customer/list">
                            <i class="fas fa-list"></i> Customer List</a></li>
                    </ul>
                </li>
                
                <!-- Items Dropdown -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="itemDropdown" role="button" 
                       data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-box"></i> Items
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="itemDropdown">
                        <!-- Add Item - Only for Admin and Manager -->
                        <% if (currentUser != null && ("ADMIN".equals(currentUser.getRole()) || "MANAGER".equals(currentUser.getRole()))) { %>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/item/add">
                                <i class="fas fa-plus"></i> Add Item</a></li>
                            <li><hr class="dropdown-divider"></li>
                        <% } %>
                        
                        <!-- Item List - Available to all logged-in users -->
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/item/list">
                            <i class="fas fa-list"></i> Item List</a></li>
                        
                        <!-- Stock Status - Only for Admin and Manager -->
                        <% if (currentUser != null && ("ADMIN".equals(currentUser.getRole()) || "MANAGER".equals(currentUser.getRole()))) { %>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/item/stock">
                                <i class="fas fa-warehouse"></i> Stock Status</a></li>
                        <% } %>
                    </ul>
                </li>
                                
                <!-- Reports (Manager/Admin only) -->
                <% if (currentUser != null && ("ADMIN".equals(currentUser.getRole()) || "MANAGER".equals(currentUser.getRole()))) { %>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="reportDropdown" role="button" 
                           data-bs-toggle="dropdown" aria-expanded="false">
                            <i class="fas fa-chart-bar"></i> Reports
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="reportDropdown">
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/report/sales">
                                <i class="fas fa-calendar-day"></i> Sales Report</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/report/stock">
                                <i class="fas fa-boxes"></i> Stock Report</a></li>
                            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/report/customer">
                                <i class="fas fa-user-friends"></i> Customer Report</a></li>
                        </ul>
                    </li>
                <% } %>
                
                <!-- Help Link -->
                <li class="nav-item">
                    <a class="nav-link ${pageContext.request.servletPath == '/help' ? 'active' : ''}" 
                       href="${pageContext.request.contextPath}/help">
                        <i class="fas fa-question-circle"></i> Help
                    </a>
                </li>
            </ul>
            
            <!-- Right Side Menu -->
            <ul class="navbar-nav ms-auto">
                <!-- User Menu -->
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" 
                       data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-user-circle"></i> 
                        <%= currentUser != null ? currentUser.getFullName() : "User" %>
                        <% if (currentUser != null) { %>
                            <span class="badge badge-role ms-1"><%= currentUser.getRole() %></span>
                        <% } %>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                        <li>
                            <span class="dropdown-item-text">
                                <strong><%= currentUser != null ? currentUser.getFullName() : "User" %></strong>
                                <br>
                                <small class="text-muted"><%= currentUser != null ? currentUser.getRole() : "" %></small>
                            </span>
                        </li>
                        <li><hr class="dropdown-divider"></li>
                        <li>
                            <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                <i class="fas fa-sign-out-alt"></i> Logout
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- Add spacing for fixed navbar -->
<div style="margin-top: 56px;"></div>