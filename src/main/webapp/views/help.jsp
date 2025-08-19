<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.pahanaedu.util.SessionUtil" %>
<%@ page import="com.pahanaedu.model.User" %>
<%
    User currentUser = SessionUtil.getLoggedInUser(session);
    String userRole = currentUser != null ? currentUser.getRole() : "";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Help - Pahana Edu Billing System</title>
    <jsp:include page="/includes/header.jsp" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    
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
        
        body {
            background-color: var(--brown-bg) !important;
            color: var(--brown-dark);
        }
        
        .text-brown-dark {
            color: var(--brown-dark) !important;
        }
        
        .text-brown-medium {
            color: var(--brown-medium) !important;
        }
        
        /* Card Styles */
        .help-card {
            border: none;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            background: white;
            border-radius: 8px;
            overflow: hidden;
            margin-bottom: 1.5rem;
            transition: transform 0.2s ease;
        }
        
        .help-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        }
        
        .help-card.admin {
            border-left: 4px solid var(--brown-primary);
        }
        
        .help-card.manager {
            border-left: 4px solid var(--accent-blue);
        }
        
        .help-card.cashier {
            border-left: 4px solid var(--accent-green);
        }
        
        .help-section {
            margin-bottom: 2.5rem;
        }
        
        /* Step Numbers */
        .step-number {
            display: inline-block;
            width: 24px;
            height: 24px;
            background-color: var(--brown-primary);
            color: white;
            border-radius: 50%;
            text-align: center;
            margin-right: 10px;
            font-weight: bold;
            line-height: 24px;
        }
        
        /* Help Section Headers */
        h4 {
            color: var(--brown-primary);
            padding-bottom: 0.5rem;
            border-bottom: 2px solid var(--brown-pale);
        }
        
        /* Tips Section */
        .fa-lightbulb {
            margin-right: 8px;
            color: var(--accent-orange);
        }
        
        /* Contact Card */
        .contact-card {
            background-color: var(--cream) !important;
            border: none;
        }
        
        /* Common Tasks Cards */
        .common-task-card {
            border: none;
            background-color: white;
            box-shadow: 0 2px 6px rgba(0,0,0,0.05);
            height: 100%;
        }
        
        .common-task-card:hover {
            background-color: var(--cream);
        }
        
        /* Responsive adjustments */
        @media (max-width: 768px) {
            .help-card {
                margin-bottom: 1rem;
            }
        }
    </style>
