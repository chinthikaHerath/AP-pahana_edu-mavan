<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
<html>
<head>
    <title>Item List - Pahana Edu</title>
    <jsp:include page="/includes/header.jsp" />
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
        
        .border-brown {
            border-color: var(--brown-pale) !important;
        }
        
        /* Card Styles */
        .card {
            border: none;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            background: white;
            border-radius: 8px;
            overflow: hidden;
        }
        
        .card-header {
            background: linear-gradient(135deg, var(--brown-primary) 0%, var(--brown-medium) 100%);
            color: white;
            border: none;
            padding: 1rem 1.25rem;
        }
        
        .card-header h6 {
            color: white !important;
            font-weight: 600;
            margin: 0;
            font-size: 1.1rem;
        }
        
        /* Search Section */
        .input-group {
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            border-radius: 6px;
            overflow: hidden;
        }
        
        .form-control, .form-select {
            border: 1px solid var(--brown-pale);
            background-color: white;
            color: var(--brown-dark);
        }
        
        .form-control:focus, .form-select:focus {
            border-color: var(--brown-primary);
            box-shadow: 0 0 0 0.2rem rgba(93, 64, 55, 0.15);
            background-color: var(--cream);
        }
        
        .btn-outline-secondary {
            color: var(--brown-primary);
            border-color: var(--brown-primary);
            background-color: white;
        }
        
        .btn-outline-secondary:hover {
            color: white;
            background-color: var(--brown-primary);
            border-color: var(--brown-primary);
        }
        
        /* Table Styles */
        .table {
            color: var(--brown-dark);
        }
        
        .table thead th {
            background: linear-gradient(135deg, var(--cream) 0%, #FAF8F3 100%);
            color: var(--brown-dark);
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.85rem;
            letter-spacing: 0.5px;
            border-bottom: 2px solid var(--brown-primary);
            padding: 1rem 0.75rem;
        }
        
        .table tbody tr {
            transition: all 0.2s ease;
            border-bottom: 1px solid #F5F5F5;
        }
        
        .table tbody tr:hover {
            background-color: var(--cream);
            transform: translateX(2px);
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        }
        
        .table tbody td {
            padding: 0.875rem 0.75rem;
            vertical-align: middle;
        }
        
        /* Inactive Item Row */
        .item-inactive {
            opacity: 0.6;
            background-color: #FAFAFA;
        }
        
        .item-inactive:hover {
            background-color: #F5F5F5 !important;
        }
        
        /* Status Badges */
        .badge {
            padding: 0.375rem 0.75rem;
            font-weight: 500;
            font-size: 0.75rem;
            border-radius: 12px;
        }
        
        .badge.bg-success {
            background-color: var(--accent-green) !important;
        }
        
        .badge.bg-secondary {
            background-color: var(--brown-light) !important;
        }
        
        .badge.bg-danger {
            background-color: var(--accent-red) !important;
        }
        
        .badge.bg-warning {
            background-color: var(--accent-orange) !important;
        }
        
        .badge.bg-info {
            background-color: var(--accent-blue) !important;
        }
        
        /* Stock Status Specific */
        .badge.stock-good {
            background-color: var(--accent-green) !important;
            color: white;
        }
        
        .badge.stock-low {
            background-color: var(--accent-yellow) !important;
            color: white;
        }
        
        .badge.stock-out {
            background-color: var(--accent-red) !important;
            color: white;
        }
        
        /* Buttons */
        .btn-primary {
            background-color: var(--brown-primary);
            border-color: var(--brown-primary);
            color: white;
            font-weight: 500;
        }
        
        .btn-primary:hover {
            background-color: var(--brown-dark);
            border-color: var(--brown-dark);
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
        }
        
        /* Action Buttons */
        .btn-sm {
            padding: 0.25rem 0.5rem;
            font-size: 0.8rem;
            border-radius: 4px;
            margin: 0 2px;
            transition: all 0.2s ease;
        }
        
        .btn-info {
            background-color: var(--accent-blue);
            border-color: var(--accent-blue);
            color: white;
        }
        
        .btn-info:hover {
            background-color: #1565C0;
            border-color: #1565C0;
            transform: scale(1.1);
        }
        
        .btn-warning {
            background-color: var(--accent-orange);
            border-color: var(--accent-orange);
            color: white;
        }
        
        .btn-warning:hover {
            background-color: #F57C00;
            border-color: #F57C00;
            transform: scale(1.1);
        }
        
        .btn-danger {
            background-color: var(--accent-red);
            border-color: var(--accent-red);
        }
        
        .btn-danger:hover {
            background-color: #B71C1C;
            border-color: #B71C1C;
            transform: scale(1.1);
        }
        
        .btn-success {
            background-color: var(--accent-green);
            border-color: var(--accent-green);
        }
        
        .btn-success:hover {
            background-color: #558B2F;
            border-color: #558B2F;
            transform: scale(1.1);
        }
        
        /* Price Column */
        td:nth-child(4) {
            font-weight: 600;
            color: var(--brown-primary);
        }
        
        /* Stock Column */
        td:nth-child(5) {
            font-weight: 600;
        }
        
        /* Category Badge */
        .category-badge {
            background-color: var(--cream);
            color: var(--brown-medium);
            padding: 0.25rem 0.5rem;
            border-radius: 4px;
            font-size: 0.8rem;
            display: inline-block;
        }
        
        /* Empty State */
        .empty-state {
            padding: 3rem 1rem;
            text-align: center;
            color: var(--brown-medium);
        }
        
        .empty-state i {
            font-size: 4rem;
            color: var(--brown-lighter);
            margin-bottom: 1rem;
        }
        
        /* Item Code Style */
        td:first-child {
            font-family: monospace;
            font-weight: 600;
            color: var(--brown-medium);
        }
        
        /* Hover Effects */
        @keyframes pulse {
            0% { transform: scale(1); }
            50% { transform: scale(1.05); }
            100% { transform: scale(1); }
        }
        
        .table tbody tr:hover .badge {
            animation: pulse 0.5s;
        }
        
        /* Loading State */
        .loading-overlay {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(255,255,255,0.9);
            display: none;
            align-items: center;
            justify-content: center;
            z-index: 9999;
        }
        
        .loading-overlay.show {
            display: flex;
        }
    </style>
</head>
<body>
    <jsp:include page="/includes/navbar.jsp" />
    
    <div class="container-fluid">
            <main class="px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom border-brown">
                    <h1 class="h2 text-brown-dark fw-bold">
                        <i class="fas fa-boxes me-2" style="color: var(--brown-medium);"></i>
                        Item Management
                    </h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <% if (canManageItems) { %>
                            <a href="${pageContext.request.contextPath}/item/add" class="btn btn-primary">
                                <i class="fas fa-plus"></i> Add New Item
                            </a>
                        <% } %>
                    </div>
                </div>
                
                <jsp:include page="/includes/messages.jsp" />
                
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <div class="row align-items-center">
                            <div class="col-md-6">
                                <h6 class="m-0 font-weight-bold">
                                    <i class="fas fa-list me-2"></i>
                                    Item List
                                </h6>
                            </div>
                            <div class="col-md-6 text-end">
                                <form class="form-inline" action="${pageContext.request.contextPath}/item" method="get">
                                    <div class="input-group">
                                        <span class="input-group-text bg-white border-end-0">
                                            <i class="fas fa-search" style="color: var(--brown-light);"></i>
                                        </span>
                                        <input type="text" class="form-control border-start-0 border-end-0" 
                                               placeholder="Search items..." 
                                               name="search" value="${param.search}">
                                        <select class="form-select border-start-0" name="category" onchange="this.form.submit()">
                                            <option value="">All Categories</option>
                                            <c:forEach items="${categories}" var="category">
                                                <option value="${category.categoryId}" 
                                                    ${param.category eq category.categoryId.toString() ? 'selected' : ''}>
                                                    ${category.categoryName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <button class="btn btn-outline-secondary" type="submit">
                                            <i class="fas fa-filter"></i> Filter
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th><i class="fas fa-barcode me-1"></i> Code</th>
                                        <th><i class="fas fa-tag me-1"></i> Name</th>
                                        <th><i class="fas fa-folder me-1"></i> Category</th>
                                        <th><i class="fas fa-dollar-sign me-1"></i> Price</th>
                                        <th><i class="fas fa-warehouse me-1"></i> Stock</th>
                                        <th><i class="fas fa-toggle-on me-1"></i> Item Status</th>
                                        <th><i class="fas fa-chart-line me-1"></i> Stock Status</th>
                                        <th><i class="fas fa-cogs me-1"></i> Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${not empty items}">
                                            <c:forEach items="${items}" var="item">
                                                <tr class="${item.active ? '' : 'item-inactive'}">
                                                    <td>
                                                        <span class="fw-bold">${item.itemCode}</span>
                                                    </td>
                                                    <td>
                                                        <span class="fw-semibold">${item.itemName}</span>
                                                        <c:if test="${!item.active}">
                                                            <span class="text-muted ms-1">
                                                                <i class="fas fa-ban"></i> (Inactive)
                                                            </span>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <span class="category-badge">
                                                            ${item.categoryName}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <strong>LKR <fmt:formatNumber value="${item.sellingPrice}" pattern="#,##0.00"/></strong>
                                                    </td>
                                                    <td>
                                                        <span class="fw-bold ${item.quantityInStock <= 10 ? 'text-danger' : 'text-success'}">
                                                            ${item.quantityInStock}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <!-- Item Active/Inactive Status -->
                                                        <span class="badge ${item.active ? 'bg-success' : 'bg-secondary'}">
                                                            <i class="fas ${item.active ? 'fa-check-circle' : 'fa-times-circle'} me-1"></i>
                                                            ${item.active ? 'Active' : 'Inactive'}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <!-- Stock Status with dynamic class -->
                                                        <c:choose>
                                                            <c:when test="${item.quantityInStock == 0}">
                                                                <span class="badge stock-out">
                                                                    <i class="fas fa-exclamation-triangle me-1"></i>
                                                                    Out of Stock
                                                                </span>
                                                            </c:when>
                                                            <c:when test="${item.quantityInStock <= 10}">
                                                                <span class="badge stock-low">
                                                                    <i class="fas fa-exclamation-circle me-1"></i>
                                                                    Low Stock
                                                                </span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge stock-good">
                                                                    <i class="fas fa-check me-1"></i>
                                                                    In Stock
                                                                </span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <div class="btn-group" role="group">
                                                            <!-- View button - Available for all users -->
                                                            <a href="${pageContext.request.contextPath}/item/view?id=${item.itemId}" 
                                                               class="btn btn-sm btn-info" title="View">
                                                                <i class="fas fa-eye"></i>
                                                            </a>
                                                            
                                                            <!-- Edit and Activate/Deactivate buttons - Only for Admin and Manager -->
                                                            <% if (canManageItems) { %>
                                                                <a href="${pageContext.request.contextPath}/item/edit?id=${item.itemId}" 
                                                                   class="btn btn-sm btn-warning" title="Edit">
                                                                    <i class="fas fa-edit"></i>
                                                                </a>
                                                                
                                                                <c:choose>
                                                                    <c:when test="${item.active}">
                                                                        <!-- Deactivate button for active items -->
                                                                        <form action="${pageContext.request.contextPath}/item/deactivate" method="post" 
                                                                              style="display:inline;" 
                                                                              onsubmit="return confirm('Are you sure you want to deactivate this item?');">
                                                                            <input type="hidden" name="id" value="${item.itemId}">
                                                                            <button type="submit" class="btn btn-sm btn-danger" title="Deactivate">
                                                                                <i class="fas fa-times-circle"></i>
                                                                            </button>
                                                                        </form>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <!-- Activate button for inactive items -->
                                                                        <form action="${pageContext.request.contextPath}/item/activate" method="post" 
                                                                              style="display:inline;" 
                                                                              onsubmit="return confirm('Are you sure you want to activate this item?');">
                                                                            <input type="hidden" name="id" value="${item.itemId}">
                                                                            <button type="submit" class="btn btn-sm btn-success" title="Activate">
                                                                                <i class="fas fa-check-circle"></i>
                                                                            </button>
                                                                        </form>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            <% } %>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="8" class="empty-state">
                                                    <i class="fas fa-inbox"></i>
                                                    <p class="mt-2 mb-0">No items found</p>
                                                    <p class="text-muted small">Try adjusting your search or filter criteria</p>
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                        
                        <!-- Summary Footer -->
                        <c:if test="${not empty items}">
                            <div class="d-flex justify-content-between align-items-center mt-3 pt-3 border-top">
                                <div class="text-muted">
                                    Showing <strong>${items.size()}</strong> items
                                </div>
                                <div>
                                    <span class="badge bg-success me-2">Active: ${activeCount}</span>
                                    <span class="badge bg-secondary me-2">Inactive: ${inactiveCount}</span>
                                    <span class="badge stock-low">Low Stock: ${lowStockCount}</span>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>
            </main>
    </div>
    
    <!-- Loading Overlay -->
    <div class="loading-overlay" id="loadingOverlay">
        <div class="spinner-border" style="color: var(--brown-primary); width: 3rem; height: 3rem;" role="status">
            <span class="visually-hidden">Loading...</span>
        </div>
    </div>
    
    <jsp:include page="/includes/footer.jsp" />
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Show loading overlay on form submission
        $('form').on('submit', function() {
            $('#loadingOverlay').addClass('show');
        });
        
        // Add tooltips to action buttons
        $(document).ready(function() {
            $('[title]').tooltip();
        });
        
        // Highlight search term
        const searchTerm = '${param.search}';
        if (searchTerm) {
            $('tbody td').each(function() {
                const text = $(this).text();
                if (text.toLowerCase().includes(searchTerm.toLowerCase())) {
                    const regex = new RegExp('(' + searchTerm + ')', 'gi');
                    $(this).html($(this).html().replace(regex, '<mark>$1</mark>'));
                }
            });
        }
    </script>
</body>
</html>