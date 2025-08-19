<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Details - Pahana Edu</title>
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
        
        .card {
            border: none;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            background: white;
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
        }
        
        .badge.bg-success {
            background-color: var(--accent-green) !important;
        }
        
        .badge.bg-secondary {
            background-color: var(--brown-light) !important;
        }
        
        .table-borderless th {
            color: var(--brown-medium);
            font-weight: 600;
            padding: 0.5rem 0;
        }
        
        .table-borderless td {
            color: var(--brown-dark);
            padding: 0.5rem 0;
        }
        
        h5 {
            color: var(--brown-dark);
            font-weight: 600;
            position: relative;
            padding-bottom: 0.5rem;
        }
        
        h5:after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 0;
            width: 40px;
            height: 2px;
            background: var(--brown-primary);
        }
        
        hr {
            border-color: var(--brown-pale);
            opacity: 0.5;
        }
        
        /* Purchase Summary Cards */
        .summary-card {
            background: linear-gradient(135deg, white 0%, var(--cream) 100%);
            border: none;
            border-radius: 10px;
            transition: transform 0.2s ease;
            overflow: hidden;
            position: relative;
        }
        
        .summary-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }
        
        .summary-card.total-purchases {
            border-top: 3px solid var(--brown-primary);
        }
        
        .summary-card.total-purchases h3 {
            color: var(--brown-primary) !important;
        }
        
        .summary-card.total-amount {
            border-top: 3px solid var(--accent-green);
        }
        
        .summary-card.total-amount h3 {
            color: var(--accent-green) !important;
        }
        
        .summary-card.average-purchase {
            border-top: 3px solid var(--accent-orange);
        }
        
        .summary-card.average-purchase h3 {
            color: var(--accent-orange) !important;
        }
        
        .summary-card .card-body {
            padding: 1.5rem;
        }
        
        .summary-card h3 {
            font-weight: 700;
            font-size: 1.75rem;
            margin-bottom: 0.5rem;
        }
        
        .summary-card p {
            color: var(--brown-medium);
            font-weight: 500;
            font-size: 0.9rem;
            text-transform: uppercase;
            letter-spacing: 0.5px;
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
        }
        
        .btn-danger {
            background-color: var(--accent-red);
            border-color: var(--accent-red);
            font-weight: 500;
        }
        
        .btn-danger:hover {
            background-color: #B71C1C;
            border-color: #B71C1C;
        }
        
        .btn-secondary {
            background-color: var(--brown-light);
            border-color: var(--brown-light);
            color: white;
            font-weight: 500;
        }
        
        .btn-secondary:hover {
            background-color: var(--brown-medium);
            border-color: var(--brown-medium);
        }
        
        /* Info Section Icons */
        .info-icon {
            display: inline-block;
            width: 20px;
            text-align: center;
            color: var(--brown-light);
            margin-right: 0.5rem;
        }
        
        /* Add subtle background to info sections */
        .info-section {
            background: var(--cream);
            padding: 1.5rem;
            border-radius: 8px;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <jsp:include page="/includes/navbar.jsp" />
    
    <div class="container-fluid"> 
            <main class="px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom border-brown">
                    <h1 class="h2 text-brown-dark fw-bold">Customer Details</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <a href="${pageContext.request.contextPath}/customer/list" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back to List
                        </a>
                    </div>
                </div>
                
                <jsp:include page="/includes/messages.jsp" />
                
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <div class="d-flex justify-content-between align-items-center">
                            <h6 class="m-0 font-weight-bold">
                                <i class="fas fa-user me-2"></i>${customer.customerName}
                            </h6>
                            <span class="badge ${customer.active ? 'bg-success' : 'bg-secondary'} px-3 py-2">
                                ${customer.active ? 'Active' : 'Inactive'}
                            </span>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="info-section">
                                    <h5 class="mb-3">Basic Information</h5>
                                    <table class="table table-sm table-borderless">
                                        <tr>
                                            <th width="40%">
                                                <i class="fas fa-id-badge info-icon"></i>Account Number:
                                            </th>
                                            <td class="fw-bold">${customer.accountNumber}</td>
                                        </tr>
                                        <tr>
                                            <th>
                                                <i class="fas fa-id-card info-icon"></i>NIC Number:
                                            </th>
                                            <td>${customer.nicNumber}</td>
                                        </tr>
                                        <tr>
                                            <th>
                                                <i class="fas fa-calendar-plus info-icon"></i>Registered On:
                                            </th>
                                            <td><fmt:formatDate value="${customer.registrationDate}" pattern="yyyy-MM-dd"/></td>
                                        </tr>
                                        <tr>
                                            <th>
                                                <i class="fas fa-user-plus info-icon"></i>Registered By:
                                            </th>
                                            <td>${customer.createdByUsername}</td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="info-section">
                                    <h5 class="mb-3">Contact Information</h5>
                                    <table class="table table-sm table-borderless">
                                        <tr>
                                            <th width="40%">
                                                <i class="fas fa-map-marker-alt info-icon"></i>Address:
                                            </th>
                                            <td>${customer.address}</td>
                                        </tr>
                                        <tr>
                                            <th>
                                                <i class="fas fa-city info-icon"></i>City:
                                            </th>
                                            <td>${customer.city}</td>
                                        </tr>
                                        <tr>
                                            <th>
                                                <i class="fas fa-mail-bulk info-icon"></i>Postal Code:
                                            </th>
                                            <td>${customer.postalCode}</td>
                                        </tr>
                                        <tr>
                                            <th>
                                                <i class="fas fa-phone info-icon"></i>Telephone:
                                            </th>
                                            <td>${customer.telephone}</td>
                                        </tr>
                                        <tr>
                                            <th>
                                                <i class="fas fa-mobile-alt info-icon"></i>Mobile:
                                            </th>
                                            <td>${customer.mobile}</td>
                                        </tr>
                                        <tr>
                                            <th>
                                                <i class="fas fa-envelope info-icon"></i>Email:
                                            </th>
                                            <td>${customer.email}</td>
                                        </tr>
                                    </table>
                                </div>
                            </div>
                        </div>
                        
                        <hr class="my-4">
                        
                        <!-- Purchase Summary Section -->
                        <div class="row">
                            <div class="col-md-12">
                                <h5 class="mb-4">Purchase Summary</h5>
                                <div class="row text-center g-3">
                                    <div class="col-md-4">
                                        <div class="card summary-card total-purchases">
                                            <div class="card-body">
                                                <i class="fas fa-shopping-cart mb-2" style="font-size: 2rem; color: var(--brown-primary); opacity: 0.3;"></i>
                                                <h3>${customer.purchaseCount}</h3>
                                                <p class="mb-0">Total Purchases</p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="card summary-card total-amount">
                                            <div class="card-body">
                                                <i class="fas fa-dollar-sign mb-2" style="font-size: 2rem; color: var(--accent-green); opacity: 0.3;"></i>
                                                <h3>LKR <fmt:formatNumber value="${customer.totalPurchases}" pattern="#,##0.00"/></h3>
                                                <p class="mb-0">Total Amount</p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="card summary-card average-purchase">
                                            <div class="card-body">
                                                <i class="fas fa-chart-line mb-2" style="font-size: 2rem; color: var(--accent-orange); opacity: 0.3;"></i>
                                                <h3>
                                                    <c:choose>
                                                        <c:when test="${customer.purchaseCount > 0}">
                                                            LKR <fmt:formatNumber value="${customer.totalPurchases / customer.purchaseCount}" pattern="#,##0.00"/>
                                                        </c:when>
                                                        <c:otherwise>0.00</c:otherwise>
                                                    </c:choose>
                                                </h3>
                                                <p class="mb-0">Average Purchase</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="mt-4 pt-3">
                            <a href="${pageContext.request.contextPath}/customer/edit?id=${customer.customerId}" 
                               class="btn btn-primary me-2">
                                <i class="fas fa-edit"></i> Edit Customer
                            </a>
                            <c:if test="${customer.active}">
                                <form action="${pageContext.request.contextPath}/customer/delete" method="post" 
                                      style="display:inline;" onsubmit="return confirm('Are you sure you want to deactivate this customer?');">
                                    <input type="hidden" name="id" value="${customer.customerId}">
                                    <button type="submit" class="btn btn-danger me-2">
                                        <i class="fas fa-user-slash"></i> Deactivate Customer
                                    </button>
                                </form>
                            </c:if>
                        </div>
                    </div>
                </div>
                
                
            </main>
    </div>
    
    <jsp:include page="/includes/footer.jsp" />
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>