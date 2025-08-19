<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer List - Pahana Edu</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    
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
        }
        
        body {
            background-color: var(--brown-bg) !important;
            color: var(--brown-dark);
        }
        
        /* Header Styling */
        .border-brown {
            border-color: var(--brown-pale) !important;
        }
        
        .text-brown-dark {
            color: var(--brown-dark) !important;
        }
        
        .text-brown-medium {
            color: var(--brown-medium) !important;
        }
        
        /* Button Styles */
        .btn-brown-primary {
            background-color: var(--brown-primary);
            color: white;
            border: none;
        }
        
        .btn-brown-primary:hover {
            background-color: var(--brown-dark);
            color: white;
        }
        
        .btn-brown-outline {
            background-color: transparent;
            color: var(--brown-medium);
            border: 1px solid var(--brown-light);
        }
        
        .btn-brown-outline:hover {
            background-color: var(--brown-pale);
            color: var(--brown-dark);
            border-color: var(--brown-medium);
        }
        
        /* Card Styles */
        .card {
            border: none;
            box-shadow: 0 2px 10px rgba(0,0,0,0.08);
        }
        
        .card-header {
            background: linear-gradient(135deg, var(--brown-primary) 0%, var(--brown-medium) 100%);
            color: white;
            border: none;
            padding: 1rem 1.25rem;
        }
        
        .card-header h6 {
            color: white !important;
            font-weight: 500;
            font-size: 1.1rem;
        }
        
        /* Search Section */
        .search-section {
            background: white;
            padding: 1.25rem;
            border-bottom: 1px solid var(--brown-pale);
        }
        
        /* Form Controls */
        .form-control, .form-select {
            border-color: var(--brown-pale);
        }
        
        .form-control:focus, .form-select:focus {
            border-color: var(--brown-light);
            box-shadow: 0 0 0 0.2rem rgba(121, 85, 72, 0.25);
        }
        
        /* Table Styling */
        .table thead th {
            background-color: var(--brown-primary);
            color: white;
            font-weight: 500;
            border: none;
            padding: 12px;
            font-size: 0.95rem;
        }
        
        .table tbody tr {
            transition: background-color 0.2s ease;
        }
        
        .table tbody tr:hover {
            background-color: var(--cream);
        }
        
        .table tbody td {
            padding: 12px;
            vertical-align: middle;
            color: var(--brown-dark);
        }
        
        /* Status Badges */
        .badge.status-active {
            background-color: var(--accent-green) !important;
            color: white;
            padding: 0.35rem 0.75rem;
            font-weight: 500;
        }
        
        .badge.status-inactive {
            background-color: var(--brown-light) !important;
            color: white;
            padding: 0.35rem 0.75rem;
            font-weight: 500;
        }
        
        /* Action Buttons */
        .btn-sm {
            padding: 0.375rem 0.75rem;
            margin: 0 2px;
        }
        
        .btn-info {
            background-color: var(--brown-medium);
            border-color: var(--brown-medium);
            color: white;
        }
        
        .btn-info:hover {
            background-color: var(--brown-primary);
            border-color: var(--brown-primary);
            color: white;
        }
        
        .btn-warning {
            background-color: var(--accent-orange);
            border-color: var(--accent-orange);
            color: white;
        }
        
        .btn-warning:hover {
            background-color: #E65100;
            border-color: #E65100;
            color: white;
        }
        
        .btn-success {
            background-color: var(--accent-green);
            border-color: var(--accent-green);
        }
        
        .btn-success:hover {
            background-color: #558B2F;
            border-color: #558B2F;
        }
        
        .btn-danger {
            background-color: var(--accent-red);
            border-color: var(--accent-red);
        }
        
        .btn-danger:hover {
            background-color: #B71C1C;
            border-color: #B71C1C;
        }
        
        /* Export Button */
        .btn-outline-secondary {
            color: var(--brown-medium);
            border-color: var(--brown-medium);
        }
        
        .btn-outline-secondary:hover {
            background-color: var(--brown-medium);
            border-color: var(--brown-medium);
            color: white;
        }
        
        /* Statistics Footer */
        .stats-footer {
            background-color: var(--cream);
            padding: 1rem 1.25rem;
            border-top: 1px solid var(--brown-pale);
        }
        
        .stats-footer .text-muted {
            color: var(--brown-medium) !important;
            font-weight: 500;
        }
        
        /* Empty State */
        .empty-state {
            padding: 3rem;
            text-align: center;
            color: var(--brown-light);
        }
        
        /* Search Icon Button */
        .btn-search {
            background-color: var(--brown-primary);
            color: white;
            border-color: var(--brown-primary);
        }
        
        .btn-search:hover {
            background-color: var(--brown-dark);
            border-color: var(--brown-dark);
            color: white;
        }
    </style>
