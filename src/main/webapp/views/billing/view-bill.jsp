<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bill #${bill.billNumber} - Pahana Edu</title>
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
        }
        
        body {
            background-color: var(--brown-bg) !important;
            color: var(--brown-dark);
        }
        
        /* Header Styling */
        .border-brown {
            border-color: var(--brown-pale) !important;
        }
        
        .text-brown-dark {
            color: var(--brown-dark) !important;
        }
        
        .text-brown-medium {
            color: var(--brown-medium) !important;
        }
        
        /* Bill Header */
        .bill-header {
            background: linear-gradient(135deg, var(--brown-primary) 0%, var(--brown-medium) 100%);
            color: white;
            padding: 2rem;
            border-radius: 10px 10px 0 0;
            margin: -1rem -1rem 0 -1rem;
        }
        
        .bill-header h2 {
            color: white;
            font-weight: 600;
        }
        
        .bill-header p {
            color: var(--cream);
            margin-bottom: 0;
        }
        
        .bill-status {
            display: inline-block;
            padding: 0.5rem 1rem;
            border-radius: 25px;
            font-weight: 500;
            background: rgba(255,255,255,0.2);
            color: white;
        }
        
        /* Cards */
        .card {
            border: none;
            box-shadow: 0 2px 10px rgba(0,0,0,0.08);
            margin-bottom: 1.5rem;
        }
        
        .card-header {
            background: linear-gradient(135deg, var(--brown-primary) 0%, var(--brown-medium) 100%);
            color: white;
            font-weight: 500;
            border: none;
            padding: 1rem 1.25rem;
        }
        
        .card-header h5 {
            margin: 0;
            font-size: 1.1rem;
            color: white;
        }
        
        /* Customer Info Card */
        .customer-info-card {
            border-left: 4px solid var(--brown-primary);
        }
        
        .customer-info-card .card-header {
            background: linear-gradient(135deg, var(--brown-medium) 0%, var(--brown-light) 100%);
        }
        
        /* Bill Info Rows */
        .bill-info-row {
            display: flex;
            justify-content: space-between;
            padding: 0.75rem 0;
            border-bottom: 1px solid var(--brown-pale);
        }
        
        .bill-info-row:last-child {
            border-bottom: none;
        }
        
        .bill-info-label {
            color: var(--brown-medium);
            font-weight: 500;
        }
        
        .bill-info-value {
            font-weight: 600;
            text-align: right;
            color: var(--brown-dark);
        }
        
        /* Items Table */
        .item-table thead th {
            background-color: var(--brown-primary);
            color: white;
            font-weight: 500;
            border: none;
            padding: 12px;
        }
        
        .item-table tbody tr {
            transition: background-color 0.2s ease;
        }
        
        .item-table tbody tr:hover {
            background-color: var(--cream);
        }
        
        .item-table td {
            padding: 12px;
            vertical-align: middle;
            color: var(--brown-dark);
        }
        
        /* Total Section */
        .total-section {
            background-color: var(--cream);
            padding: 1.5rem;
            border-radius: 8px;
            margin-top: 1rem;
        }
        
        .total-row {
            display: flex;
            justify-content: space-between;
            padding: 0.5rem 0;
            color: var(--brown-dark);
        }
        
        .total-row.grand-total {
            border-top: 2px solid var(--brown-primary);
            padding-top: 1rem;
            font-size: 1.25rem;
            font-weight: bold;
            color: var(--brown-primary);
        }
        
        .total-row.text-danger {
            color: var(--accent-red) !important;
        }
        
        /* Action Buttons */
        .action-button {
            min-width: 120px;
            font-weight: 500;
            margin: 0.25rem;
        }
        
        .btn-warning {
            background-color: var(--accent-orange);
            border-color: var(--accent-orange);
            color: white;
        }
        
        .btn-warning:hover {
            background-color: #E65100;
            border-color: #E65100;
            color: white;
        }
        
        .btn-success {
            background-color: var(--accent-green);
            border-color: var(--accent-green);
        }
        
        .btn-success:hover {
            background-color: #558B2F;
            border-color: #558B2F;
        }
        
        .btn-danger {
            background-color: var(--accent-red);
            border-color: var(--accent-red);
        }
        
        .btn-danger:hover {
            background-color: #B71C1C;
            border-color: #B71C1C;
        }
        
        .btn-secondary {
            background-color: var(--brown-light);
            border-color: var(--brown-light);
        }
        
        .btn-secondary:hover {
            background-color: var(--brown-medium);
            border-color: var(--brown-medium);
        }
        
        /* Quick Actions Buttons */
        .btn-outline-primary {
            color: var(--brown-primary);
            border-color: var(--brown-primary);
        }
        
        .btn-outline-primary:hover {
            background-color: var(--brown-primary);
            border-color: var(--brown-primary);
            color: white;
        }
        
        .btn-outline-danger {
            color: var(--accent-red);
            border-color: var(--accent-red);
        }
        
        .btn-outline-danger:hover {
            background-color: var(--accent-red);
            border-color: var(--accent-red);
            color: white;
        }
        
        .btn-outline-info {
            color: var(--brown-medium);
            border-color: var(--brown-medium);
        }
        
        .btn-outline-info:hover {
            background-color: var(--brown-medium);
            border-color: var(--brown-medium);
            color: white;
        }
        
        /* Modal Styles */
        .modal-header {
            background: linear-gradient(135deg, var(--brown-primary) 0%, var(--brown-medium) 100%);
            color: white;
            border: none;
        }
        
        .modal-header .modal-title {
            color: white;
            font-weight: 500;
        }
        
        .modal-header .btn-close {
            filter: brightness(0) invert(1);
        }
        
        .modal-body {
            background-color: white;
        }
        
        .modal-footer {
            background-color: var(--cream);
            border-top: 1px solid var(--brown-pale);
        }
        
        .form-control, .form-select {
            border-color: var(--brown-pale);
        }
        
        .form-control:focus, .form-select:focus {
            border-color: var(--brown-light);
            box-shadow: 0 0 0 0.2rem rgba(121, 85, 72, 0.25);
        }
        
        .form-label {
            color: var(--brown-dark);
            font-weight: 500;
        }
        
        /* Alert in Modal */
        .alert-warning {
            background-color: #FFF3E0;
            border-color: var(--accent-orange);
            color: var(--brown-dark);
        }
        
        .alert-warning i {
            color: var(--accent-orange);
        }
        
        /* Text Colors */
        .text-muted {
            color: var(--brown-light) !important;
        }
        
        .text-success {
            color: var(--accent-green) !important;
        }
        
        /* Print Styles */
        @media print {
            .no-print {
                display: none !important;
            }
            
            .bill-header {
                background: none !important;
                color: black !important;
                -webkit-print-color-adjust: exact;
            }
            
            .card {
                box-shadow: none !important;
                border: 1px solid #dee2e6 !important;
            }
            
            body {
                background: white !important;
            }
        }
        
        /* Gap utility for flexbox */
        .gap-2 {
            gap: 0.5rem !important;
        }
        
        /* Combined Actions Section Styles */
