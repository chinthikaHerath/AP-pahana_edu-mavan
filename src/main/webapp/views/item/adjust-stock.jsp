<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Adjust Stock - Pahana Edu</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
            --accent-info: #00ACC1;
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
            border: none;
            padding: 1rem 1.25rem;
            font-weight: 600;
        }
        
        .card-header h6 {
            margin: 0;
            font-size: 1.1rem;
        }
        
        /* Item Information Card */
        .item-info-card .card-header {
            background: linear-gradient(135deg, var(--accent-info) 0%, #0097A7 100%);
            color: white;
        }
        
        .item-info-card .card-header h6 {
            color: white !important;
        }
        
        .item-info-card .table th {
            color: var(--brown-medium);
            font-weight: 600;
            padding: 0.5rem 0;
            border: none;
        }
        
        .item-info-card .table td {
            color: var(--brown-dark);
            padding: 0.5rem 0;
            border: none;
        }
        
        /* Stock Adjustment Card */
        .adjustment-card .card-header {
            background: linear-gradient(135deg, var(--brown-primary) 0%, var(--brown-medium) 100%);
            color: white;
        }
        
        .adjustment-card .card-header h6 {
            color: white !important;
        }
        
        /* Movement History Card */
        .history-card .card-header {
            background: linear-gradient(135deg, var(--cream) 0%, #FAF8F3 100%);
            color: var(--brown-dark);
            border-bottom: 2px solid var(--brown-primary);
        }
        
        .history-card .card-header h6 {
            color: var(--brown-dark) !important;
        }
        
        /* Stock Badge */
        .stock-badge {
            font-size: 1.1rem;
            padding: 8px 16px;
            border-radius: 20px;
            font-weight: 600;
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
        
        /* Form Controls */
        .form-label {
            color: var(--brown-dark);
            font-weight: 600;
            margin-bottom: 0.5rem;
        }
        
        .form-control, .form-select {
            border: 1px solid var(--brown-pale);
            background-color: white;
            color: var(--brown-dark);
            transition: all 0.3s ease;
        }
        
        .form-control:focus, .form-select:focus {
            border-color: var(--brown-primary);
            box-shadow: 0 0 0 0.2rem rgba(93, 64, 55, 0.15);
            background-color: var(--cream);
        }
        
        /* Movement Type Options */
        optgroup {
            font-weight: 600;
            color: var(--brown-dark);
        }
        
        /* Input Group */
        .input-group-text {
            background-color: var(--cream);
            border: 1px solid var(--brown-pale);
            font-weight: 600;
            min-width: 45px;
            justify-content: center;
        }
        
        .input-group-text.text-success {
            background-color: #E8F5E9;
            color: var(--accent-green);
            border-color: var(--accent-green);
        }
        
        .input-group-text.text-danger {
            background-color: #FFEBEE;
            color: var(--accent-red);
            border-color: var(--accent-red);
        }
        
        .input-group-text.text-warning {
            background-color: #FFF8E1;
            color: var(--accent-orange);
            border-color: var(--accent-orange);
        }
        
        /* Alert Styles */
        .alert-warning {
            background-color: #FFF3E0;
            border-left: 4px solid var(--accent-orange);
            border: none;
            border-left: 4px solid var(--accent-orange);
            color: var(--brown-dark);
        }
        
        .alert-info {
            background-color: #E1F5FE;
            border: none;
            border-left: 4px solid var(--accent-info);
            color: var(--brown-dark);
        }
        
        /* Preview Section */
        #previewSection {
            background: linear-gradient(135deg, #E3F2FD 0%, #BBDEFB 100%);
            border-left: 4px solid var(--accent-blue);
            padding: 1rem;
            border-radius: 6px;
        }
        
        #previewSection h6 {
            color: var(--brown-dark);
            font-weight: 600;
            margin-bottom: 0.75rem;
        }
        
        #previewSection p {
            color: var(--brown-medium);
        }
        
        #previewSection strong {
            color: var(--brown-dark);
        }
        
        /* Buttons */
        .btn-primary {
            background-color: var(--brown-primary);
            border-color: var(--brown-primary);
            color: white;
            font-weight: 500;
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
            transition: all 0.3s ease;
        }
        
        .btn-secondary:hover {
            background-color: var(--brown-medium);
            border-color: var(--brown-medium);
        }
        
        /* Movement History Table */
        .movement-history {
            max-height: 400px;
            overflow-y: auto;
        }
        
        .movement-history::-webkit-scrollbar {
            width: 6px;
        }
        
        .movement-history::-webkit-scrollbar-track {
            background: #f1f1f1;
            border-radius: 3px;
        }
        
        .movement-history::-webkit-scrollbar-thumb {
            background: var(--brown-light);
            border-radius: 3px;
        }
        
        .movement-history::-webkit-scrollbar-thumb:hover {
            background: var(--brown-medium);
        }
        
        .movement-history .table thead th {
            background-color: var(--cream);
            color: var(--brown-dark);
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.8rem;
            letter-spacing: 0.5px;
            position: sticky;
            top: 0;
            z-index: 10;
        }
        
        .movement-history .table tbody tr:hover {
            background-color: var(--cream);
        }
        
        /* Movement Type Badges */
        .badge.bg-info {
            background-color: var(--accent-info) !important;
        }
        
        .badge.bg-primary {
            background-color: var(--brown-primary) !important;
        }
        
        /* Quantity Display */
        .movement-in { 
            color: var(--accent-green);
            font-weight: 600;
        }
        
        .movement-out { 
            color: var(--accent-red);
            font-weight: 600;
        }
        
        .movement-adjustment { 
            color: var(--accent-orange);
            font-weight: 600;
        }
        
        /* Helper Text */
        .text-muted {
            color: var(--brown-medium) !important;
            font-size: 0.875rem;
        }
        
        /* Item Code Highlight */
        .item-code {
            background-color: var(--cream);
            padding: 0.25rem 0.5rem;
            border-radius: 4px;
            font-family: monospace;
            font-weight: 700;
            color: var(--brown-primary);
        }
        
        /* Loading State */
        .btn-loading {
            position: relative;
            color: transparent;
        }
        
        .btn-loading::after {
            content: "";
            position: absolute;
            width: 16px;
            height: 16px;
            top: 50%;
            left: 50%;
            margin-left: -8px;
            margin-top: -8px;
            border: 2px solid #ffffff;
            border-radius: 50%;
            border-top-color: transparent;
            animation: spinner 0.6s linear infinite;
        }
        
        @keyframes spinner {
            to { transform: rotate(360deg); }
        }
    </style>
