<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bills - Pahana Edu</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css">
    
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
        
        .border-brown {
            border-color: var(--brown-pale) !important;
        }
        
        .text-brown-dark {
            color: var(--brown-dark) !important;
        }
        
        .text-brown-medium {
            color: var(--brown-medium) !important;
        }
        
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
        
        /* Statistics Cards */
        .stats-card {
            border: none;
            border-top: 3px solid;
            transition: transform 0.2s ease;
            background: white;
        }
        
        .stats-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.1) !important;
        }
        
        .stats-card.total {
            border-top-color: var(--brown-primary);
            background: linear-gradient(to bottom, white 0%, var(--cream) 100%);
        }
        
        .stats-card.paid {
            border-top-color: var(--accent-green);
            background: linear-gradient(to bottom, white 0%, #F1F8E9 100%);
        }
        
        .stats-card.pending {
            border-top-color: var(--accent-orange);
            background: linear-gradient(to bottom, white 0%, #FFF3E0 100%);
        }
        
        .stats-card.cancelled {
            border-top-color: var(--accent-red);
            background: linear-gradient(to bottom, white 0%, #FFEBEE 100%);
        }
        
        /* Table Styling */
        .table thead th {
            background-color: var(--brown-primary);
            color: white;
            font-weight: 500;
            border: none;
            padding: 12px;
        }
        
        .table tbody tr {
            transition: background-color 0.2s ease;
        }
        
        .table tbody tr:hover {
            background-color: var(--cream);
        }
        
        /* Status Badges */
        .badge.status-paid {
            background-color: var(--accent-green) !important;
        }
        
        .badge.status-pending {
            background-color: var(--accent-orange) !important;
        }
        
        .badge.status-cancelled {
            background-color: var(--accent-red) !important;
        }
        
        /* Quick Filter Buttons */
        .btn-filter-active {
            background-color: var(--brown-primary) !important;
            color: white !important;
            border-color: var(--brown-primary) !important;
        }
        
        .btn-filter-inactive {
            background-color: white !important;
            color: var(--brown-medium) !important;
            border-color: var(--brown-pale) !important;
        }
        
        .btn-filter-inactive:hover {
            background-color: var(--cream) !important;
            color: var(--brown-dark) !important;
            border-color: var(--brown-medium) !important;
        }
        
        /* Card Styling */
        .card {
            border: none;
            box-shadow: 0 2px 10px rgba(0,0,0,0.08);
        }
        
        .card-header {
            background-color: var(--brown-primary);
            color: white;
            font-weight: 500;
        }
        
        /* Form Controls */
        .form-control:focus, .form-select:focus {
            border-color: var(--brown-light);
            box-shadow: 0 0 0 0.2rem rgba(121, 85, 72, 0.25);
        }
        
        /* Pagination */
        .page-link {
            color: var(--brown-primary);
            border-color: var(--brown-pale);
        }
        
        .page-link:hover {
            color: white;
            background-color: var(--brown-light);
            border-color: var(--brown-light);
        }
        
        .page-item.active .page-link {
            background-color: var(--brown-primary);
            border-color: var(--brown-primary);
        }
        
        /* Bill Number Link */
        a.text-decoration-none {
            color: var(--brown-primary);
            font-weight: 600;
        }
        
        a.text-decoration-none:hover {
            color: var(--brown-dark);
        }
    </style>
</head>
<body>
    <jsp:include page="/includes/navbar.jsp" />
    
    <div class="container-fluid"> 
            <main class="px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom border-brown">
                    <h1 class="h2 text-brown-dark fw-bold">${pageTitle}</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <div class="btn-group me-2">
                            <a href="${pageContext.request.contextPath}/bill/create" class="btn btn-brown-primary">
                                <i class="fas fa-plus"></i> New Bill
                            </a>
                            <button type="button" class="btn btn-brown-outline" onclick="exportBills('csv')">
                                <i class="fas fa-download"></i> Export
                            </button>
                        </div>
                    </div>
                </div>
                
                <jsp:include page="/includes/messages.jsp" />
                
                <!-- Statistics Cards -->
                <div class="row mb-4 g-3">
                    <div class="col-md-3">
                        <div class="card stats-card total shadow-sm">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <h6 class="text-brown-medium mb-0">Total Bills</h6>
                                    <i class="fas fa-file-invoice" style="color: var(--brown-primary);"></i>
                                </div>
                                <h3 class="mb-1 text-brown-dark fw-bold">${statistics.totalBills}</h3>
                                <p class="mb-0 text-brown-medium small">
                                    <fmt:formatNumber value="${statistics.totalAmount}" type="currency" currencySymbol="Rs. "/>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card stats-card paid shadow-sm">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <h6 class="text-brown-medium mb-0">Paid Bills</h6>
                                    <i class="fas fa-check-circle" style="color: var(--accent-green);"></i>
                                </div>
                                <h3 class="mb-1 text-brown-dark fw-bold">${statistics.paidCount}</h3>
                                <p class="mb-0 small" style="color: var(--accent-green);">
                                    <fmt:formatNumber value="${statistics.paidAmount}" type="currency" currencySymbol="Rs. "/>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card stats-card pending shadow-sm">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <h6 class="text-brown-medium mb-0">Pending Bills</h6>
                                    <i class="fas fa-clock" style="color: var(--accent-orange);"></i>
                                </div>
                                <h3 class="mb-1 text-brown-dark fw-bold">${statistics.pendingCount}</h3>
                                <p class="mb-0 small" style="color: var(--accent-orange);">
                                    <fmt:formatNumber value="${statistics.pendingAmount}" type="currency" currencySymbol="Rs. "/>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card stats-card cancelled shadow-sm">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <h6 class="text-brown-medium mb-0">Cancelled</h6>
                                    <i class="fas fa-times-circle" style="color: var(--accent-red);"></i>
                                </div>
                                <h3 class="mb-1 text-brown-dark fw-bold">${statistics.cancelledCount}</h3>
                                <p class="mb-0 text-brown-medium small">
                                    ${statistics.cancelledCount > 0 ? statistics.cancelledCount : 0} bills
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Quick Filter Buttons -->
				<div class="row mb-4">
				    <div class="col-md-12">
				        <div class="btn-group btn-group-sm" role="group">
				            <a href="${pageContext.request.contextPath}/bill/list" 
				               class="btn ${filterType == null ? 'btn-filter-active' : 'btn-filter-inactive'}">All</a>
				            <a href="${pageContext.request.contextPath}/bill/list?filter=today" 
				               class="btn ${filterType == 'today' ? 'btn-filter-active' : 'btn-filter-inactive'}">Today</a>
				            <a href="${pageContext.request.contextPath}/bill/list?filter=pending" 
				               class="btn ${filterType == 'pending' ? 'btn-filter-active' : 'btn-filter-inactive'}">Pending</a>
				            <a href="${pageContext.request.contextPath}/bill/list?filter=overdue" 
				               class="btn ${filterType == 'overdue' ? 'btn-filter-active' : 'btn-filter-inactive'}">Overdue</a>
				        </div>
				    </div>
				</div>
                
                <!-- Bills Table -->
                <div class="card shadow">
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <table class="table table-hover mb-0">
                                <thead>
                                    <tr>
                                        <th>Bill No.</th>
                                        <th>Date</th>
                                        <th>Customer</th>
                                        <th>Items</th>
                                        <th>Amount</th>
                                        <th>Payment</th>
                                        <th>Status</th>
                                        <th width="80">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${not empty bills}">
                                            <c:forEach items="${bills}" var="bill">
                                                <tr data-bill-id="${bill.billId}">
                                                    <td>
                                                        <a href="${pageContext.request.contextPath}/bill/view?id=${bill.billId}" 
                                                           class="text-decoration-none">
                                                            <strong>${bill.billNumber}</strong>
                                                        </a>
                                                    </td>
                                                    <td>
                                                        <span class="text-brown-dark">
                                                            <fmt:formatDate value="${bill.billDate}" pattern="dd/MM/yyyy"/>
                                                        </span>
                                                        <br>
                                                        <small class="text-brown-medium">
                                                            <fmt:formatDate value="${bill.billTime}" pattern="hh:mm a"/>
                                                        </small>
                                                    </td>
                                                    <td>
                                                        <span class="text-brown-dark">${bill.customerName}</span>
                                                        <br>
                                                        <small class="text-brown-medium">${bill.customerAccountNumber}</small>
                                                    </td>
                                                    <td>
                                                        <span class="badge rounded-pill" style="background-color: var(--brown-light); color: white;">
                                                            ${bill.itemCount} items
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <strong class="text-brown-dark">
                                                            <fmt:formatNumber value="${bill.totalAmount}" 
                                                                            type="currency" currencySymbol="Rs. "/>
                                                        </strong>
                                                    </td>
                                                    <td>
                                                        <span class="text-brown-medium">
                                                            <i class="${bill.paymentMethodIcon} me-1"></i>
                                                            ${bill.paymentMethod}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${bill.paymentStatus == 'PAID'}">
                                                                <span class="badge rounded-pill status-paid">
                                                                    ${bill.paymentStatus}
                                                                </span>
                                                            </c:when>
                                                            <c:when test="${bill.paymentStatus == 'PENDING'}">
                                                                <span class="badge rounded-pill status-pending">
                                                                    ${bill.paymentStatus}
                                                                </span>
                                                            </c:when>
                                                            <c:when test="${bill.paymentStatus == 'CANCELLED'}">
                                                                <span class="badge rounded-pill status-cancelled">
                                                                    ${bill.paymentStatus}
                                                                </span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge rounded-pill" style="background-color: var(--brown-pale); color: var(--brown-dark);">
                                                                    ${bill.paymentStatus}
                                                                </span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <a href="${pageContext.request.contextPath}/bill/view?id=${bill.billId}" 
                                                           class="btn btn-sm btn-brown-primary" title="View">
                                                            <i class="fas fa-eye"></i>
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="8" class="text-center py-5">
                                                    <div style="color: var(--brown-light);">
                                                        <i class="fas fa-file-invoice fa-4x mb-3 d-block"></i>
                                                        <p class="text-brown-medium mb-3">No bills found</p>
                                                        <a href="${pageContext.request.contextPath}/bill/create" 
                                                           class="btn btn-brown-primary">
                                                            <i class="fas fa-plus"></i> Create First Bill
                                                        </a>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                        
                        <!-- Pagination -->
                        <c:if test="${totalBills > pageSize}">
                            <div class="card-footer bg-white border-top">
                                <nav aria-label="Bill pagination">
                                    <ul class="pagination justify-content-center mb-0">
                                        <c:set var="totalPages" value="${(totalBills + pageSize - 1) / pageSize}" />
                                        <c:set var="totalPages" value="${totalPages - (totalPages % 1)}" />
                                        
                                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                            <a class="page-link" href="?page=${currentPage - 1}&pageSize=${pageSize}">
                                                Previous
                                            </a>
                                        </li>
                                        
                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                <a class="page-link" href="?page=${i}&pageSize=${pageSize}">${i}</a>
                                            </li>
                                        </c:forEach>
                                        
                                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                            <a class="page-link" href="?page=${currentPage + 1}&pageSize=${pageSize}">
                                                Next
                                            </a>
                                        </li>
                                    </ul>
                                </nav>
                            </div>
                        </c:if>
                    </div>
                </div>
            </main>
    </div>
    
    <jsp:include page="/includes/footer.jsp" />
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/moment/min/moment.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.js"></script>
    
    <script>
        $(document).ready(function() {
            
            // Select all checkbox
            $('#selectAll').on('change', function() {
                $('.bill-select').prop('checked', this.checked);
                updateSelectedCount();
            });
            
            // Individual checkbox change
            $('.bill-select').on('change', function() {
                updateSelectedCount();
            });
        });
        
        function updateSelectedCount() {
            var count = $('.bill-select:checked').length;
            $('#selectedCount').text(count);
            if (count > 0) {
                $('.bulk-select-info').show();
            } else {
                $('.bulk-select-info').hide();
            }
        }
        
        function exportBills(format) {
            var params = window.location.search + (window.location.search ? '&' : '?') + 'export=' + format;
            window.location.href = '${pageContext.request.contextPath}/bill/list' + params;
        }
    </script>
</body>
</html>