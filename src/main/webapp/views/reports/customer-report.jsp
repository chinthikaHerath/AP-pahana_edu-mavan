<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Report - Pahana Edu</title>
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
            background:  linear-gradient(135deg, #135F3C 0%, #0CC46E 100%);
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
        
        .stat-card.success {
            border-left-color: var(--accent-green);
        }
        
        .stat-card.danger {
            border-left-color: var(--accent-red);
        }
        
        .stat-card.info {
            border-left-color: var(--accent-blue);
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
        .btn-outline-secondary {
            color: var(--brown-primary);
            border-color: var(--brown-primary);
        }
        
        .btn-outline-secondary:hover {
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
        
        .bg-danger {
            background-color: var(--accent-red) !important;
        }
        
        .bg-info {
            background-color: var(--accent-blue) !important;
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
            
            .stat-card {
                border: 1px solid #ddd !important;
            }
            
            .table thead th {
                background: var(--cream) !important;
                -webkit-print-color-adjust: exact;
                print-color-adjust: exact;
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
                    <i class="fas fa-users me-2" style="color: #135F3C;"></i>
                    Customer Report
                </h1>
                <div class="btn-toolbar mb-2 mb-md-0">
                    <div class="btn-group me-2">
                        <button type="button" class="btn btn-sm btn-outline-secondary" onclick="printReport()">
                            <i class="fas fa-print me-1"></i> Print
                        </button>
                        <a href="?startDate=<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>&endDate=<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>&export=csv" 
                           class="btn btn-sm btn-outline-secondary">
                            <i class="fas fa-download me-1"></i> Export CSV
                        </a>
                    </div>
                </div>
            </div>
            
            <jsp:include page="/includes/messages.jsp" />
            
            <!-- Date Range Selection -->
            <div class="card mb-4">
                <div class="card-body">
                    <form method="get" action="${pageContext.request.contextPath}/report/customer" class="row g-3">
                        <div class="col-md-4">
                            <label for="startDate" class="form-label">Start Date</label>
                            <input type="date" class="form-control" id="startDate" name="startDate" 
                                   value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>">
                        </div>
                        <div class="col-md-4">
                            <label for="endDate" class="form-label">End Date</label>
                            <input type="date" class="form-control" id="endDate" name="endDate" 
                                   value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>"
                                   max="<fmt:formatDate value="<%=new java.util.Date()%>" pattern="yyyy-MM-dd"/>">
                        </div>
                        <div class="col-md-2">
                            <label class="form-label">&nbsp;</label>
                            <button type="submit" class="btn btn-primary d-block">
                                <i class="fas fa-search me-1"></i> Generate
                            </button>
                        </div>
                    </form>
                </div>
            </div>
            
            <!-- Report Content -->
            <div id="reportContent">
                <!-- Report Header -->
                <div class="text-center mb-4">
                    <h3 class="text-brown-dark">${report.reportTitle}</h3>
                    <p class="text-brown-medium">
                        <i class="fas fa-calendar-alt me-1"></i>
                        <c:choose>
                            <c:when test="${not empty startDate and not empty endDate}">
                                Period: <fmt:formatDate value="${startDate}" pattern="MMM dd, yyyy"/> - 
                                <fmt:formatDate value="${endDate}" pattern="MMM dd, yyyy"/>
                            </c:when>
                            <c:otherwise>
                                All Time
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
                
                <!-- Summary Cards -->
                <div class="row mb-4">
                    <div class="col-md-3">
                        <div class="card stat-card primary h-100">
                            <div class="card-body">
                                <h6 class="text-muted">
                                    <i class="fas fa-users me-1"></i> Total Customers
                                </h6>
                                <h3 class="mb-0 text-brown-dark">${report.totalCustomers}</h3>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card stat-card success h-100">
                            <div class="card-body">
                                <h6 class="text-muted">
                                    <i class="fas fa-check-circle me-1"></i> Active Customers
                                </h6>
                                <h3 class="mb-0 text-brown-dark">${report.activeCustomers}</h3>
                                <small class="text-brown-medium">
                                    <c:if test="${report.totalCustomers > 0}">
                                        <fmt:formatNumber value="${(report.activeCustomers / report.totalCustomers) * 100}" 
                                                         pattern="#0.0"/>%
                                    </c:if>
                                </small>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card stat-card danger h-100">
                            <div class="card-body">
                                <h6 class="text-muted">
                                    <i class="fas fa-times-circle me-1"></i> Inactive Customers
                                </h6>
                                <h3 class="mb-0 text-brown-dark">${report.inactiveCustomers}</h3>
                                <small class="text-brown-medium">
                                    <c:if test="${report.totalCustomers > 0}">
                                        <fmt:formatNumber value="${(report.inactiveCustomers / report.totalCustomers) * 100}" 
                                                         pattern="#0.0"/>%
                                    </c:if>
                                </small>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card stat-card info h-100">
                            <div class="card-body">
                                <h6 class="text-muted">
                                    <i class="fas fa-user-plus me-1"></i> New This Month
                                </h6>
                                <h3 class="mb-0 text-brown-dark">${report.newCustomers}</h3>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Customer Distribution by City -->
                <c:if test="${not empty report.customersByCity}">
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5 class="mb-0">
                                <i class="fas fa-map-marker-alt me-1"></i>
                                Customer Distribution by City
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <canvas id="cityChart" height="200"></canvas>
                                </div>
                                <div class="col-md-6">
                                    <div class="table-responsive">
                                        <table class="table table-sm">
                                            <thead>
                                                <tr>
                                                    <th><i class="fas fa-city me-1"></i> City</th>
                                                    <th><i class="fas fa-users me-1"></i> Customers</th>
                                                    <th><i class="fas fa-percentage me-1"></i> Percentage</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${report.customersByCity}" var="city">
                                                    <tr>
                                                        <td>${city.city}</td>
                                                        <td>${city.customerCount}</td>
                                                        <td>
                                                            <fmt:formatNumber value="${city.percentage}" pattern="#0.0"/>%
                                                        </td>
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
                
                <!-- Top Customers -->
                <c:if test="${not empty report.topCustomers}">
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5 class="mb-0">
                                <i class="fas fa-star me-1"></i>
                                Recent Customers
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th><i class="fas fa-id-card me-1"></i> Account Number</th>
                                            <th><i class="fas fa-user me-1"></i> Customer Name</th>
                                            <th><i class="fas fa-coins me-1"></i> Total Purchases</th>
                                            <th><i class="fas fa-file-invoice me-1"></i> Number of Bills</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${report.topCustomers}" var="customer">
                                            <tr>
                                                <td>${customer.accountNumber}</td>
                                                <td>${customer.customerName}</td>
                                                <td class="fw-bold">LKR <fmt:formatNumber value="${customer.totalPurchases}" pattern="#,##0.00"/></td>
                                                <td>${customer.billCount}</td>
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
        // City distribution chart
        <c:if test="${not empty report.customersByCity}">
        $(document).ready(function() {
            var ctx = document.getElementById('cityChart').getContext('2d');
            var cityChart = new Chart(ctx, {
                type: 'doughnut',
                data: {
                    labels: [
                        <c:forEach items="${report.customersByCity}" var="city" varStatus="status">
                            '${city.city}'<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    ],
                    datasets: [{
                        data: [
                            <c:forEach items="${report.customersByCity}" var="city" varStatus="status">
                                ${city.customerCount}<c:if test="${!status.last}">,</c:if>
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