.combined-actions .action-group {
    margin-bottom: 1.5rem;
}

.combined-actions .action-group:last-child {
    margin-bottom: 0;
}

.combined-actions .action-group h6 {
    font-size: 0.9rem;
    text-transform: uppercase;
    letter-spacing: 0.5px;
    color: var(--brown-medium);
    border-bottom: 1px solid var(--brown-pale);
    padding-bottom: 0.5rem;
    margin-bottom: 1rem;
}

.combined-actions .btn {
    padding: 0.65rem 1rem;
    text-align: left;
    display: flex;
    align-items: center;
    transition: all 0.2s ease;
}

.combined-actions .btn i {
    width: 20px;
    text-align: center;
    margin-right: 10px;
}

.combined-actions .btn:hover {
    transform: translateX(3px);
}
    </style>
</head>
<body>
    <jsp:include page="/includes/navbar.jsp" />
    
    <div class="container-fluid">
            <main class="px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom border-brown no-print">
                    <h1 class="h2 text-brown-dark fw-bold">Bill Details</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <a href="${pageContext.request.contextPath}/bill/list" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back
                        </a>
                    </div>
                </div>
                
                <jsp:include page="/includes/messages.jsp" />
                
                <div class="row">
                    <div class="col-lg-8">
                        <!-- Bill Main Content -->
                        <div class="card mb-4">
                            <div class="bill-header">
                                <div class="row align-items-center">
                                    <div class="col-md-8">
                                        <h2 class="mb-1">Bill #${bill.billNumber}</h2>
                                        <p class="mb-0">
                                            <i class="far fa-calendar me-2"></i>
                                            <fmt:formatDate value="${bill.billDate}" pattern="EEEE, dd MMMM yyyy"/> at 
                                            <fmt:formatDate value="${bill.billTime}" pattern="hh:mm a"/>
                                        </p>
                                    </div>
                                    <div class="col-md-4 text-md-end">
                                        <span class="bill-status">
                                            <i class="${paymentStatus.iconClass}"></i> ${paymentStatus.displayName}
                                        </span>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="card-body">
                                <!-- Customer Information -->
                                <div class="row mb-4">
                                    <div class="col-md-6">
                                        <h5 class="text-brown-medium mb-3">
                                            <i class="fas fa-user me-2"></i>Bill To
                                        </h5>
                                        <h6 class="mb-2 text-brown-dark">${bill.customer.customerName}</h6>
                                        <p class="mb-1 text-brown-medium">
                                            <i class="fas fa-id-card me-2"></i>${bill.customer.accountNumber}
                                        </p>
                                        <p class="mb-1 text-brown-medium">
                                            <i class="fas fa-phone me-2"></i>${bill.customer.telephone}
                                        </p>
                                        <p class="mb-0 text-brown-medium">
                                            <i class="fas fa-map-marker-alt me-2"></i>${bill.customer.address}
                                        </p>
                                    </div>
                                    <div class="col-md-6">
                                        <h5 class="text-brown-medium mb-3">
                                            <i class="fas fa-info-circle me-2"></i>Bill Information
                                        </h5>
                                        <div class="bill-info-row">
                                            <span class="bill-info-label">Created By:</span>
                                            <span class="bill-info-value">${bill.userName}</span>
                                        </div>
                                        <div class="bill-info-row">
                                            <span class="bill-info-label">Payment Method:</span>
                                            <span class="bill-info-value">
                                                <i class="${paymentMethod.iconClass}"></i> ${paymentMethod.displayName}
                                            </span>
                                        </div>
                                        <c:if test="${not empty bill.notes}">
                                            <div class="bill-info-row">
                                                <span class="bill-info-label">Notes:</span>
                                                <span class="bill-info-value">${bill.notes}</span>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                                
                                <!-- Items Table -->
                                <h5 class="mb-3 text-brown-dark">
                                    <i class="fas fa-shopping-cart me-2"></i>Items
                                </h5>
                                <div class="table-responsive">
                                    <table class="table item-table">
                                        <thead>
                                            <tr>
                                                <th width="50">#</th>
                                                <th>Item</th>
                                                <th>Code</th>
                                                <th class="text-end">Price</th>
                                                <th class="text-center">Qty</th>
                                                <th class="text-end">Subtotal</th>
                                                <th class="text-end">Discount</th>
                                                <th class="text-end">Total</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${bill.billItems}" var="item" varStatus="status">
                                                <tr>
                                                    <td>${status.index + 1}</td>
                                                    <td>${item.itemName}</td>
                                                    <td>${item.itemCode}</td>
                                                    <td class="text-end">
                                                        <fmt:formatNumber value="${item.unitPrice}" type="currency" currencySymbol="Rs. "/>
                                                    </td>
                                                    <td class="text-center">${item.quantity}</td>
                                                    <td class="text-end">
                                                        <fmt:formatNumber value="${item.unitPrice * item.quantity}" type="currency" currencySymbol="Rs. "/>
                                                    </td>
                                                    <td class="text-end">
                                                        <c:if test="${item.discountAmount > 0}">
                                                            <span style="color: var(--accent-red);">
                                                                -<fmt:formatNumber value="${item.discountAmount}" type="currency" currencySymbol="Rs. "/>
                                                                <small>(${item.discountPercentage}%)</small>
                                                            </span>
                                                        </c:if>
                                                        <c:if test="${item.discountAmount == 0}">
                                                            -
                                                        </c:if>
                                                    </td>
                                                    <td class="text-end">
                                                        <strong>
                                                            <fmt:formatNumber value="${item.totalPrice}" type="currency" currencySymbol="Rs. "/>
                                                        </strong>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                                
                                <!-- Totals Section -->
                                <div class="total-section">
                                    <div class="row">
                                        <div class="col-md-6 offset-md-6">
                                            <div class="total-row">
                                                <span>Subtotal:</span>
                                                <span><fmt:formatNumber value="${bill.subtotal}" type="currency" currencySymbol="Rs. "/></span>
                                            </div>
                                            <c:if test="${bill.discountAmount > 0}">
                                                <div class="total-row text-danger">
                                                    <span>Discount (${bill.discountPercentage}%):</span>
                                                    <span>-<fmt:formatNumber value="${bill.discountAmount}" type="currency" currencySymbol="Rs. "/></span>
                                                </div>
                                            </c:if>
                                            <c:if test="${bill.taxAmount > 0}">
                                                <div class="total-row">
                                                    <span>Tax (${bill.taxPercentage}%):</span>
                                                    <span><fmt:formatNumber value="${bill.taxAmount}" type="currency" currencySymbol="Rs. "/></span>
                                                </div>
                                            </c:if>
                                            <div class="total-row grand-total">
                                                <span>Total Amount:</span>
                                                <span><fmt:formatNumber value="${bill.totalAmount}" type="currency" currencySymbol="Rs. "/></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                     </div>
                        
                       
                    
                    <!-- Sidebar Information -->
                    <div class="col-lg-4">
                        <!-- Customer Summary -->
                        <div class="card customer-info-card mb-4">
                            <div class="card-header">
                                <h5 class="mb-0">
                                    <i class="fas fa-chart-line me-2"></i>Customer Summary
                                </h5>
                            </div>
                            <div class="card-body">
                                <div class="bill-info-row">
                                    <span class="bill-info-label">Total Purchases:</span>
                                    <span class="bill-info-value">${customerPurchaseSummary.totalBills}</span>
                                </div>
                                <div class="bill-info-row">
                                    <span class="bill-info-label">Total Spent:</span>
                                    <span class="bill-info-value">
                                        <fmt:formatNumber value="${customerPurchaseSummary.totalSpent}" 
                                                        type="currency" currencySymbol="Rs. "/>
                                    </span>
                                </div>
                                <div class="bill-info-row">
                                    <span class="bill-info-label">Average Bill:</span>
                                    <span class="bill-info-value">
                                        <fmt:formatNumber value="${customerPurchaseSummary.averageBill}" 
                                                        type="currency" currencySymbol="Rs. "/>
                                    </span>
                                </div>
                                <div class="bill-info-row">
                                    <span class="bill-info-label">Member Since:</span>
                                    <span class="bill-info-value">
                                        <fmt:formatDate value="${customerPurchaseSummary.firstPurchaseDate}" 
                                                      pattern="dd/MM/yyyy"/>
                                    </span>
                                </div>
                            </div>
                        </div>
                        
                        
                        
                        <!-- Combined Actions Section -->
						<div class="card mb-4 no-print">
						    <div class="card-header">
						        <h5 class="mb-0">
						            <i class="fas fa-tasks me-2"></i>Bill Actions
						        </h5>
						    </div>
						    <div class="card-body">
						        <!-- Main Actions -->
						        <div class="mb-4">
						            <h6 class="text-brown-medium mb-3">
						                <i class="fas fa-cogs me-2"></i>Main Actions
						            </h6>
						            <div class="d-grid gap-2">
						                
						                <c:if test="${canProcessPayment}">
						                    <button type="button" class="btn btn-success text-start" 
						                            onclick="showPaymentModal()">
						                        <i class="fas fa-dollar-sign me-2"></i> Process Payment
						                    </button>
						                </c:if>
						                
						                <c:if test="${canCancel}">
						                    <button type="button" class="btn btn-danger text-start" 
						                            onclick="showCancelModal()">
						                        <i class="fas fa-times me-2"></i> Cancel Bill
						                    </button>
						                </c:if>
						            </div>
						        </div>
						        
						        <!-- Quick Actions -->
						        <div>
						            <h6 class="text-brown-medium mb-3">
						                <i class="fas fa-bolt me-2"></i>Quick Actions
						            </h6>
						            <div class="d-grid gap-2">
						                <button type="button" class="btn btn-outline-danger text-start" onclick="downloadPDF()">
						                    <i class="fas fa-file-pdf me-2"></i> Download PDF
						                </button>
						                <button type="button" class="btn btn-outline-info text-start" onclick="viewCustomer()">
						                    <i class="fas fa-user me-2"></i> View Customer
						                </button>
						            </div>
						        </div>
						    </div>
						</div>
                    </div>
                </div>
            </main>
    </div>
    
    <!-- Modals remain the same structure with updated styling -->
    <!-- Process Payment Modal -->
    <div class="modal fade" id="paymentModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <form id="paymentForm" onsubmit="processPayment(event)">
                    <div class="modal-header">
                        <h5 class="modal-title">
                            <i class="fas fa-credit-card me-2"></i>Process Payment
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label">Total Amount Due</label>
                            <div class="h4 text-success">
                                <fmt:formatNumber value="${bill.totalAmount}" type="currency" currencySymbol="Rs. "/>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="paymentMethodModal" class="form-label">Payment Method</label>
                            <select class="form-select" id="paymentMethodModal" required>
                                <option value="CASH">Cash</option>
                                <option value="CARD">Credit/Debit Card</option>
                                <option value="CHEQUE">Cheque</option>
                                <option value="BANK_TRANSFER">Bank Transfer</option>
                            </select>
                        </div>
                        <div class="mb-3" id="referenceFieldModal" style="display: none;">
                            <label for="paymentReferenceModal" class="form-label">Reference Number</label>
                            <input type="text" class="form-control" id="paymentReferenceModal" 
                                   placeholder="Enter cheque/transaction number">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-success">
                            <i class="fas fa-check"></i> Confirm Payment
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Cancel Bill Modal -->
    <div class="modal fade" id="cancelModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <form action="${pageContext.request.contextPath}/bill/view" method="post" 
                      onsubmit="return confirm('Are you sure you want to cancel this bill?')">
                    <input type="hidden" name="billAction" value="cancel">
                    <input type="hidden" name="billId" value="${bill.billId}">
                    <div class="modal-header">
                        <h5 class="modal-title">
                            <i class="fas fa-times-circle me-2"></i>Cancel Bill
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="alert alert-warning">
                            <i class="fas fa-exclamation-triangle me-2"></i> 
                            This action cannot be undone. Stock levels will be adjusted accordingly.
                        </div>
                        <div class="mb-3">
                            <label for="cancellationReason" class="form-label">Cancellation Reason *</label>
                            <textarea class="form-control" id="cancellationReason" name="cancellationReason" 
                                      rows="3" required placeholder="Please provide a reason for cancellation"></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-times"></i> Cancel Bill
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <jsp:include page="/includes/footer.jsp" />
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        $(document).ready(function() {
            // Payment method change in modal
            $('#paymentMethodModal').on('change', function() {
                var method = $(this).val();
                if (method === 'CHEQUE' || method === 'BANK_TRANSFER') {
                    $('#referenceFieldModal').show();
                    $('#paymentReferenceModal').attr('required', true);
                } else {
                    $('#referenceFieldModal').hide();
                    $('#paymentReferenceModal').removeAttr('required');
                }
            });
        });
        
        function showPaymentModal() {
            $('#paymentModal').modal('show');
        }
        
        function showCancelModal() {
            $('#cancelModal').modal('show');
        }
        
        
        function processPayment(event) {
            event.preventDefault();
            
            var paymentMethod = $('#paymentMethodModal').val();
            var paymentReference = $('#paymentReferenceModal').val();
            
            $.ajax({
                url: '${pageContext.request.contextPath}/bill/view',
                type: 'POST',
                data: {
                    action: 'ajax',
                    ajaxAction: 'processPayment',
                    billId: ${bill.billId},
                    paymentMethod: paymentMethod,
                    paymentReference: paymentReference
                },
                success: function(response) {
                    if (response.success) {
                        alert('Payment processed successfully!');
                        location.reload();
                    } else {
                        alert('Error: ' + response.error);
                    }
                }
            });
        }
        
        
        function viewCustomer() {
            window.location.href = '${pageContext.request.contextPath}/customer/view?id=${bill.customerId}';
        }
        
        function downloadPDF() {
            // Direct PDF download (not HTML)
            window.location.href = '${pageContext.request.contextPath}/bill/download/${bill.billId}/pdf';
        }
        
        function openPrintView() {
            // Open the dedicated print view
            var printWindow = window.open('${pageContext.request.contextPath}/bill/print/${bill.billId}', 
                                          'PrintBill', 
                                          'width=900,height=700');
        }
        
        // Print formatting
        window.addEventListener('beforeprint', function() {
            document.body.classList.add('printing');
        });
        
        window.addEventListener('afterprint', function() {
            document.body.classList.remove('printing');
        });
    </script>
</body>
</html>