</head>
<body>
    <jsp:include page="/includes/navbar.jsp" />
    
    <div class="container mt-4">
        <div class="row">
            <div class="col-12">
                <h2 class="mb-4 text-brown-dark">
                    <i class="fas fa-question-circle" style="color: var(--brown-medium);"></i> Help & User Guide
                </h2>
                
                <!-- Quick Start Guide -->
                <div class="help-section">
                    <h4 class="mb-3">Quick Start Guide</h4>
                    <div class="card help-card">
                        <div class="card-body">
                            <h5 class="card-title text-brown-dark">
                                <i class="fas fa-file-invoice" style="color: var(--brown-medium);"></i> Creating Your First Bill
                            </h5>
                            <ol class="mt-3">
                                <li class="mb-2">
                                    <span class="step-number">1</span>
                                    Navigate to <strong>Billing → Create Bill</strong>
                                </li>
                                <li class="mb-2">
                                    <span class="step-number">2</span>
                                    Select or add a customer
                                </li>
                                <li class="mb-2">
                                    <span class="step-number">3</span>
                                    Add items by searching and clicking "Add to Bill"
                                </li>
                                <li class="mb-2">
                                    <span class="step-number">4</span>
                                    Adjust quantities as needed
                                </li>
                                <li class="mb-2">
                                    <span class="step-number">5</span>
                                    Review total and click "Generate Bill"
                                </li>
                            </ol>
                        </div>
                    </div>
                </div>
                
                <!-- Role-Based Features -->
                <div class="help-section">
                    <h4 class="mb-3">Features by User Role</h4>
                    
                    <% if ("ADMIN".equals(userRole)) { %>
                    <div class="card help-card admin">
                        <div class="card-body">
                            <h5 class="card-title text-brown-dark">
                                <i class="fas fa-user-shield" style="color: var(--brown-primary);"></i> Admin Features
                            </h5>
                            <ul class="mt-3">
                                <li>Full access to all system features</li>
                                <li>User management and role assignment</li>
                                <li>View and generate all types of reports</li>
                                <li>Manage items, customers, and billing</li>
                                <li>System configuration and settings</li>
                            </ul>
                        </div>
                    </div>
                    <% } %>
                    
                    <% if ("MANAGER".equals(userRole) || "ADMIN".equals(userRole)) { %>
                    <div class="card help-card manager">
                        <div class="card-body">
                            <h5 class="card-title text-brown-dark">
                                <i class="fas fa-user-tie" style="color: var(--accent-blue);"></i> Manager Features
                            </h5>
                            <ul class="mt-3">
                                <li>Access to all reports (Sales, Stock, Customer)</li>
                                <li>Manage inventory and items</li>
                                <li>View and edit all bills</li>
                                <li>Customer management</li>
                                <li>Monitor daily operations</li>
                            </ul>
                        </div>
                    </div>
                    <% } %>
                    
                    <div class="card help-card cashier">
                        <div class="card-body">
                            <h5 class="card-title text-brown-dark">
                                <i class="fas fa-cash-register" style="color: var(--accent-green);"></i> Cashier Features
                            </h5>
                            <ul class="mt-3">
                                <li>Create and print bills</li>
                                <li>Add new customers</li>
                                <li>View item list and stock</li>
                                <li>Search and filter bills</li>
                                <li>Basic customer management</li>
                            </ul>
                        </div>
                    </div>
                </div>
                
                <!-- Common Tasks -->
                <div class="help-section">
                    <h4 class="mb-3">Common Tasks</h4>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="card common-task-card mb-3">
                                <div class="card-body">
                                    <h6 class="card-title text-brown-dark">
                                        <i class="fas fa-user-plus" style="color: var(--brown-medium);"></i> Adding a Customer
                                    </h6>
                                    <p class="card-text text-brown-medium">Go to <strong>Customers → Add Customer</strong>. Fill in the required details and click Save.</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="card common-task-card mb-3">
                                <div class="card-body">
                                    <h6 class="card-title text-brown-dark">
                                        <i class="fas fa-box" style="color: var(--brown-medium);"></i> Adding Items
                                    </h6>
                                    <p class="card-text text-brown-medium">Navigate to <strong>Items → Add Item</strong>. Enter item details including name, price, and initial stock.</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="card common-task-card mb-3">
                                <div class="card-body">
                                    <h6 class="card-title text-brown-dark">
                                        <i class="fas fa-search" style="color: var(--brown-medium);"></i> Finding Bills
                                    </h6>
                                    <p class="card-text text-brown-medium">Use <strong>Billing → View Bills</strong> to search by bill number, customer name, or date range.</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="card common-task-card mb-3">
                                <div class="card-body">
                                    <h6 class="card-title text-brown-dark">
                                        <i class="fas fa-warehouse" style="color: var(--brown-medium);"></i> Checking Stock
                                    </h6>
                                    <p class="card-text text-brown-medium">Go to <strong>Items → Stock Status</strong> to view current inventory levels and low stock alerts.</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Tips & Shortcuts -->
                <div class="help-section">
                    <h4 class="mb-3">Tips & Best Practices</h4>
                    <div class="card">
                        <div class="card-body">
                            <ul class="list-unstyled">
                                <li class="mb-2">
                                    <i class="fas fa-lightbulb"></i> 
                                    <strong class="text-brown-dark">Quick Search:</strong> Use the search bars in lists to quickly find customers, items, or bills
                                </li>
                                <li class="mb-2">
                                    <i class="fas fa-lightbulb"></i> 
                                    <strong class="text-brown-dark">Stock Alerts:</strong> Items with stock below 10 units will show a warning indicator
                                </li>
                                <li class="mb-2">
                                    <i class="fas fa-lightbulb"></i> 
                                    <strong class="text-brown-dark">Print Bills:</strong> After generating a bill, use the Print button for a customer receipt
                                </li>
                                <li class="mb-2">
                                    <i class="fas fa-lightbulb"></i> 
                                    <strong class="text-brown-dark">Dashboard:</strong> Check the dashboard regularly for today's sales summary and recent activities
                                </li>
                                <li class="mb-2">
                                    <i class="fas fa-lightbulb"></i> 
                                    <strong class="text-brown-dark">Regular Backups:</strong> Admins should regularly backup the database for data safety
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                
                <!-- Contact Support -->
                <div class="help-section">
                    <div class="card contact-card">
                        <div class="card-body text-center">
                            <h5 class="card-title text-brown-dark">Need More Help?</h5>
                            <p class="card-text text-brown-medium">If you encounter any issues or need additional assistance, please contact your system administrator.</p>
                            <p class="mb-0 text-brown-medium">
                                <i class="fas fa-envelope"></i> Email: support@pahanaedu.com | 
                                <i class="fas fa-phone"></i> Phone: +94 11 234 5678
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <jsp:include page="/includes/footer.jsp" />
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>