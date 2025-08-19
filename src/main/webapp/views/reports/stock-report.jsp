<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Stock Report - Pahana Edu</title>
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
        
        .card-header h5, .card-header h6 {
            color: white !important;
            font-weight: 600;
            margin: 0;
        }
        
        /* Stat Cards */
        .stat-card {
            border-left: 4px solid;
            transition: transform 0.2s ease;
        }
        
        .stat-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
        
        .stat-card.primary {
            border-left-color: var(--brown-primary);
        }
        
        .stat-card.warning {
            border-left-color: var(--accent-yellow);
        }
        
        .stat-card.danger {
            border-left-color: var(--accent-red);
        }
        
        .stat-card.success {
            border-left-color: var(--accent-green);
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
        
        /* Buttons */
        .btn-outline-primary {
            color: var(--brown-primary);
            border-color: var(--brown-primary);
        }
        
        .btn-outline-primary:hover {
            color: white;
            background-color: var(--brown-primary);
        }
        
        .btn-primary {
            background-color: var(--brown-primary);
            border-color: var(--brown-primary);
        }
        
        .btn-primary:hover {
            background-color: var(--brown-dark);
            border-color: var(--brown-dark);
        }
        
        .btn-outline-secondary {
            color: var(--brown-primary);
            border-color: var(--brown-primary);
        }
        
        .btn-outline-secondary:hover {
            color: white;
            background-color: var(--brown-primary);
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
        
        /* Status Badges */
        .badge {
            padding: 0.375rem 0.75rem;
            font-weight: 500;
            font-size: 0.75rem;
            border-radius: 12px;
        }
        
        .bg-primary {
            background-color: var(--brown-primary) !important;
        }
        
        .bg-success {
            background-color: var(--accent-green) !important;
        }
        
        .bg-warning {
            background-color: var(--accent-yellow) !important;
        }
        
        .bg-danger {
            background-color: var(--accent-red) !important;
        }
        
        /* Section Divider */
        .section-divider {
            height: 2px;
            background: linear-gradient(90deg, transparent, var(--brown-primary), transparent);
            margin: 3rem 0;
        }
        
        /* Print Styles */
        @media print {
            body {
                background-color: white !important;
                color: black !important;
            }
            
            .card {
                box-shadow: none !important;
                border: 1px solid #ddd !important;
            }
            
            .card-header {
                background: var(--brown-primary) !important;
                color: white !important;
                -webkit-print-color-adjust: exact;
                print-color-adjust: exact;
            }
            
            .btn-group, .form-inline, .btn-toolbar {
                display: none !important;
            }
            
            .stat-card {
                border: 1px solid #ddd !important;
            }
        }
    </style>
</head>
<body>
    <jsp:include page="/includes/navbar.jsp" />
    
    <div class="container-fluid">
        <main class="px-md-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom border-brown">
                <h1 class="h2 text-brown-dark fw-bold">
                    <i class="fas fa-chart-pie me-2" style="color: var(--brown-medium);"></i>
                    Stock Report
                </h1>
                <div class="btn-toolbar mb-2 mb-md-0">
                    <div class="btn-group me-2">
                        <button type="button" class="btn btn-sm btn-outline-secondary" onclick="printReport()">
                            <i class="fas fa-print me-1"></i> Print
                        </button>
                        <a href="?export=csv" 
                           class="btn btn-sm btn-outline-secondary">
                            <i class="fas fa-download me-1"></i> Export CSV
                        </a>
                    </div>
                </div>
            </div>
            
            <jsp:include page="/includes/messages.jsp" />
            
            <!-- Report Content -->
            <div id="reportContent">
                <!-- Report Header -->
                <div class="text-center mb-4">
                    <h3 class="text-brown-dark">Complete Stock Report</h3>
                    <p class="text-brown-medium">
                        <i class="fas fa-calendar-alt me-1"></i>
                        Generated on: <fmt:formatDate value="${report.generatedDate}" pattern="MMMM dd, yyyy HH:mm"/>
                    </p>
                </div>
                
                <!-- Summary Cards -->
                <div class="row mb-4">
                    <div class="col-md-3">
                        <div class="card stat-card primary h-100">
                            <div class="card-body">
                                <h6 class="text-muted">
                                    <i class="fas fa-box me-1"></i> Total Items
                                </h6>
                                <h3 class="mb-0 text-brown-dark">${report.totalItems}</h3>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card stat-card warning h-100">
                            <div class="card-body">
                                <h6 class="text-muted">
                                    <i class="fas fa-exclamation-circle me-1"></i> Low Stock Items
                                </h6>
                                <h3 class="mb-0 text-brown-dark">${report.lowStockItems}</h3>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card stat-card danger h-100">
                            <div class="card-body">
                                <h6 class="text-muted">
                                    <i class="fas fa-times-circle me-1"></i> Out of Stock
                                </h6>
                                <h3 class="mb-0 text-brown-dark">${report.outOfStockItems}</h3>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card stat-card success h-100">
                            <div class="card-body">
                                <h6 class="text-muted">
                                    <i class="fas fa-coins me-1"></i> Total Stock Value
                                </h6>
                                <h3 class="mb-0 text-brown-dark">
                                    LKR <fmt:formatNumber value="${report.totalStockValue}" pattern="#,##0.00"/>
                                </h3>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Stock by Category -->
                <c:if test="${not empty report.stockByCategory}">
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5 class="mb-0">
                                <i class="fas fa-chart-pie me-1"></i>
                                Stock by Category
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <canvas id="categoryChart" height="200"></canvas>
                                </div>
                                <div class="col-md-6">
                                    <div class="table-responsive">
                                        <table class="table table-sm">
                                            <thead>
                                                <tr>
                                                    <th>Category</th>
                                                    <th>Items</th>
                                                    <th>Stock Value</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${report.stockByCategory}" var="cat">
                                                    <tr>
                                                        <td>${cat.category}</td>
                                                        <td>${cat.itemCount}</td>
                                                        <td>LKR <fmt:formatNumber value="${cat.stockValue}" pattern="#,##0.00"/></td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
                
                <!-- Full Stock List - Use fullStockList from request attribute -->
                <c:if test="${not empty fullStockList}">
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5 class="mb-0">
                                <i class="fas fa-boxes me-1"></i>
                                All Items Stock Status
                            </h5>
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
                                            <th><i class="fas fa-dollar-sign me-1"></i> Unit Price</th>
                                            <th><i class="fas fa-coins me-1"></i> Stock Value</th>
                                            <th><i class="fas fa-info-circle me-1"></i> Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${fullStockList}" var="item">
                                            <tr>
                                                <td>${item.itemCode}</td>
                                                <td>${item.itemName}</td>
                                                <td>${item.category}</td>
                                                <td class="fw-bold ${item.currentStock == 0 ? 'text-danger' : (item.currentStock <= item.reorderLevel ? 'text-warning' : 'text-success')}">
                                                    ${item.currentStock}
                                                </td>
                                                <td>${item.reorderLevel}</td>
                                                <td>LKR <fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/></td>
                                                <td>LKR <fmt:formatNumber value="${item.stockValue}" pattern="#,##0.00"/></td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${item.currentStock == 0}">
                                                            <span class="badge bg-danger">
                                                                <i class="fas fa-times-circle me-1"></i> Out of Stock
                                                            </span>
                                                        </c:when>
                                                        <c:when test="${item.currentStock <= item.reorderLevel}">
                                                            <span class="badge bg-warning">
                                                                <i class="fas fa-exclamation-circle me-1"></i> Low Stock
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-success">
                                                                <i class="fas fa-check-circle me-1"></i> In Stock
                                                            </span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </c:if>
                
                <!-- Section Divider -->
                <div class="section-divider"></div>
                
                <!-- Low Stock Alert Section -->
                <c:if test="${not empty report.lowStockList}">
                    <div class="card mb-4">
                        <div class="card-header" style="background: linear-gradient(135deg, var(--accent-orange) 0%, var(--accent-yellow) 100%);">
                            <h5 class="mb-0">
                                <i class="fas fa-exclamation-triangle me-1"></i>
                                Low Stock Alert - Items Requiring Attention
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="alert alert-warning mb-3" role="alert">
                                <i class="fas fa-info-circle me-1"></i>
                                <strong>Note:</strong> The following items have stock levels at or below their reorder points and require immediate attention.
                            </div>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th><i class="fas fa-barcode me-1"></i> Item Code</th>
                                            <th><i class="fas fa-tag me-1"></i> Item Name</th>
                                            <th><i class="fas fa-folder me-1"></i> Category</th>
                                            <th><i class="fas fa-boxes me-1"></i> Current Stock</th>
                                            <th><i class="fas fa-exclamation-circle me-1"></i> Reorder Level</th>
                                            <th><i class="fas fa-arrow-down me-1"></i> Stock Deficit</th>
                                            <th><i class="fas fa-info-circle me-1"></i> Status</th>
                                            <th><i class="fas fa-bell me-1"></i> Priority</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${report.lowStockList}" var="item">
                                            <tr class="${item.currentStock == 0 ? 'table-danger' : 'table-warning'}">
                                                <td>${item.itemCode}</td>
                                                <td class="fw-bold">${item.itemName}</td>
                                                <td>${item.category}</td>
                                                <td class="fw-bold ${item.currentStock == 0 ? 'text-danger' : 'text-warning'}">
                                                    ${item.currentStock}
                                                </td>
                                                <td>${item.reorderLevel}</td>
                                                <td class="text-danger fw-bold">
                                                    -${item.reorderLevel - item.currentStock}
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${item.currentStock == 0}">
                                                            <span class="badge bg-danger">
                                                                <i class="fas fa-times-circle me-1"></i> Out of Stock
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-warning">
                                                                <i class="fas fa-exclamation-circle me-1"></i> Low Stock
                                                            </span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${item.currentStock == 0}">
                                                            <span class="badge bg-danger">
                                                                <i class="fas fa-fire me-1"></i> Critical
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-warning">
                                                                <i class="fas fa-flag me-1"></i> High
                                                            </span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </c:if>
                
                <!-- Report Footer -->
                <div class="text-center text-brown-medium mt-4">
                    <small>
                        <i class="fas fa-user me-1"></i>
                        Generated by: ${report.generatedBy} | 
                        <i class="fas fa-calendar-alt me-1"></i>
                        Generated on: <fmt:formatDate value="${report.generatedDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </small>
                </div>
            </div>
        </main>
    </div>
    
    <jsp:include page="/includes/footer.jsp" />
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/reports.js"></script>
    
    <script>
        // Category pie chart
        <c:if test="${not empty report.stockByCategory}">
        $(document).ready(function() {
            var ctx = document.getElementById('categoryChart').getContext('2d');
            var categoryChart = new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: [
                        <c:forEach items="${report.stockByCategory}" var="cat" varStatus="status">
                            '${cat.category}'<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    ],
                    datasets: [{
                        data: [
                            <c:forEach items="${report.stockByCategory}" var="cat" varStatus="status">
                                ${cat.itemCount}<c:if test="${!status.last}">,</c:if>
                            </c:forEach>
                        ],
                        backgroundColor: [
                            'rgba(93, 64, 55, 0.5)',    // Brown
                            'rgba(104, 159, 56, 0.5)',   // Green
                            'rgba(255, 160, 0, 0.5)',    // Orange
                            'rgba(121, 85, 72, 0.5)',    // Medium Brown
                            'rgba(161, 136, 127, 0.5)'   // Light Brown
                        ],
                        borderColor: [
                            'rgba(93, 64, 55, 1)',
                            'rgba(104, 159, 56, 1)',
                            'rgba(255, 160, 0, 1)',
                            'rgba(121, 85, 72, 1)',
                            'rgba(161, 136, 127, 1)'
                        ],
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'bottom'
                        }
                    }
                }
            });
        });
        </c:if>
        
        function printReport() {
            window.print();
        }
    </script>
</body>
</html>