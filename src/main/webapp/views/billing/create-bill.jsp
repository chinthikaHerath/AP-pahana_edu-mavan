
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create New Bill - Pahana Edu</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/select2-bootstrap-5-theme@1.3.0/dist/select2-bootstrap-5-theme.min.css">
    
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
        
        /* Button Styles */
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
        
        .btn-brown-secondary {
            background-color: var(--brown-light);
            color: white;
            border: none;
        }
        
        .btn-brown-secondary:hover {
            background-color: var(--brown-medium);
            color: white;
        }
        
        /* Card Styles */
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
        }
        
        /* Form Controls */
        .form-control, .form-select {
            border: 1px solid var(--brown-pale);
            background-color: white;
        }
        
        .form-control:focus, .form-select:focus {
            border-color: var(--brown-light);
            box-shadow: 0 0 0 0.2rem rgba(121, 85, 72, 0.25);
        }
        
        .form-control[readonly] {
            background-color: var(--cream);
            border-color: var(--brown-pale);
            color: var(--brown-medium);
        }
        
        .form-label {
            color: var(--brown-dark);
            font-weight: 500;
            margin-bottom: 0.5rem;
        }
        
        /* Item Row Styles */
        .item-row {
            background: white;
            border: 1px solid var(--brown-pale);
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 10px;
            transition: all 0.3s ease;
        }
        
        .item-row:hover {
            box-shadow: 0 3px 10px rgba(0,0,0,0.1);
            border-color: var(--brown-light);
        }
        
        .item-total {
            font-weight: 600;
            color: var(--brown-primary);
            font-size: 1rem;
            padding-top: 8px;
        }
        
        .remove-item-btn {
            cursor: pointer;
            color: var(--accent-red);
            font-size: 1.2rem;
            transition: color 0.3s ease;
            display: inline-block;
            padding: 8px;
        }
        
        .remove-item-btn:hover {
            color: #B71C1C;
            transform: scale(1.1);
        }
        
        .stock-info {
            font-size: 0.85rem;
            color: var(--brown-medium);
        }
        
        .stock-info .text-danger {
            color: var(--accent-red) !important;
            font-weight: 600;
        }
        
        /* Summary Card */
        .summary-card {
            position: sticky;
            top: 20px;
            border: none;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
        }
        
        .summary-card .card-header {
            background: linear-gradient(135deg, var(--brown-dark) 0%, var(--brown-primary) 100%);
            padding: 1.25rem;
        }
        
        .summary-card .card-body {
            background: linear-gradient(to bottom, white 0%, var(--cream) 100%);
        }
        
        .summary-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 0;
            border-bottom: 1px solid var(--brown-pale);
            color: var(--brown-dark);
        }
        
        .summary-row:last-of-type {
            border-bottom: 2px solid var(--brown-primary);
            padding-top: 15px;
            margin-top: 10px;
            font-size: 1.25rem;
            font-weight: 600;
            color: var(--brown-primary);
        }
        
        .discount-input {
            max-width: 80px;
            text-align: right;
            border-color: var(--brown-pale);
        }
        
        /* No Items Message */
        #noItemsMessage {
            padding: 3rem;
            background: var(--cream);
            border-radius: 10px;
            color: var(--brown-light);
        }
        
        #noItemsMessage i {
            color: var(--brown-lighter);
        }
        
        /* Select2 Custom Styles */
        .select2-container--bootstrap-5 .select2-selection {
            border-color: var(--brown-pale);
        }
        
        .select2-container--bootstrap-5 .select2-selection--single:focus,
        .select2-container--bootstrap-5.select2-container--focus .select2-selection {
            border-color: var(--brown-light);
            box-shadow: 0 0 0 0.2rem rgba(121, 85, 72, 0.25);
        }
        
        .select2-container--bootstrap-5 .select2-dropdown {
            border-color: var(--brown-pale);
        }
        
        .select2-container--bootstrap-5 .select2-results__option--highlighted {
            background-color: var(--brown-primary);
        }
        
        /* Small Text */
        small.text-muted {
            color: var(--brown-light) !important;
        }
        
        /* Form Check */
        .form-check-input:checked {
            background-color: var(--brown-primary);
            border-color: var(--brown-primary);
        }
        
        .form-check-input:focus {
            border-color: var(--brown-light);
            box-shadow: 0 0 0 0.25rem rgba(121, 85, 72, 0.25);
        }
        
        /* Action Buttons in Summary */
        .summary-card .btn-primary {
            background-color: var(--brown-primary);
            border: none;
            font-weight: 500;
            padding: 0.75rem;
        }
        
        .summary-card .btn-primary:hover {
            background-color: var(--brown-dark);
        }
        
        .summary-card .btn-outline-primary {
            color: var(--brown-primary);
            border-color: var(--brown-primary);
            font-weight: 500;
            padding: 0.75rem;
        }
        
        .summary-card .btn-outline-primary:hover {
            background-color: var(--brown-primary);
            border-color: var(--brown-primary);
            color: white;
        }
        
        /* Invalid Feedback */
        .invalid-feedback {
            color: var(--accent-red);
        }
        
        .is-invalid {
            border-color: var(--accent-red) !important;
        }
    </style>
