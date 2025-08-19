<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bill #${bill.billNumber} - Pahana Edu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/print.css">
    <style>
        /* Inline styles for print view */
        @media print {
            .no-print { display: none !important; }
            body { margin: 0; }
            .bill-container { box-shadow: none !important; }
        }
    </style>
</head>
<body>
    <div class="bill-container">
        <!-- Header -->
        <div class="bill-header">
            <div class="company-info">
                <h1>${companyName}</h1>
                <p>${companyAddress}</p>
                <p>Tel: ${companyPhone} | Email: ${companyEmail}</p>
            </div>
            <div class="bill-title">
                <h2>INVOICE</h2>
                <p>Bill No: ${bill.billNumber}</p>
            </div>
        </div>
        
        <!-- Bill Info -->
        <div class="bill-info">
            <div class="customer-info">
                <h3>Bill To:</h3>
                <p><strong>${bill.customer.customerName}</strong></p>
                <p>${bill.customer.address}</p>
                <c:if test="${not empty bill.customer.city}">
                    <p>${bill.customer.city} ${bill.customer.postalCode}</p>
                </c:if>
                <p>Tel: ${bill.customer.telephone}</p>
            </div>
            <div class="bill-details">
                <table>
                    <tr>
                        <td><strong>Date:</strong></td>
                        <td><fmt:formatDate value="${bill.billDate}" pattern="yyyy-MM-dd"/></td>
                    </tr>
                    <tr>
                        <td><strong>Time:</strong></td>
                        <td><fmt:formatDate value="${bill.billTime}" pattern="HH:mm:ss"/></td>
                    </tr>
                    <tr>
                        <td><strong>Account No:</strong></td>
                        <td>${bill.customer.accountNumber}</td>
                    </tr>
                    <tr>
                        <td><strong>Payment:</strong></td>
                        <td>${bill.paymentMethod}</td>
                    </tr>
                </table>
            </div>
        </div>
        
        <!-- Items Table -->
        <div class="items-section">
            <table class="items-table">
                <thead>
                    <tr>
                        <th width="5%">#</th>
                        <th width="15%">Item Code</th>
                        <th width="35%">Description</th>
                        <th width="10%">Qty</th>
                        <th width="15%">Unit Price</th>
                        <th width="10%">Discount</th>
                        <th width="15%">Total</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${bill.billItems}" var="item" varStatus="status">
                        <tr>
                            <td>${status.count}</td>
                            <td>${item.item.itemCode}</td>
                            <td>${item.item.itemName}</td>
                            <td class="text-center">${item.quantity}</td>
                            <td class="text-right">
                                <fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/>
                            </td>
                            <td class="text-center">
                                <c:if test="${item.discountPercentage > 0}">
                                    ${item.discountPercentage}%
                                </c:if>
                                <c:if test="${item.discountPercentage == 0}">
                                    -
                                </c:if>
                            </td>
                            <td class="text-right">
                                <fmt:formatNumber value="${item.totalPrice}" pattern="#,##0.00"/>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        
        <!-- Summary -->
        <div class="bill-summary">
            <div class="summary-left">
                <div class="payment-info">
                    <p><strong>Payment Status:</strong> 
                        <span class="status-${bill.paymentStatus.toLowerCase()}">${bill.paymentStatus}</span>
                    </p>
                    <c:if test="${not empty bill.notes}">
                        <p><strong>Notes:</strong> ${bill.notes}</p>
                    </c:if>
                </div>
            </div>
            <div class="summary-right">
                <table class="summary-table">
                    <tr>
                        <td>Subtotal:</td>
                        <td><fmt:formatNumber value="${bill.subtotal}" pattern="#,##0.00"/></td>
                    </tr>
                    <c:if test="${bill.discountAmount > 0}">
                        <tr>
                            <td>Discount (${bill.discountPercentage}%):</td>
                            <td>- <fmt:formatNumber value="${bill.discountAmount}" pattern="#,##0.00"/></td>
                        </tr>
                    </c:if>
                    <c:if test="${bill.taxAmount > 0}">
                        <tr>
                            <td>Tax (${bill.taxPercentage}%):</td>
                            <td>+ <fmt:formatNumber value="${bill.taxAmount}" pattern="#,##0.00"/></td>
                        </tr>
                    </c:if>
                    <tr class="total-row">
                        <td><strong>Total Amount:</strong></td>
                        <td><strong>LKR <fmt:formatNumber value="${bill.totalAmount}" pattern="#,##0.00"/></strong></td>
                    </tr>
                </table>
            </div>
        </div>
        
        <!-- Footer -->
        <div class="bill-footer">
            <div class="thank-you">
                <p>Thank you for your business!</p>
            </div>
            <div class="signature-section">
                <div class="signature">
                    <p>_____________________</p>
                    <p>Authorized Signature</p>
                </div>
                <div class="signature">
                    <p>_____________________</p>
                    <p>Customer Signature</p>
                </div>
            </div>
            <div class="footer-info">
                <p>This is a computer generated invoice.</p>
                <p>Printed on: ${currentDate}</p>
            </div>
        </div>
    </div>
    
    <!-- Print Button (won't show when printing) -->
    <div class="no-print print-actions">
        <button onclick="window.print();" class="btn-print">Print Bill</button>
        <button onclick="window.close();" class="btn-close">Close</button>
        <a href="${pageContext.request.contextPath}/bill/view/${bill.billId}" class="btn-back">Back to Bill</a>
    </div>
    
    <script>
        // Auto-print on load (optional)
        // window.onload = function() { window.print(); }
    </script>
</body>
</html>