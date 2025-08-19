<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sales Report - Pahana Edu</title>
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
        
        .stat-card.success {
            border-left-color: var(--accent-green);
        }
        
        .stat-card.info {
            border-left-color: var(--accent-blue);
        }
        
        .stat-card.warning {
            border-left-color: var(--accent-orange);
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
            background-color: var(--accent-orange) !important;
        }
        
        .bg-danger {
            background-color: var(--accent-red) !important;
        }
        
        .bg-info {
            background-color: var(--accent-blue) !important;
        }
        
        /* Payment Breakdown */
        .payment-breakdown {
            display: flex;
            flex-wrap: wrap;
            gap: 1rem;
        }
        
        .payment-item {
            flex: 1;
            min-width: 120px;
            background: white;
            border-radius: 8px;
            padding: 1rem;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
            text-align: center;
        }
        
        .payment-item .amount {
            font-weight: 600;
            font-size: 1.1rem;
            color: var(--brown-primary);
        }
        
        .payment-item .label {
            color: var(--brown-medium);
            font-size: 0.9rem;
        }
        
        /* Print Styles */
        @media print {
            body {
                background-color: white !important;
                color: black !important;
            }
            
            .no-print {
                display: none !important;
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
                    <i class="fas fa-chart-line me-2" style="color: var(--brown-medium);"></i>
                    Sales Report
                </h1>
                <div class="btn-toolbar mb-2 mb-md-0 no-print">
                    <div class="btn-group me-2">
                        <button type="button" class="btn btn-sm btn-outline-secondary" onclick="window.print()">
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
            
            <!-- Date Selection -->
            <div class="card mb-4 no-print">
                <div class="card-body">
                    <!-- Quick filters -->
                    <div class="btn-group mb-3" role="group">
                        <a href="${pageContext.request.contextPath}/report/sales?type=today" 
                           class="btn ${reportType eq 'today' ? 'btn-primary' : 'btn-outline-primary'}">
                            <i class="fas fa-calendar-day me-1"></i> Today
                        </a>
                        <a href="${pageContext.request.contextPath}/report/sales?type=month" 
                           class="btn ${reportType eq 'month' ? 'btn-primary' : 'btn-outline-primary'}">
                            <i class="fas fa-calendar-alt me-1"></i> This Month
                        </a>
                        <button type="button" class="btn btn-outline-primary" onclick="$('#customDateForm').toggle()">
                            <i class="fas fa-calendar-week me-1"></i> Custom Range
                        </button>
                    </div>
                    
                    <!-- Custom date range form -->
                    <form id="customDateForm" method="get" action="${pageContext.request.contextPath}/report/sales" 
                          class="row g-3" style="${empty reportType or (reportType ne 'today' and reportType ne 'month') ? '' : 'display:none;'}">
                        <div class="col-md-4">
                            <label for="startDate" class="form-label">Start Date</label>
                            <input type="date" class="form-control" id="startDate" name="startDate" 
                                   value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>" required>
                        </div>
                        <div class="col-md-4">
                            <label for="endDate" class="form-label">End Date</label>
                            <input type="date" class="form-control" id="endDate" name="endDate" 
                                   value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>"
                                   max="<fmt:formatDate value="<%=new java.util.Date()%>" pattern="yyyy-MM-dd"/>" required>
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
                        Generated on: <fmt:formatDate value="${report.generatedDate}" pattern="dd MMMM yyyy, hh:mm a"/>
                    </p>
                </div>
                
                <!-- Summary Cards -->
                <div class="row mb-4">
                    <div class="col-md-3">
                        <div class="card stat-card primary h-100">
                            <div class="card-body">
                                <h6 class="text-muted">
                                    <i class="fas fa-coins me-1"></i> Total Sales
                                </h6>
                                <h3 class="mb-0 text-brown-dark">
                                    LKR <fmt:formatNumber value="${report.totalSales}" pattern="#,##0.00"/>
                                </h3>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card stat-card success h-100">
                            <div class="card-body">
                                <h6 class="text-muted">
                                    <i class="fas fa-file-invoice me-1"></i> Total Bills
                                </h6>
                                <h3 class="mb-0 text-brown-dark">${report.totalBills}</h3>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card stat-card info h-100">
                            <div class="card-body">
                                <h6 class="text-muted">
                                    <i class="fas fa-users me-1"></i> Customers Served
                                </h6>
                                <h3 class="mb-0 text-brown-dark">${report.totalCustomers}</h3>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card stat-card warning h-100">
                            <div class="card-body">
                                <h6 class="text-muted">
                                    <i class="fas fa-calculator me-1"></i> Average Bill Value
                                </h6>
                                <h3 class="mb-0 text-brown-dark">
                                    LKR <fmt:formatNumber value="${report.averageBillValue}" pattern="#,##0.00"/>
                                </h3>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Sales Details Table -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="fas fa-list me-1"></i>
                            Sales Details
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th><i class="fas fa-hashtag me-1"></i> Bill No</th>
                                        <th><i class="fas fa-calendar me-1"></i> Date & Time</th>
                                        <th><i class="fas fa-user me-1"></i> Customer</th>
                                        <th><i class="fas fa-boxes me-1"></i> Items</th>
                                        <th><i class="fas fa-money-bill-wave me-1"></i> Subtotal</th>
                                        <th><i class="fas fa-tag me-1"></i> Discount</th>
                                        <th><i class="fas fa-coins me-1"></i> Total Amount</th>
                                        <th><i class="fas fa-credit-card me-1"></i> Payment Method</th>
                                        <th><i class="fas fa-info-circle me-1"></i> Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${bills}" var="bill">
                                        <tr>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/bill/view?id=${bill.billId}" class="text-brown-primary">
                                                    ${bill.billNumber}
                                                </a>
                                            </td>
                                            <td>
                                                <fmt:formatDate value="${bill.billDate}" pattern="dd/MM/yyyy"/>
                                                <fmt:formatDate value="${bill.billTime}" pattern="hh:mm a"/>
                                            </td>
                                            <td>${bill.customerName}</td>
                                            <td>${bill.itemCount}</td>
                                            <td>LKR <fmt:formatNumber value="${bill.subtotal}" pattern="#,##0.00"/></td>
                                            <td>LKR <fmt:formatNumber value="${bill.discountAmount}" pattern="#,##0.00"/></td>
                                            <td class="fw-bold">LKR <fmt:formatNumber value="${bill.totalAmount}" pattern="#,##0.00"/></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${bill.paymentMethod eq 'CASH'}">
                                                        <span class="badge bg-success">
                                                            <i class="fas fa-money-bill-wave me-1"></i> Cash
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${bill.paymentMethod eq 'CARD'}">
                                                        <span class="badge bg-info">
                                                            <i class="fas fa-credit-card me-1"></i> Card
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${bill.paymentMethod eq 'CHEQUE'}">
                                                        <span class="badge bg-primary">
                                                            <i class="fas fa-money-check me-1"></i> Cheque
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${bill.paymentMethod eq 'BANK_TRANSFER'}">
                                                        <span class="badge bg-warning">
                                                            <i class="fas fa-university me-1"></i> Bank Transfer
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-secondary">
                                                            ${bill.paymentMethod}
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <span class="badge bg-${bill.paymentStatusClass}">
                                                    <i class="fas ${bill.paymentStatusClass eq 'success' ? 'fa-check-circle' : 'fa-exclamation-circle'} me-1"></i>
                                                    ${bill.paymentStatus}
                                                </span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty bills}">
                                        <tr>
                                            <td colspan="9" class="text-center empty-state">
                                                <i class="fas fa-inbox fa-2x mb-2" style="color: var(--brown-lighter);"></i>
                                                <p class="text-brown-medium">No bills found for the selected period</p>
                                            </td>
                                        </tr>
                                    </c:if>
                                </tbody>
                                <c:if test="${not empty bills}">
                                    <tfoot>
                                        <tr class="fw-bold">
                                            <td colspan="6" class="text-end">Total:</td>
                                            <td>LKR <fmt:formatNumber value="${report.totalSales}" pattern="#,##0.00"/></td>
                                            <td colspan="2"></td>
                                        </tr>
                                    </tfoot>
                                </c:if>
                            </table>
                        </div>
                    </div>
                </div>
                
                <!-- Payment Method Breakdown -->
                <c:if test="${not empty report.salesByCategory}">
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5 class="mb-0">
                                <i class="fas fa-credit-card me-1"></i>
                                Payment Method Breakdown
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="payment-breakdown">
                                <c:forEach items="${report.salesByCategory}" var="payment">
                                    <div class="payment-item">
                                        <div class="amount">
                                            LKR <fmt:formatNumber value="${payment.amount}" pattern="#,##0.00"/>
                                        </div>
                                        <div class="label">
                                            <c:choose>
                                                <c:when test="${payment.method eq 'CASH'}">
                                                    <i class="fas fa-money-bill-wave me-1"></i> Cash
                                                </c:when>
                                                <c:when test="${payment.method eq 'CARD'}">
                                                    <i class="fas fa-credit-card me-1"></i> Card
                                                </c:when>
                                                <c:when test="${payment.method eq 'CHEQUE'}">
                                                    <i class="fas fa-money-check me-1"></i> Cheque
                                                </c:when>
                                                <c:when test="${payment.method eq 'BANK_TRANSFER'}">
                                                    <i class="fas fa-university me-1"></i> Bank Transfer
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="fas fa-money-bill-alt me-1"></i> ${payment.method}
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </c:if>
                
                <!-- Daily Breakdown for Monthly Reports -->
                <c:if test="${not empty report.topSellingItems}">
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5 class="mb-0">
                                <i class="fas fa-calendar-day me-1"></i>
                                Daily Breakdown
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-sm">
                                    <thead>
                                        <tr>
                                            <th><i class="fas fa-calendar me-1"></i> Date</th>
                                            <th><i class="fas fa-file-invoice me-1"></i> Number of Bills</th>
                                            <th><i class="fas fa-coins me-1"></i> Total Sales</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${report.topSellingItems}" var="daily">
                                            <tr>
                                                <td>${daily.date}</td>
                                                <td>${daily.bills}</td>
                                                <td>LKR <fmt:formatNumber value="${daily.sales}" pattern="#,##0.00"/></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                    <tfoot>
                                        <tr class="fw-bold">
                                            <td>Total</td>
                                            <td>${report.totalBills}</td>
                                            <td>LKR <fmt:formatNumber value="${report.totalSales}" pattern="#,##0.00"/></td>
                                        </tr>
                                    </tfoot>
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
    
    <script>
        $(document).ready(function() {
            // Validate date range
            $('#customDateForm').on('submit', function(e) {
                var startDate = new Date($('#startDate').val());
                var endDate = new Date($('#endDate').val());
                
                if (startDate > endDate) {
                    e.preventDefault();
                    alert('Start date cannot be after end date');
                    return false;
                }
                
                // Check if date range is not more than 1 year
                var diffTime = Math.abs(endDate - startDate);
                var diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
                
                if (diffDays > 365) {
                    e.preventDefault();
                    alert('Date range cannot exceed 365 days');
                    return false;
                }
                
                return true;
            });
        });
    </script>
</body>
</html>