</head>
<body>
    <jsp:include page="/includes/navbar.jsp" />
    
    <div class="container-fluid">
            <main class="px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom border-brown">
                    <h1 class="h2 text-brown-dark fw-bold">Create New Bill</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <div class="btn-group me-2">
                            <button type="button" class="btn btn-sm btn-brown-outline" onclick="saveDraft()">
                                <i class="fas fa-save"></i> Save Draft
                            </button>
                            <a href="${pageContext.request.contextPath}/bill/list" class="btn btn-sm btn-brown-outline">
                                <i class="fas fa-times"></i> Cancel
                            </a>
                        </div>
                    </div>
                </div>
                
                <jsp:include page="/includes/messages.jsp" />
                
                <form id="billForm" action="${pageContext.request.contextPath}/bill/create" method="post" class="needs-validation" novalidate>
                    <div class="row">
                        <div class="col-lg-8">
                            <!-- Customer Selection -->
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="mb-0">
                                        <i class="fas fa-user me-2"></i>Customer Information
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <label for="customerId" class="form-label">Select Customer *</label>
                                            <select class="form-select select2" id="customerId" name="customerId" required>
                                                <option value="">-- Select Customer --</option>
                                                <c:forEach items="${customers}" var="customer">
                                                    <option value="${customer.customerId}" 
                                                        ${selectedCustomer != null && selectedCustomer.customerId == customer.customerId ? 'selected' : ''}
                                                        data-phone="${customer.telephone}"
                                                        data-address="${customer.address}">
                                                        ${customer.customerName} - ${customer.accountNumber}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                            <div class="invalid-feedback">Please select a customer</div>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">Bill Number</label>
                                            <input type="text" class="form-control" value="${nextBillNumber}" readonly>
                                            <small class="text-muted">Auto-generated</small>
                                        </div>
                                    </div>
                                    <div class="row mt-3" id="customerDetails" style="display: none;">
                                        <div class="col-md-6">
                                            <label class="form-label">Phone</label>
                                            <input type="text" class="form-control" id="customerPhone" readonly>
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">Address</label>
                                            <input type="text" class="form-control" id="customerAddress" readonly>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Items Selection -->
                            <div class="card">
                                <div class="card-header d-flex justify-content-between align-items-center">
                                    <h5 class="mb-0">
                                        <i class="fas fa-shopping-cart me-2"></i>Bill Items
                                    </h5>
                                    <button type="button" class="btn btn-sm btn-light text-white" 
                                            style="background-color: var(--accent-green); border: none;" 
                                            onclick="addItemRow()">
                                        <i class="fas fa-plus"></i> Add Item
                                    </button>
                                </div>
                                <div class="card-body">
                                    <div id="itemsContainer">
                                    </div>
                                    <div id="noItemsMessage" class="text-center text-muted py-4">
                                        <i class="fas fa-shopping-cart fa-3x mb-3"></i>
                                        <p>No items added yet. Click "Add Item" to start.</p>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Payment Information -->
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="mb-0">
                                        <i class="fas fa-credit-card me-2"></i>Payment Information
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <div class="row">
                                        <div class="col-md-4">
                                            <label for="paymentMethod" class="form-label">Payment Method *</label>
                                            <select class="form-select" id="paymentMethod" name="paymentMethod" required>
                                                <c:forEach items="${paymentMethods}" var="method">
                                                    <option value="${method}" ${method == 'CASH' ? 'selected' : ''}>
                                                        <i class="${method.iconClass}"></i> ${method.displayName}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="col-md-4">
                                            <label for="paymentStatus" class="form-label">Payment Status *</label>
                                            <select class="form-select" id="paymentStatus" name="paymentStatus" required>
                                                <option value="PENDING">Pending</option>
                                                <option value="PAID" selected>Paid</option>
                                                <option value="PARTIAL">Partial Payment</option>
                                            </select>
                                        </div>
                                        <div class="col-md-4" id="referenceField" style="display: none;">
                                            <label for="paymentReference" class="form-label">Reference Number</label>
                                            <input type="text" class="form-control" id="paymentReference" name="paymentReference"
                                                   placeholder="Cheque/Transaction No.">
                                        </div>
                                    </div>
                                    <div class="row mt-3">
                                        <div class="col-md-12">
                                            <label for="notes" class="form-label">Notes (Optional)</label>
                                            <textarea class="form-control" id="notes" name="notes" rows="2"></textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Summary Sidebar -->
                        <div class="col-lg-4">
                            <div class="card summary-card">
                                <div class="card-header">
                                    <h5 class="mb-0">
                                        <i class="fas fa-calculator me-2"></i>Bill Summary
                                    </h5>
                                </div>
                                <div class="card-body">
                                    <div class="summary-row">
                                        <span>Subtotal:</span>
                                        <span id="subtotal">Rs. 0.00</span>
                                    </div>
                                    <div class="summary-row">
                                        <span>Discount (%):</span>
                                        <input type="number" class="form-control form-control-sm discount-input" 
                                               id="discountPercentage" name="discountPercentage" 
                                               min="0" max="100" step="0.01" value="0" onchange="calculateTotals()">
                                    </div>
                                    <div class="summary-row">
                                        <span>Discount Amount:</span>
                                        <span id="discountAmount">Rs. 0.00</span>
                                    </div>
                                    <div class="summary-row">
                                        <span>Tax (${defaultTaxRate}%):</span>
                                        <input type="hidden" id="taxPercentage" name="taxPercentage" value="${defaultTaxRate}">
                                        <span id="taxAmount">Rs. 0.00</span>
                                    </div>
                                    <div class="summary-row">
                                        <span>Total Amount:</span>
                                        <span id="totalAmount">Rs. 0.00</span>
                                    </div>
                                    
                                    <div class="mt-4">
                                        <div class="form-check mb-3">
                                            <input class="form-check-input" type="checkbox" id="printAfterSave">
                                            <label class="form-check-label" for="printAfterSave">
                                                Print bill after saving
                                            </label>
                                        </div>
                                        <div class="d-grid gap-2">
                                            <button type="submit" name="submitAction" value="save" class="btn btn-primary">
                                                <i class="fas fa-save"></i> Save Bill
                                            </button>
                                            <button type="submit" name="submitAction" value="saveAndNew" class="btn btn-outline-primary">
                                                <i class="fas fa-plus"></i> Save & Create New
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </main>
    </div>
    
    <!-- Item Row Template -->
    <template id="itemRowTemplate">
        <div class="item-row">
            <div class="row align-items-end g-2">
                <div class="col-md-4">
                    <label class="form-label">Item *</label>
                    <select class="form-select item-select" name="itemId[]" required onchange="updateItemDetails(this)">
                        <option value="">-- Select Item --</option>
                        <c:forEach items="${items}" var="item">
                            <option value="${item.itemId}" 
                                data-price="${item.sellingPrice}"
                                data-stock="${item.quantityInStock}"
                                data-code="${item.itemCode}">
                                ${item.itemName} - ${item.itemCode}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-2">
                    <label class="form-label">Price</label>
                    <input type="number" class="form-control" name="unitPrice[]" readonly>
                </div>
                <div class="col-md-2">
                    <label class="form-label">Quantity *</label>
                    <input type="number" class="form-control" name="quantity[]" min="1" value="1" required onchange="calculateItemTotal(this)">
                    <small class="text-muted stock-info"></small>
                </div>
                <div class="col-md-2">
                    <label class="form-label">Discount %</label>
                    <input type="number" class="form-control" name="itemDiscount[]" min="0" max="100" step="0.01" value="0" onchange="calculateItemTotal(this)">
                </div>
                <div class="col-md-1">
                    <label class="form-label">Total</label>
                    <div class="item-total">Rs. 0.00</div>
                </div>
                <div class="col-md-1 text-center">
                    <label class="form-label d-block">&nbsp;</label>
                    <span class="remove-item-btn" onclick="removeItemRow(this)" title="Remove item">
                        <i class="fas fa-trash"></i>
                    </span>
                </div>
            </div>
        </div>
    </template>
    
    <jsp:include page="/includes/footer.jsp" />
    
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script>
        $(document).ready(function() {
            // Initialize Select2
            $('.select2').select2({
                theme: 'bootstrap-5'
            });
            
            // Customer selection change
            $('#customerId').on('change', function() {
                var selectedOption = $(this).find('option:selected');
                if (selectedOption.val()) {
                    $('#customerPhone').val(selectedOption.data('phone'));
                    $('#customerAddress').val(selectedOption.data('address'));
                    $('#customerDetails').show();
                    
                    // Check if customer can be billed
                    checkCustomerStatus(selectedOption.val());
                } else {
                    $('#customerDetails').hide();
                }
            });
            
            // Payment method change
            $('#paymentMethod').on('change', function() {
                var method = $(this).val();
                if (method === 'CHEQUE' || method === 'BANK_TRANSFER') {
                    $('#referenceField').show();
                } else {
                    $('#referenceField').hide();
                    $('#paymentReference').val('');
                }
            });
            
            // Form validation
            $('#billForm').on('submit', function(e) {
                if (!this.checkValidity()) {
                    e.preventDefault();
                    e.stopPropagation();
                }
                
                // Check if at least one item is added
                if ($('#itemsContainer .item-row').length === 0) {
                    e.preventDefault();
                    alert('Please add at least one item to the bill');
                    return false;
                }
                
                // If print is checked, change submit action
                if ($('#printAfterSave').is(':checked')) {
                    $('<input>').attr({
                        type: 'hidden',
                        name: 'submitAction',
                        value: 'saveAndPrint'
                    }).appendTo(this);
                }
                
                $(this).addClass('was-validated');
            });
            
         // Add first item row
            addItemRow();
        });
        
        function addItemRow() {
            var template = document.getElementById('itemRowTemplate');
            var clone = template.content.cloneNode(true);
            $('#itemsContainer').append(clone);
            $('#noItemsMessage').hide();
            
            // Initialize select2 for new row
            $('#itemsContainer .item-row:last .item-select').select2({
                theme: 'bootstrap-5',
                width: '100%'
            });
            
            calculateTotals();
        }
        
        function removeItemRow(btn) {
            if ($('#itemsContainer .item-row').length > 1) {
                $(btn).closest('.item-row').remove();
                calculateTotals();
            } else {
                alert('At least one item is required');
            }
            
            if ($('#itemsContainer .item-row').length === 0) {
                $('#noItemsMessage').show();
            }
        }
        
        function updateItemDetails(select) {
            var selectedOption = $(select).find('option:selected');
            var row = $(select).closest('.item-row');
            
            if (selectedOption.val()) {
                var price = selectedOption.data('price');
                var stock = selectedOption.data('stock');
                
                row.find('input[name="unitPrice[]"]').val(price);
                row.find('.stock-info').text('Stock: ' + stock);
                
                // Check stock availability
                var quantity = row.find('input[name="quantity[]"]').val();
                checkStockAvailability(select, quantity);
                
                calculateItemTotal(select);
            } else {
                row.find('input[name="unitPrice[]"]').val('');
                row.find('.stock-info').text('');
                row.find('.item-total').text('Rs. 0.00');
            }
        }
        
        function calculateItemTotal(input) {
            var row = $(input).closest('.item-row');
            var price = parseFloat(row.find('input[name="unitPrice[]"]').val()) || 0;
            var quantity = parseInt(row.find('input[name="quantity[]"]').val()) || 0;
            var discount = parseFloat(row.find('input[name="itemDiscount[]"]').val()) || 0;
            
            var subtotal = price * quantity;
            var discountAmount = subtotal * (discount / 100);
            var total = subtotal - discountAmount;
            
            row.find('.item-total').text('Rs. ' + total.toFixed(2));
            
            // Check stock if quantity changed
            if ($(input).attr('name') === 'quantity[]') {
                var itemSelect = row.find('select[name="itemId[]"]');
                checkStockAvailability(itemSelect[0], quantity);
            }
            
            calculateTotals();
        }
        
        function calculateTotals() {
            var subtotal = 0;
            
            // Calculate subtotal from all items
            $('#itemsContainer .item-row').each(function() {
                var price = parseFloat($(this).find('input[name="unitPrice[]"]').val()) || 0;
                var quantity = parseInt($(this).find('input[name="quantity[]"]').val()) || 0;
                var discount = parseFloat($(this).find('input[name="itemDiscount[]"]').val()) || 0;
                
                var itemSubtotal = price * quantity;
                var itemDiscount = itemSubtotal * (discount / 100);
                subtotal += (itemSubtotal - itemDiscount);
            });
            
            // Calculate bill-level discount
            var discountPercentage = parseFloat($('#discountPercentage').val()) || 0;
            var discountAmount = subtotal * (discountPercentage / 100);
            
            // Calculate tax on discounted amount
            var afterDiscount = subtotal - discountAmount;
            var taxPercentage = parseFloat($('#taxPercentage').val()) || 0;
            var taxAmount = afterDiscount * (taxPercentage / 100);
            
            // Calculate total
            var totalAmount = afterDiscount + taxAmount;
            
            // Update display
            $('#subtotal').text('Rs. ' + subtotal.toFixed(2));
            $('#discountAmount').text('Rs. ' + discountAmount.toFixed(2));
            $('#taxAmount').text('Rs. ' + taxAmount.toFixed(2));
            $('#totalAmount').text('Rs. ' + totalAmount.toFixed(2));
        }
        
        function checkCustomerStatus(customerId) {
            $.ajax({
                url: '${pageContext.request.contextPath}/bill/create',
                type: 'POST',
                data: {
                    action: 'ajax',
                    ajaxAction: 'getCustomer',
                    customerId: customerId
                },
                success: function(response) {
                    if (response.success && !response.canBeBilled) {
                        alert('Warning: ' + response.message);
                    }
                }
            });
        }
        
        function checkStockAvailability(itemSelect, quantity) {
            var selectedOption = $(itemSelect).find('option:selected');
            var stock = parseInt(selectedOption.data('stock')) || 0;
            var row = $(itemSelect).closest('.item-row');
            
            if (quantity > stock) {
                row.find('.stock-info').html('<span class="text-danger">Insufficient stock!</span>');
                row.find('input[name="quantity[]"]').addClass('is-invalid');
            } else {
                row.find('.stock-info').html('Stock: ' + stock);
                row.find('input[name="quantity[]"]').removeClass('is-invalid');
            }
        }
        
        function saveDraft() {
            var billData = {
                customerId: $('#customerId').val(),
                items: []
            };
            
            $('#itemsContainer .item-row').each(function() {
                var itemId = $(this).find('select[name="itemId[]"]').val();
                if (itemId) {
                    billData.items.push({
                        itemId: itemId,
                        quantity: $(this).find('input[name="quantity[]"]').val(),
                        discount: $(this).find('input[name="itemDiscount[]"]').val()
                    });
                }
            });
            
            $.ajax({
                url: '${pageContext.request.contextPath}/bill/create',
                type: 'POST',
                data: {
                    action: 'ajax',
                    ajaxAction: 'saveDraft',
                    billData: JSON.stringify(billData)
                },
                success: function(response) {
                    if (response.success) {
                        alert('Draft saved successfully!');
                    }
                }
            });
        }
    </script>
</body>
</html>