</head>
<body>
    <jsp:include page="/includes/navbar.jsp" />
    
    <div class="container-fluid">
            
            <main class="px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom border-brown">
                    <h1 class="h2 text-brown-dark fw-bold">
                        <i class="fas fa-boxes me-2" style="color: var(--brown-medium);"></i>
                        Stock Adjustment
                    </h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <a href="${pageContext.request.contextPath}/item/stock" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back to Stock Status
                        </a>
                    </div>
                </div>
                
                <jsp:include page="/includes/messages.jsp" />
                
                <div class="row">
                    <!-- Item Information Card -->
                    <div class="col-md-4">
                        <div class="card shadow mb-4 item-info-card">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold">
                                    <i class="fas fa-info-circle me-2"></i>
                                    Item Information
                                </h6>
                            </div>
                            <div class="card-body">
                                <table class="table table-sm table-borderless">
                                    <tr>
                                        <th width="40%">
                                            <i class="fas fa-barcode me-1" style="color: var(--brown-light);"></i>
                                            Code:
                                        </th>
                                        <td><span class="item-code">${item.itemCode}</span></td>
                                    </tr>
                                    <tr>
                                        <th>
                                            <i class="fas fa-tag me-1" style="color: var(--brown-light);"></i>
                                            Name:
                                        </th>
                                        <td><strong>${item.itemName}</strong></td>
                                    </tr>
                                    <tr>
                                        <th>
                                            <i class="fas fa-folder me-1" style="color: var(--brown-light);"></i>
                                            Category:
                                        </th>
                                        <td>${item.categoryName}</td>
                                    </tr>
                                    <tr>
                                        <th>
                                            <i class="fas fa-dollar-sign me-1" style="color: var(--brown-light);"></i>
                                            Unit Price:
                                        </th>
                                        <td>LKR <fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/></td>
                                    </tr>
                                    <tr>
                                        <th>
                                            <i class="fas fa-warehouse me-1" style="color: var(--brown-light);"></i>
                                            Current Stock:
                                        </th>
                                        <td>
                                            <span class="badge stock-badge bg-${item.stockStatusClass}">
                                                ${item.quantityInStock} units
                                            </span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>
                                            <i class="fas fa-exclamation-triangle me-1" style="color: var(--brown-light);"></i>
                                            Reorder Level:
                                        </th>
                                        <td>${item.reorderLevel} units</td>
                                    </tr>
                                    <tr>
                                        <th>
                                            <i class="fas fa-chart-line me-1" style="color: var(--brown-light);"></i>
                                            Status:
                                        </th>
                                        <td>
                                            <span class="badge bg-${item.stockStatusClass}">
                                                ${item.stockStatus}
                                            </span>
                                        </td>
                                    </tr>
                                </table>
                                
                                <c:if test="${item.quantityInStock <= item.reorderLevel}">
                                    <div class="alert alert-warning mt-3" role="alert">
                                        <i class="fas fa-exclamation-triangle me-2"></i>
                                        <c:choose>
                                            <c:when test="${item.quantityInStock == 0}">
                                                <strong>Out of Stock!</strong>
                                                <p class="mb-0 mt-1 small">Immediate restock required</p>
                                            </c:when>
                                            <c:otherwise>
                                                <strong>Low Stock Alert!</strong>
                                                <p class="mb-0 mt-1 small">Stock below reorder level</p>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Stock Adjustment Form -->
                    <div class="col-md-8">
                        <div class="card shadow mb-4 adjustment-card">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold">
                                    <i class="fas fa-sliders-h me-2"></i>
                                    Adjust Stock Level
                                </h6>
                            </div>
                            <div class="card-body">
                                <form action="${pageContext.request.contextPath}/item/adjust-stock" method="post" id="adjustStockForm">
                                    <input type="hidden" name="itemId" value="${item.itemId}">
                                    
                                    <div class="row mb-3">
                                        <div class="col-md-6">
                                            <label for="movementType" class="form-label">
                                                <i class="fas fa-exchange-alt me-1" style="color: var(--brown-medium);"></i>
                                                Movement Type <span style="color: var(--accent-red);">*</span>
                                            </label>
                                            <select class="form-select" id="movementType" name="movementType" required>
                                                <option value="">Select Movement Type</option>
                                                <optgroup label="📈 Stock Increase">
                                                    <option value="IN" data-color="success">Stock In (Purchase/Delivery)</option>
                                                    <option value="RETURN" data-color="success">Customer Return</option>
                                                </optgroup>
                                                <optgroup label="📉 Stock Decrease">
                                                    <option value="OUT" data-color="danger">Stock Out (Manual Sale)</option>
                                                    <option value="DAMAGE" data-color="danger">Damaged/Expired</option>
                                                </optgroup>
                                                <optgroup label="⚖️ Adjustment">
                                                    <option value="ADJUSTMENT" data-color="warning">Stock Adjustment (Set to specific value)</option>
                                                </optgroup>
                                            </select>
                                            <small class="text-muted">Select the type of stock movement</small>
                                        </div>
                                        
                                        <div class="col-md-6">
                                            <label for="quantity" class="form-label">
                                                <i class="fas fa-sort-numeric-up-alt me-1" style="color: var(--brown-medium);"></i>
                                                <span id="quantityLabel">Quantity <span style="color: var(--accent-red);">*</span></span>
                                            </label>
                                            <div class="input-group">
                                                <span class="input-group-text" id="quantityPrefix">
                                                    <i class="fas fa-plus"></i>
                                                </span>
                                                <input type="number" class="form-control" id="quantity" name="quantity" 
                                                       min="1" required placeholder="0">
                                                <span class="input-group-text">units</span>
                                            </div>
                                            <small class="text-muted" id="quantityHelp">Enter the quantity to add/remove</small>
                                        </div>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <label for="reason" class="form-label">
                                            <i class="fas fa-comment-alt me-1" style="color: var(--brown-medium);"></i>
                                            Reason/Notes <span style="color: var(--accent-red);">*</span>
                                        </label>
                                        <textarea class="form-control" id="reason" name="reason" rows="3" 
                                                  required maxlength="500"
                                                  placeholder="Provide a reason for this stock adjustment..."></textarea>
                                        <small class="text-muted">This will be recorded in the stock movement history</small>
                                    </div>
                                    
                                    <!-- Preview Section -->
                                    <div class="alert alert-info d-none" id="previewSection">
                                        <h6><i class="fas fa-eye me-2"></i>Preview:</h6>
                                        <div class="row">
                                            <div class="col-md-4">
                                                <p class="mb-1">Current Stock: <strong>${item.quantityInStock} units</strong></p>
                                            </div>
                                            <div class="col-md-4">
                                                <p class="mb-1">Operation: <strong id="previewOperation">-</strong></p>
                                            </div>
                                            <div class="col-md-4">
                                                <p class="mb-0">New Stock Level: <strong id="previewNewStock">-</strong></p>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <hr class="my-4" style="border-color: var(--brown-pale);">
                                    
                                    <div class="text-end">
                                        <button type="button" class="btn btn-secondary me-2" onclick="window.history.back()">
                                            <i class="fas fa-times"></i> Cancel
                                        </button>
                                        <button type="submit" class="btn btn-primary" id="submitBtn">
                                            <i class="fas fa-save"></i> Confirm Adjustment
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                        
                        <!-- Recent Movement History -->
                        <div class="card shadow history-card">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold">
                                    <i class="fas fa-history me-2"></i>
                                    Recent Stock Movements
                                </h6>
                            </div>
                            <div class="card-body movement-history">
                                <c:choose>
                                    <c:when test="${not empty stockHistory}">
                                        <div class="table-responsive">
                                            <table class="table table-sm table-hover">
                                                <thead>
                                                    <tr>
                                                        <th><i class="fas fa-calendar-alt me-1"></i>Date</th>
                                                        <th><i class="fas fa-tag me-1"></i>Type</th>
                                                        <th><i class="fas fa-sort-numeric-up-alt me-1"></i>Quantity</th>
                                                        <th><i class="fas fa-comment me-1"></i>Reason</th>
                                                        <th><i class="fas fa-user me-1"></i>By</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach items="${stockHistory}" var="movement">
                                                        <tr>
                                                            <td>
                                                                <small>
                                                                    <fmt:formatDate value="${movement.movementDate}" 
                                                                                  pattern="yyyy-MM-dd HH:mm"/>
                                                                </small>
                                                            </td>
                                                            <td>
                                                                <span class="badge bg-${movement.movementClass}">
                                                                    ${movement.movementType.displayName}
                                                                </span>
                                                            </td>
                                                            <td class="${movement.movementClass}">
                                                                <strong>
                                                                    ${movement.movementSymbol}${movement.absoluteQuantity}
                                                                </strong>
                                                            </td>
                                                            <td><small>${movement.reason}</small></td>
                                                            <td><small>${movement.userName}</small></td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="text-center py-4">
                                            <i class="fas fa-history fa-3x" style="color: var(--brown-lighter);"></i>
                                            <p class="text-muted mt-2">No stock movement history available</p>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
    </div>
    
    <jsp:include page="/includes/footer.jsp" />
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
    $(document).ready(function() {
        var currentStock = ${item.quantityInStock};
        
        // Handle movement type change
        $('#movementType').on('change', function() {
            var selectedType = $(this).val();
            var selectedOption = $(this).find('option:selected');
            var color = selectedOption.data('color');
            
            // Update quantity label and prefix based on type
            switch(selectedType) {
                case 'IN':
                case 'RETURN':
                    $('#quantityLabel').html('<i class="fas fa-sort-numeric-up-alt me-1" style="color: var(--brown-medium);"></i>Quantity to Add <span style="color: var(--accent-red);">*</span>');
                    $('#quantityPrefix').html('<i class="fas fa-plus"></i>').removeClass().addClass('input-group-text text-success');
                    $('#quantityHelp').text('Enter the quantity to add to stock');
                    break;
                    
                case 'OUT':
                case 'DAMAGE':
                    $('#quantityLabel').html('<i class="fas fa-sort-numeric-down-alt me-1" style="color: var(--brown-medium);"></i>Quantity to Remove <span style="color: var(--accent-red);">*</span>');
                    $('#quantityPrefix').html('<i class="fas fa-minus"></i>').removeClass().addClass('input-group-text text-danger');
                    $('#quantityHelp').text('Enter the quantity to remove from stock');
                    break;
                    
                case 'ADJUSTMENT':
                    $('#quantityLabel').html('<i class="fas fa-equals me-1" style="color: var(--brown-medium);"></i>New Stock Level <span style="color: var(--accent-red);">*</span>');
                    $('#quantityPrefix').html('<i class="fas fa-equals"></i>').removeClass().addClass('input-group-text text-warning');
                    $('#quantityHelp').text('Enter the new total stock quantity');
                    break;
                    
                default:
                    $('#quantityLabel').html('<i class="fas fa-sort-numeric-up-alt me-1" style="color: var(--brown-medium);"></i>Quantity <span style="color: var(--accent-red);">*</span>');
                    $('#quantityPrefix').html('<i class="fas fa-question"></i>').removeClass().addClass('input-group-text');
                    $('#quantityHelp').text('Enter the quantity');
            }
            
            updatePreview();
        });
        
        // Handle quantity change
        $('#quantity').on('input', function() {
            updatePreview();
        });
        
        // Update preview section
        function updatePreview() {
            var movementType = $('#movementType').val();
            var quantity = parseInt($('#quantity').val()) || 0;
            
            if (movementType && quantity > 0) {
                var newStock = currentStock;
                var operation = '';
                
                switch(movementType) {
                    case 'IN':
                    case 'RETURN':
                        newStock = currentStock + quantity;
                        operation = '<span class="text-success">Adding ' + quantity + ' units</span>';
                        break;
                        
                    case 'OUT':
                    case 'DAMAGE':
                        newStock = currentStock - quantity;
                        operation = '<span class="text-danger">Removing ' + quantity + ' units</span>';
                        break;
                        
                    case 'ADJUSTMENT':
                        newStock = quantity;
                        var diff = quantity - currentStock;
                        operation = '<span class="text-warning">Adjusting to ' + quantity + ' units (' + 
                                  (diff >= 0 ? '+' : '') + diff + ')</span>';
                        break;
                }
                
                $('#previewOperation').html(operation);
                $('#previewNewStock').text(newStock + ' units');
                
                // Show warning if stock will be negative
                $('#previewNewStock').removeClass('text-danger text-warning text-success');
                if (newStock < 0) {
                    $('#previewNewStock').addClass('text-danger').append(' ⚠️ INVALID - Cannot be negative!');
                } else if (newStock === 0) {
                    $('#previewNewStock').addClass('text-warning').append(' ⚠️ Out of Stock');
                } else if (newStock <= ${item.reorderLevel}) {
                    $('#previewNewStock').addClass('text-warning').append(' ⚠️ Low Stock');
                } else {
                    $('#previewNewStock').addClass('text-success');
                }
                
                $('#previewSection').removeClass('d-none');
            } else {
                $('#previewSection').addClass('d-none');
            }
        }
        
        // Form validation
        $('#adjustStockForm').on('submit', function(e) {
            var movementType = $('#movementType').val();
            var quantity = parseInt($('#quantity').val()) || 0;
            var reason = $('#reason').val().trim();
            
            if (!movementType) {
                alert('Please select a movement type');
                e.preventDefault();
                return false;
            }
            
            if (quantity <= 0) {
                alert('Quantity must be greater than zero');
                e.preventDefault();
                return false;
            }
            
            if (!reason) {
                alert('Please provide a reason for this adjustment');
                e.preventDefault();
                return false;
            }
            
            // Check for negative stock
            var newStock = currentStock;
            if (movementType === 'OUT' || movementType === 'DAMAGE') {
                newStock = currentStock - quantity;
                if (newStock < 0) {
                    alert('Error: This adjustment would result in negative stock!\n' +
                          'Current stock: ' + currentStock + '\n' +
                          'Trying to remove: ' + quantity);
                    e.preventDefault();
                    return false;
                }
            }
            
            // Confirmation for critical operations
            if (movementType === 'ADJUSTMENT') {
                if (!confirm('Are you sure you want to adjust the stock level to ' + quantity + ' units?')) {
                    e.preventDefault();
                    return false;
                }
            } else if (movementType === 'DAMAGE') {
                if (!confirm('Are you sure you want to mark ' + quantity + ' units as damaged/expired?')) {
                    e.preventDefault();
                    return false;
                }
            }
            
            // Add loading state to button
            $('#submitBtn').addClass('btn-loading').prop('disabled', true);
            
            return true;
        });
    });
    </script>
</body>
</html>