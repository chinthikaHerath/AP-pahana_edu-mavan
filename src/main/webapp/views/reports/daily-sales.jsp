<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Daily Sales Report - Pahana Edu</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <style>
        .stat-card {
            border-left: 4px solid;
            transition: transform 0.2s;
        }
        .stat-card:hover {
            transform: translateY(-5px);
        }
        .stat-card.primary { border-left-color: #007bff; }
        .stat-card.success { border-left-color: #28a745; }
        .stat-card.info { border-left-color: #17a2b8; }
        .stat-card.warning { border-left-color: #ffc107; }
    </style>
</head>
<body>
    <jsp:include page="/includes/navbar.jsp" />
    
    <div class="container-fluid">
            <main class="px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Daily Sales Report</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <div class="btn-group me-2">
                            <button type="button" class="btn btn-sm btn-outline-secondary" onclick="printReport()">
                                <i class="fas fa-print"></i> Print
                            </button>
                            <a href="?date=<fmt:formatDate value="${reportDate}" pattern="yyyy-MM-dd"/>&export=csv" 
                               class="btn btn-sm btn-outline-secondary">
                                <i class="fas fa-download"></i> Export CSV
                            </a>
                        </div>
                    </div>
                </div>
                
                <jsp:include page="/includes/messages.jsp" />
                
                <!-- Date Selection -->
                <div class="card mb-4">
                    <div class="card-body">
                        <form method="get" action="${pageContext.request.contextPath}/report/daily-sales" class="row g-3">
                            <div class="col-md-4">
                                <label for="date" class="form-label">Select Date</label>
                                <input type="date" class="form-control" id="date" name="date" 
                                       value="<fmt:formatDate value="${reportDate}" pattern="yyyy-MM-dd"/>"
                                       max="<fmt:formatDate value="<%=new java.util.Date()%>" pattern="yyyy-MM-dd"/>">
                            </div>
                            <div class="col-md-2">
                                <label class="form-label">&nbsp;</label>
                                <button type="submit" class="btn btn-primary d-block">
                                    <i class="fas fa-search"></i> Generate
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
                
                <!-- Report Content -->
                <div id="reportContent">
                    <!-- Report Header -->
                    <div class="text-center mb-4">
                        <h3>${report.reportTitle}</h3>
                        <p class="text-muted">
                            Date: <fmt:formatDate value="${report.startDate}" pattern="EEEE, MMMM dd, yyyy"/>
                        </p>
                    </div>
                    
                    <!-- Summary Cards -->
                    <div class="row mb-4">
                        <div class="col-md-3">
                            <div class="card stat-card primary">
                                <div class="card-body">
                                    <h6 class="text-muted">Total Sales</h6>
                                    <h3 class="mb-0">LKR <fmt:formatNumber value="${report.totalSales}" pattern="#,##0.00"/></h3>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card stat-card success">
                                <div class="card-body">
                                    <h6 class="text-muted">Total Bills</h6>
                                    <h3 class="mb-0">${report.totalBills}</h3>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card stat-card info">
                                <div class="card-body">
                                    <h6 class="text-muted">Customers Served</h6>
                                    <h3 class="mb-0">${report.totalCustomers}</h3>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="card stat-card warning">
                                <div class="card-body">
                                    <h6 class="text-muted">Average Bill Value</h6>
                                    <h3 class="mb-0">LKR <fmt:formatNumber value="${report.averageBillValue}" pattern="#,##0.00"/></h3>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Hourly Breakdown Chart -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5 class="mb-0">Hourly Sales Breakdown</h5>
                        </div>
                        <div class="card-body">
                            <canvas id="hourlyChart" height="100"></canvas>
                        </div>
                    </div>
                    
                    <!-- Hourly Details Table -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5 class="mb-0">Hourly Details</h5>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th>Hour</th>
                                            <th>Number of Bills</th>
                                            <th>Sales Amount</th>
                                            <th>Percentage</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${report.hourlyBreakdown}" var="hourData">
                                            <tr>
                                                <td>${hourData.hour}</td>
                                                <td>${hourData.bills}</td>
                                                <td>LKR <fmt:formatNumber value="${hourData.sales}" pattern="#,##0.00"/></td>
                                                <td>
                                                    <c:if test="${report.totalSales > 0}">
                                                        <fmt:formatNumber value="${(hourData.sales / report.totalSales) * 100}" 
                                                                         pattern="#0.0"/>%
                                                    </c:if>
                                                    <c:if test="${report.totalSales == 0}">0.0%</c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                    <tfoot>
                                        <tr class="fw-bold">
                                            <td>Total</td>
                                            <td>${report.totalBills}</td>
                                            <td>LKR <fmt:formatNumber value="${report.totalSales}" pattern="#,##0.00"/></td>
                                            <td>100.0%</td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Report Footer -->
                    <div class="text-center text-muted mt-4">
                        <small>
                            Generated by: ${report.generatedBy} | 
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
        // Hourly chart
        $(document).ready(function() {
            var ctx = document.getElementById('hourlyChart').getContext('2d');
            var hourlyChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: [
                        <c:forEach items="${report.hourlyBreakdown}" var="hour" varStatus="status">
                            '${hour.hour}'<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    ],
                    datasets: [{
                        label: 'Sales (LKR)',
                        data: [
                            <c:forEach items="${report.hourlyBreakdown}" var="hour" varStatus="status">
                                ${hour.sales}<c:if test="${!status.last}">,</c:if>
                            </c:forEach>
                        ],
                        backgroundColor: 'rgba(54, 162, 235, 0.5)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                callback: function(value) {
                                    return 'LKR ' + value.toLocaleString();
                                }
                            }
                        }
                    },
                    plugins: {
                        tooltip: {
                            callbacks: {
                                label: function(context) {
                                    return 'Sales: LKR ' + context.parsed.y.toLocaleString();
                                }
                            }
                        }
                    }
                }
            });
        });
        
        function printReport() {
            window.print();
        }
    </script>
</body>
</html>