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
    <title>Item Details - Pahana Edu</title>
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
        
        /* Info Sections */
        .info-section {
            background: linear-gradient(135deg, white 0%, var(--cream) 100%);
            padding: 1.5rem;
            border-radius: 8px;
            margin-bottom: 1.5rem;
            border-left: 3px solid var(--brown-primary);
        }
        
        .info-section h5 {
            color: var(--brown-primary);
            font-weight: 600;
            margin-bottom: 1rem;
            font-size: 1.1rem;
            display: flex;
            align-items: center;
        }
        
        .info-section h5 i {
            margin-right: 0.5rem;
            color: var(--brown-medium);
        }
        
        /* Table Styles */
        .table {
            color: var(--brown-dark);
        }
        
        .table-borderless th {
            color: var(--brown-medium);
            font-weight: 600;
            padding: 0.5rem 0;
            font-size: 0.9rem;
        }
        
        .table-borderless td {
            color: var(--brown-dark);
            padding: 0.5rem 0;
            font-weight: 500;
        }
        
        /* Status Badge */
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
        
        .badge.bg-warning {
            background-color: var(--accent-yellow) !important;
        }
        
        .badge.bg-danger {
            background-color: var(--accent-red) !important;
        }
        
        .badge.bg-info {
            background-color: var(--accent-blue) !important;
        }
        
        /* Stock Status Badges */
        .stock-good {
            background-color: var(--accent-green) !important;
            color: white;
        }
        
        .stock-low {
            background-color: var(--accent-yellow) !important;
            color: white;
        }
        
        .stock-out {
            background-color: var(--accent-red) !important;
            color: white;
        }
        
        /* Buttons */
        .btn-primary {
            background-color: var(--brown-primary);
            border-color: var(--brown-primary);
            color: white;
            font-weight: 500;
            padding: 0.5rem 1.5rem;
            transition: all 0.3s ease;
        }
        
        .btn-primary:hover {
            background-color: var(--brown-dark);
            border-color: var(--brown-dark);
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
        }
        
        .btn-secondary {
            background-color: var(--brown-light);
            border-color: var(--brown-light);
            color: white;
            font-weight: 500;
            padding: 0.5rem 1.5rem;
            transition: all 0.3s ease;
        }
        
        .btn-secondary:hover {
            background-color: var(--brown-medium);
            border-color: var(--brown-medium);
        }
        
        .btn-info {
            background-color: var(--accent-blue);
            border-color: var(--accent-blue);
            color: white;
            font-weight: 500;
            transition: all 0.3s ease;
        }
        
        .btn-info:hover {
            background-color: #1565C0;
            border-color: #1565C0;
            transform: translateY(-2px);
        }
        
        .btn-warning {
            background-color: var(--accent-orange);
            border-color: var(--accent-orange);
            color: white;
            font-weight: 500;
            transition: all 0.3s ease;
        }
        
        .btn-warning:hover {
            background-color: #F57C00;
            border-color: #F57C00;
            transform: translateY(-2px);
        }
        
        .btn-success {
            background-color: var(--accent-green);
            border-color: var(--accent-green);
            color: white;
            font-weight: 500;
            transition: all 0.3s ease;
        }
        
        .btn-success:hover {
            background-color: #558B2F;
            border-color: #558B2F;
            transform: translateY(-2px);
        }
        
        /* Price Display */
        .price-value {
            color: var(--brown-primary);
            font-weight: 600;
            font-size: 1.1rem;
        }
        
        /* Profit Display */
        .profit-positive {
            color: var(--accent-green);
            font-weight: 600;
        }
        
        .profit-negative {
            color: var(--accent-red);
            font-weight: 600;
        }
        
        /* Stock Quantity Display */
        .stock-quantity {
            font-size: 1.2rem;
            font-weight: 600;
            color: var(--brown-primary);
        }
        
        /* Description Box */
        .description-box {
            background-color: var(--cream);
            padding: 1rem;
            border-radius: 6px;
            border-left: 3px solid var(--brown-primary);
        }
        
        /* Stock Movement Table */
        .movement-table thead th {
            background: linear-gradient(135deg, var(--cream) 0%, #FAF8F3 100%);
            color: var(--brown-dark);
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.85rem;
            letter-spacing: 0.5px;
            border-bottom: 2px solid var(--brown-primary);
        }
        
        .movement-table tbody tr:hover {
            background-color: var(--cream);
        }
        
        /* Additional Info */
        .additional-info {
            background-color: #F5F5F5;
            padding: 0.75rem;
            border-radius: 6px;
            border-left: 3px solid var(--brown-pale);
        }
        
        .additional-info i {
            color: var(--brown-medium);
        }
        
        /* Warning Box */
        .warning-box {
            background-color: #FFF3E0;
            border-left: 3px solid var(--accent-orange);
            padding: 0.5rem 1rem;
            border-radius: 4px;
            display: inline-block;
        }
        
        .warning-box i {
            color: var(--accent-orange);
        }
        
        /* Divider */
        hr {
            border-color: var(--brown-pale);
            opacity: 0.3;
            margin: 2rem 0;
        }
        
        /* Status Icon */
        .status-icon {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            margin-right: 0.5rem;
        }
        
        .status-icon.active {
            background-color: rgba(104, 159, 56, 0.1);
            color: var(--accent-green);
        }
        
        .status-icon.inactive {
            background-color: rgba(161, 136, 127, 0.1);
            color: var(--brown-light);
        }
        
        /* Staff View Notice */
        .staff-notice {
            background-color: var(--cream);
            border-left: 3px solid var(--brown-primary);
            padding: 1rem;
            border-radius: 6px;
            margin-top: 1.5rem;
        }
        
        .staff-notice i {
            color: var(--brown-medium);
        }
    </style>
</head>
<body>
    <jsp:include page="/includes/navbar.jsp" />
    
    <div class="container-fluid">
            <main class="px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom border-brown">
                    <h1 class="h2 text-brown-dark fw-bold">
                        <i class="fas fa-cube me-2" style="color: var(--brown-medium);"></i>
                        Item Details
                    </h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <a href="${pageContext.request.contextPath}/item/list" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back to List
                        </a>
                    </div>
                </div>
                
                <jsp:include page="/includes/messages.jsp" />
                
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <div class="d-flex justify-content-between align-items-center">
                            <h6 class="m-0 font-weight-bold">
                                <i class="fas fa-tag me-2"></i>
                                ${item.itemName}
                            </h6>
                            <div class="d-flex align-items-center">
                                <div class="status-icon ${item.active ? 'active' : 'inactive'}">
                                    <i class="fas ${item.active ? 'fa-check' : 'fa-times'}"></i>
                                </div>
                                <span class="badge ${item.active ? 'bg-success' : 'bg-secondary'}" id="statusBadge">
                                    ${item.active ? 'Active' : 'Inactive'}
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="info-section">
                                    <h5>
                                        <i class="fas fa-info-circle"></i>
                                        Basic Information
                                    </h5>
                                    <table class="table table-sm table-borderless">
                                        <tr>
                                            <th width="40%">Item Code:</th>
                                            <td><span class="fw-bold" style="font-family: monospace;">${item.itemCode}</span></td>
                                        </tr>
                                        <tr>
                                            <th>Category:</th>
                                            <td>
                                                <span class="badge" style="background-color: var(--cream); color: var(--brown-medium);">
                                                    ${item.categoryName}
                                                </span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Author:</th>
                                            <td>${not empty item.author ? item.author : '<span class="text-muted">N/A</span>'}</td>
                                        </tr>
                                        <tr>
                                            <th>Publisher:</th>
                                            <td>${not empty item.publisher ? item.publisher : '<span class="text-muted">N/A</span>'}</td>
                                        </tr>
                                        <tr>
                                            <th>ISBN:</th>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty item.isbn}">
                                                        <span style="font-family: monospace;">${item.isbn}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted">N/A</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="info-section">
                                    <h5>
                                        <i class="fas fa-chart-line"></i>
                                        Pricing & Stock
                                    </h5>
                                    <table class="table table-sm table-borderless">
                                        <tr>
                                            <th width="40%">Unit Price:</th>
                                            <td class="price-value">LKR <fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/></td>
                                        </tr>
                                        <tr>
                                            <th>Selling Price:</th>
                                            <td class="price-value">LKR <fmt:formatNumber value="${item.sellingPrice}" pattern="#,##0.00"/></td>
                                        </tr>
                                        <tr>
                                            <th>Profit:</th>
                                            <td class="${item.profit >= 0 ? 'profit-positive' : 'profit-negative'}">
                                                LKR <fmt:formatNumber value="${item.profit}" pattern="#,##0.00"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Profit Margin:</th>
                                            <td class="${item.profitMargin >= 0 ? 'profit-positive' : 'profit-negative'}">
                                                <fmt:formatNumber value="${item.profitMargin}" pattern="#.##"/>%
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Quantity in Stock:</th>
                                            <td>
                                                <span id="stockQuantity" class="stock-quantity">${item.quantityInStock}</span> 
                                                <span class="text-brown-medium">units</span>
                                                <c:if test="${item.quantityInStock > 0 && !item.active}">
                                                    <div class="warning-box mt-2">
                                                        <i class="fas fa-exclamation-triangle"></i>
                                                        Has stock but inactive
                                                    </div>
                                                </c:if>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Reorder Level:</th>
                                            <td>
                                                <span class="fw-bold">${item.reorderLevel}</span> 
                                                <span class="text-brown-medium">units</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>Stock Status:</th>
                                            <td>
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
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </div>
                        
                        <c:if test="${not empty item.description}">
                            <div class="description-box mt-3">
                                <h5 class="text-brown-dark mb-2">
                                    <i class="fas fa-align-left me-2" style="color: var(--brown-medium);"></i>
                                    Description
                                </h5>
                                <p class="mb-0">${item.description}</p>
                            </div>
                        </c:if>
                        
                        <!-- Action Buttons Section - Only visible for Admin and Manager -->
                        <% if (canManageItems) { %>
                            <hr>
                            
                            <div class="mt-4">
                                <!-- Edit Button -->
                                <a href="${pageContext.request.contextPath}/item/edit?id=${item.itemId}" 
                                   class="btn btn-primary me-2">
                                    <i class="fas fa-edit"></i> Edit Details
                                </a>
                                
                                <!-- Adjust Stock Button (only for active items) -->
                                <c:if test="${item.active}">
                                    <a href="${pageContext.request.contextPath}/item/adjust-stock?id=${item.itemId}" 
                                       class="btn btn-info me-2">
                                        <i class="fas fa-boxes"></i> Adjust Stock
                                    </a>
                                </c:if>
                                
                                <!-- Activate/Deactivate Button -->
                                <c:choose>
                                    <c:when test="${item.active}">
                                        <!-- Deactivate Button for Active Items -->
                                        <button type="button" class="btn btn-warning" id="statusToggleBtn"
                                                onclick="toggleItemStatus(${item.itemId}, false)">
                                            <i class="fas fa-pause-circle"></i> Deactivate Item
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <!-- Activate Button for Inactive Items -->
                                        <button type="button" class="btn btn-success" id="statusToggleBtn"
                                                onclick="toggleItemStatus(${item.itemId}, true)">
                                            <i class="fas fa-play-circle"></i> Activate Item
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        <% } else { %>
                            <!-- Notice for Staff Users -->
                            <div class="staff-notice">
                                <i class="fas fa-info-circle me-2"></i>
                                <strong>View-Only Mode:</strong> You have read-only access to item details. 
                                Contact a Manager or Administrator to make changes to this item.
                            </div>
                        <% } %>
                        
                        <!-- Additional Information -->
                        <div class="additional-info mt-4">
                            <small class="text-brown-medium">
                                <i class="fas fa-info-circle me-2"></i>
                                <strong>Created:</strong> <fmt:formatDate value="${item.createdAt}" pattern="yyyy-MM-dd HH:mm"/> 
                                <span class="mx-2">|</span>
                                <strong>Last Updated:</strong> <fmt:formatDate value="${item.updatedAt}" pattern="yyyy-MM-dd HH:mm"/>
                            </small>
                        </div>
                    </div>
                </div>
                
                <!-- Stock Movement History (Optional - if you want to show recent movements) -->
                <c:if test="${not empty stockHistory}">
                    <div class="card shadow mb-4">
                        <div class="card-header py-3">
                            <h6 class="m-0 font-weight-bold">
                                <i class="fas fa-history me-2"></i>
                                Recent Stock Movements
                            </h6>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-sm movement-table">
                                    <thead>
                                        <tr>
                                            <th><i class="fas fa-calendar me-1"></i> Date</th>
                                            <th><i class="fas fa-exchange-alt me-1"></i> Type</th>
                                            <th><i class="fas fa-sort-numeric-up-alt me-1"></i> Quantity</th>
                                            <th><i class="fas fa-comment me-1"></i> Reason</th>
                                            <th><i class="fas fa-user me-1"></i> By</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${stockHistory}" var="movement" varStatus="loop">
                                            <c:if test="${loop.index < 5}">
                                                <tr>
                                                    <td><fmt:formatDate value="${movement.movementDate}" pattern="yyyy-MM-dd HH:mm"/></td>
                                                    <td>
                                                        <span class="badge bg-info">${movement.movementType.displayName}</span>
                                                    </td>
                                                    <td class="fw-bold">
                                                        <span class="${movement.quantity > 0 ? 'text-success' : 'text-danger'}">
                                                            ${movement.movementSymbol}${movement.quantity}
                                                        </span>
                                                    </td>
                                                    <td>${movement.reason}</td>
                                                    <td>${movement.userName}</td>
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </c:if>
            </main>
    </div>
    
    <jsp:include page="/includes/footer.jsp" />
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    
    <% if (canManageItems) { %>
    <script>
    function toggleItemStatus(itemId, activate) {
        var action = activate ? 'activate' : 'deactivate';
        var confirmMsg = activate ? 
            'Are you sure you want to activate this item?' : 
            'Are you sure you want to deactivate this item?';
        
        // Check stock before deactivation
        var stockQty = parseInt($('#stockQuantity').text());
        if (!activate && stockQty > 0) {
            confirmMsg += '\n\nWarning: This item has ' + stockQty + ' units in stock!';
            confirmMsg += '\nDeactivating will prevent sales but won\'t remove the stock.';
        }
        
        if (confirm(confirmMsg)) {
            // Create form and submit
            var form = $('<form>', {
                'method': 'POST',
                'action': '${pageContext.request.contextPath}/item/' + action
            });
            
            form.append($('<input>', {
                'type': 'hidden',
                'name': 'id',
                'value': itemId
            }));
            
            // Add force parameter if deactivating with stock
            if (!activate && stockQty > 0) {
                form.append($('<input>', {
                    'type': 'hidden',
                    'name': 'force',
                    'value': 'true'
                }));
            }
            
            form.appendTo('body').submit();
        }
    }
    
    // Alternative: AJAX implementation
    function toggleItemStatusAjax(itemId, activate) {
        var action = activate ? 'activate' : 'deactivate';
        
        $.ajax({
            url: '${pageContext.request.contextPath}/item/' + action,
            type: 'POST',
            data: {
                id: itemId,
                force: true // For items with stock
            },
            beforeSend: function(xhr) {
                xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
            },
            success: function(response) {
                if (response.success) {
                    // Update UI
                    location.reload(); // Simple reload, or update specific elements
                } else {
                    alert('Error: ' + response.error);
                }
            },
            error: function(xhr, status, error) {
                alert('Error: ' + error);
            }
        });
    }
    </script>
    <% } %>
</body>
</html>