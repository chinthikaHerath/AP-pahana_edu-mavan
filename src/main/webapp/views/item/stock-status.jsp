<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Stock Status - Pahana Edu</title>
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
        
        /* Form Elements */
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
        
        .btn-warning {
            background-color: var(--accent-orange);
            border-color: var(--accent-orange);
            color: white;
        }
        
        .btn-warning:hover {
            background-color: #F57C00;
            border-color: #F57C00;
            transform: scale(1.05);
        }
        
        .btn-success {
            background-color: var(--accent-green);
            border-color: var(--accent-green);
        }
        
        .btn-success:hover {
            background-color: #558B2F;
            border-color: #558B2F;
            transform: scale(1.05);
        }
        
        /* Action Buttons */
        .btn-sm {
            padding: 0.25rem 0.5rem;
            font-size: 0.8rem;
            border-radius: 4px;
            margin: 0 2px;
            transition: all 0.2s ease;
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
    </style>
</head>
<body>
    <jsp:include page="/includes/navbar.jsp" />
    
    <div class="container-fluid">
        <main class="px-md-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom border-brown">
                <h1 class="h2 text-brown-dark fw-bold">
                    <i class="fas fa-boxes me-2" style="color: var(--brown-medium);"></i>
                    Stock Status Report
                </h1>
                <div class="btn-toolbar mb-2 mb-md-0">
                    <div class="btn-group me-2">
                        <button type="button" class="btn btn-sm btn-outline-secondary">
                            <i class="fas fa-download me-1"></i> Export
                        </button>
                    </div>
                </div>
            </div>
            
            <jsp:include page="/includes/messages.jsp" />
            
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <div class="row align-items-center">
                        <div class="col-md-6">
                            <h6 class="m-0 font-weight-bold">
                                <i class="fas fa-warehouse me-2"></i>
                                Current Stock Levels
                            </h6>
                        </div>
                        <div class="col-md-6 text-end">
                            <form class="form-inline" action="${pageContext.request.contextPath}/item/stock" method="get">
                                <div class="input-group">
                                    <select class="form-select" name="status" onchange="this.form.submit()">
                                        <option value="">All Items</option>
                                        <option value="low" ${param.status eq 'low' ? 'selected' : ''}>Low Stock Only</option>
                                        <option value="out" ${param.status eq 'out' ? 'selected' : ''}>Out of Stock Only</option>
                                    </select>
                                    <button class="btn btn-outline-secondary" type="button" onclick="this.form.submit()">
                                        <i class="fas fa-filter"></i> Apply
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
                                    <th><i class="fas fa-barcode me-1"></i> Item Code</th>
                                    <th><i class="fas fa-tag me-1"></i> Item Name</th>
                                    <th><i class="fas fa-folder me-1"></i> Category</th>
                                    <th><i class="fas fa-boxes me-1"></i> Current Stock</th>
                                    <th><i class="fas fa-exclamation-circle me-1"></i> Reorder Level</th>
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
                                                    ${item.itemName}
                                                    <c:if test="${!item.active}">
                                                        <span class="text-muted ms-1">
                                                            <i class="fas fa-ban"></i> (Inactive)
                                                        </span>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <span class="badge" style="background-color: var(--cream); color: var(--brown-medium);">
                                                        ${item.categoryName}
                                                    </span>
                                                </td>
                                                <td class="fw-bold ${item.quantityInStock <= 10 ? 'text-danger' : 'text-success'}">
                                                    ${item.quantityInStock}
                                                </td>
                                                <td class="fw-bold">
                                                    ${item.reorderLevel}
                                                </td>
                                                <td>
                                                    <!-- Item Active/Inactive Status -->
                                                    <span class="badge ${item.active ? 'bg-success' : 'bg-secondary'}">
                                                        <i class="fas ${item.active ? 'fa-check-circle' : 'fa-times-circle'} me-1"></i>
                                                        ${item.active ? 'Active' : 'Inactive'}
                                                    </span>
                                                </td>
                                                <td>
                                                    <!-- Stock Status -->
                                                    <c:choose>
                                                        <c:when test="${item.quantityInStock == 0}">
                                                            <span class="badge stock-out">
                                                                <i class="fas fa-exclamation-triangle me-1"></i>
                                                                Out of Stock
                                                            </span>
                                                        </c:when>
                                                        <c:when test="${item.quantityInStock <= item.reorderLevel}">
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
                                                        <!-- Only show Adjust Stock for active items -->
                                                        <c:if test="${item.active}">
                                                            <a href="${pageContext.request.contextPath}/item/adjust-stock?id=${item.itemId}" 
                                                               class="btn btn-sm btn-primary" title="Adjust Stock">
                                                                <i class="fas fa-boxes"></i>
                                                            </a>
                                                        </c:if>
                                                        
                                                        <!-- Edit button for all items -->
                                                        <a href="${pageContext.request.contextPath}/item/edit?id=${item.itemId}" 
                                                           class="btn btn-sm btn-warning" title="Edit Item">
                                                            <i class="fas fa-edit"></i>
                                                        </a>
                                                        
                                                        <!-- Activate button for inactive items -->
                                                        <c:if test="${!item.active}">
                                                            <form action="${pageContext.request.contextPath}/item/activate" method="post" 
                                                                  style="display:inline;" 
                                                                  onsubmit="return confirm('Activate this item?');">
                                                                <input type="hidden" name="id" value="${item.itemId}">
                                                                <button type="submit" class="btn btn-sm btn-success" title="Activate">
                                                                    <i class="fas fa-check-circle"></i>
                                                                </button>
                                                            </form>
                                                        </c:if>
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
                                                <p class="text-muted small">Try adjusting your filter criteria</p>
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
    
    <jsp:include page="/includes/footer.jsp" />
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        $(document).ready(function() {
            $('[title]').tooltip();
        });
    </script>
</body>
</html>