</head>
<body>
    <jsp:include page="/includes/navbar.jsp" />
    
    <div class="container-fluid">
        <main class="px-md-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom border-brown">
                <h1 class="h2 text-brown-dark fw-bold">Customer Management</h1>
                <div class="btn-toolbar mb-2 mb-md-0">
                    <a href="${pageContext.request.contextPath}/customer/add" class="btn btn-brown-primary">
                        <i class="fas fa-plus"></i> Add New Customer
                    </a>
                </div>
            </div>
            
            <jsp:include page="/includes/messages.jsp" />
            
            <div class="card shadow mb-4">
                <div class="card-header">
                    <div class="d-flex justify-content-between align-items-center">
                        <h6 class="m-0">
                            <i class="fas fa-users me-2"></i>Customer List
                        </h6>
                        <span class="badge rounded-pill" style="background-color: rgba(255,255,255,0.2);">
                            Total: ${customerStats.totalCustomers}
                        </span>
                    </div>
                </div>
                
                <!-- Search Section -->
                <div class="search-section">
                    <form class="form-inline" action="${pageContext.request.contextPath}/customer/list" method="get">
                        <div class="row g-2">
                            <div class="col-md-5">
                                <input type="text" class="form-control" placeholder="Search by name, account number..." 
                                       name="search" value="${param.search}">
                            </div>
                            <div class="col-md-5">
                                <select class="form-select" name="city" onchange="this.form.submit()">
                                    <option value="">All Cities</option>
                                    <c:forEach items="${cities}" var="city">
                                        <option value="${city}" ${param.city eq city ? 'selected' : ''}>${city}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <button class="btn btn-search w-100" type="submit">
                                    <i class="fas fa-search"></i> Search
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
                
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead>
                                <tr>
                                    <th>Account No</th>
                                    <th>Customer Name</th>
                                    <th>Telephone</th>
                                    <th>City</th>
                                    <th>Registered On</th>
                                    <th>Status</th>
                                    <th width="180">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty customers}">
                                        <c:forEach items="${customers}" var="customer">
                                            <tr>
                                                <td>
                                                    <span class="fw-bold text-brown-dark">${customer.accountNumber}</span>
                                                </td>
                                                <td>
                                                    <span class="text-brown-dark">${customer.customerName}</span>
                                                </td>
                                                <td>
                                                    <span class="text-brown-medium">
                                                        <i class="fas fa-phone small me-1"></i>${customer.telephone}
                                                    </span>
                                                </td>
                                                <td>
                                                    <span class="text-brown-medium">${customer.city}</span>
                                                </td>
                                                <td>
                                                    <span class="text-brown-medium">
                                                        <fmt:formatDate value="${customer.registrationDate}" pattern="yyyy-MM-dd"/>
                                                    </span>
                                                </td>
                                                <td>
                                                    <span class="badge rounded-pill ${customer.active ? 'status-active' : 'status-inactive'}">
                                                        ${customer.active ? 'Active' : 'Inactive'}
                                                    </span>
                                                </td>
                                                <td>
                                                    <div class="btn-group" role="group">
                                                        <a href="${pageContext.request.contextPath}/customer/view?id=${customer.customerId}" 
                                                           class="btn btn-sm btn-info" title="View">
                                                            <i class="fas fa-eye"></i>
                                                        </a>
                                                        <a href="${pageContext.request.contextPath}/customer/edit?id=${customer.customerId}" 
                                                           class="btn btn-sm btn-warning" title="Edit">
                                                            <i class="fas fa-edit"></i>
                                                        </a>
                                                        
                                                        <c:choose>
                                                            <c:when test="${customer.active}">
                                                                <!-- Show deactivate button for active customers -->
                                                                <form action="${pageContext.request.contextPath}/customer/toggle-status" method="post" 
                                                                      style="display:inline;" 
                                                                      onsubmit="return confirm('Are you sure you want to deactivate this customer?');">
                                                                    <input type="hidden" name="id" value="${customer.customerId}">
                                                                    <input type="hidden" name="action" value="deactivate">
                                                                    <button type="submit" class="btn btn-sm btn-danger" title="Deactivate">
                                                                        <i class="fas fa-user-slash"></i>
                                                                    </button>
                                                                </form>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <!-- Show activate button for inactive customers -->
                                                                <form action="${pageContext.request.contextPath}/customer/toggle-status" method="post" 
                                                                      style="display:inline;" 
                                                                      onsubmit="return confirm('Are you sure you want to activate this customer?');">
                                                                    <input type="hidden" name="id" value="${customer.customerId}">
                                                                    <input type="hidden" name="action" value="activate">
                                                                    <button type="submit" class="btn btn-sm btn-success" title="Activate">
                                                                        <i class="fas fa-user-check"></i>
                                                                    </button>
                                                                </form>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="7" class="empty-state">
                                                <i class="fas fa-users fa-3x mb-3 d-block" style="color: var(--brown-lighter);"></i>
                                                <p class="mb-0">No customers found</p>
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
                
                <!-- Statistics Footer -->
                <div class="stats-footer">
                    <div class="row align-items-center">
                        <div class="col-md-6">
                            <p class="mb-0 text-muted">
                                <i class="fas fa-info-circle me-2"></i>
                                Showing ${customers.size()} of ${customerStats.totalCustomers} customers
                                <span class="ms-3">
                                    <span class="badge rounded-pill" style="background-color: var(--accent-green); color: white;">
                                        ${customerStats.activeCustomers} active
                                    </span>
                                    <span class="badge rounded-pill ms-1" style="background-color: var(--brown-light); color: white;">
                                        ${customerStats.inactiveCustomers} inactive
                                    </span>
                                </span>
                            </p>
                        </div>
                        <div class="col-md-6 text-end">
                            <form action="${pageContext.request.contextPath}/customer/list" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="export">
                                <button type="submit" class="btn btn-sm btn-outline-secondary">
                                    <i class="fas fa-download"></i> Export to CSV
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
    
    <jsp:include page="/includes/footer.jsp" />
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/common.js"></script>
</body>
</html>