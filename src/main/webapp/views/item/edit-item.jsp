<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Item - Pahana Edu</title>
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
        
        .card-header h6 {
            color: white !important;
            font-weight: 600;
            margin: 0;
            font-size: 1.1rem;
        }
        
        /* Form Sections */
        .form-section {
            background: linear-gradient(135deg, white 0%, var(--cream) 100%);
            padding: 1.5rem;
            border-radius: 8px;
            margin-bottom: 1.5rem;
            border-left: 3px solid var(--brown-primary);
        }
        
        .form-section-title {
            color: var(--brown-primary);
            font-weight: 600;
            margin-bottom: 1rem;
            font-size: 1rem;
            display: flex;
            align-items: center;
        }
        
        .form-section-title i {
            margin-right: 0.5rem;
            color: var(--brown-medium);
        }
        
        /* Info Alert */
        .info-alert {
            background-color: #FFF3E0;
            border-left: 3px solid var(--accent-orange);
            padding: 0.75rem 1rem;
            border-radius: 4px;
            margin-bottom: 1.5rem;
        }
        
        .info-alert i {
            color: var(--accent-orange);
            margin-right: 0.5rem;
        }
        
        /* Form Controls */
        .form-label {
            color: var(--brown-dark);
            font-weight: 600;
            margin-bottom: 0.5rem;
            font-size: 0.95rem;
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
        
        /* Readonly fields */
        .form-control[readonly], .form-control[disabled] {
            background-color: #F5F5F5 !important;
            border: 1px solid #E0E0E0;
            color: var(--brown-medium);
            cursor: not-allowed;
            font-weight: 500;
        }
        
        .readonly-field {
            position: relative;
        }
        
        .readonly-field::after {
            content: '\f023';
            font-family: 'Font Awesome 5 Free';
            font-weight: 900;
            position: absolute;
            right: 12px;
            top: 50%;
            transform: translateY(-50%);
            color: var(--brown-lighter);
            pointer-events: none;
        }
        
        .form-control.is-invalid {
            border-color: var(--accent-red);
        }
        
        .form-control.is-invalid:focus {
            border-color: var(--accent-red);
            box-shadow: 0 0 0 0.2rem rgba(211, 47, 47, 0.15);
        }
        
        .invalid-feedback {
            color: var(--accent-red);
            font-size: 0.875rem;
        }
        
        textarea.form-control {
            resize: vertical;
        }
        
        /* Required Field Indicator */
        .required-indicator {
            color: var(--accent-red);
            font-weight: bold;
        }
        
        /* Status Badge */
        .status-display {
            background-color: var(--cream);
            padding: 0.75rem;
            border-radius: 6px;
            border: 1px solid var(--brown-pale);
        }
        
        .badge.bg-success {
            background-color: var(--accent-green) !important;
        }
        
        .badge.bg-danger {
            background-color: var(--accent-red) !important;
        }
        
        /* Input Groups */
        .input-group-text {
            background-color: var(--cream);
            border: 1px solid var(--brown-pale);
            color: var(--brown-medium);
            font-weight: 600;
        }
        
        /* Profit Display */
        #profitDisplay {
            border: 1px solid var(--brown-pale);
            padding: 0.375rem 0.75rem;
            border-radius: 0 0.25rem 0.25rem 0;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        
        #profitDisplay.bg-success {
            background-color: #E8F5E9 !important;
            border-color: var(--accent-green);
            color: var(--accent-green);
        }
        
        #profitDisplay.bg-danger {
            background-color: #FFEBEE !important;
            border-color: var(--accent-red);
            color: var(--accent-red);
        }
        
        #profitDisplay.bg-warning {
            background-color: #FFF3E0 !important;
            border-color: var(--accent-orange);
            color: var(--accent-orange);
        }
        
        #profitDisplay.bg-light {
            background-color: var(--cream) !important;
        }
        
        /* Stock Alert */
        .alert-warning {
            background-color: #FFF3E0;
            border: none;
            border-left: 4px solid var(--accent-orange);
            color: var(--brown-dark);
        }
        
        .alert-warning i {
            color: var(--accent-orange);
        }
        
        /* Buttons */
        .btn-primary {
            background-color: var(--brown-primary);
            border-color: var(--brown-primary);
            color: white;
            font-weight: 500;
            padding: 0.5rem 1.5rem;
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
            padding: 0.5rem 1.5rem;
            transition: all 0.3s ease;
        }
        
        .btn-secondary:hover {
            background-color: var(--brown-medium);
            border-color: var(--brown-medium);
        }
        
        /* Helper Text */
        .text-muted {
            color: var(--brown-medium) !important;
            font-size: 0.875rem;
        }
        
        /* Card Body */
        .card-body {
            padding: 2rem;
            background: white;
        }
        
        /* Divider */
        hr {
            border-color: var(--brown-pale);
            opacity: 0.3;
            margin: 2rem 0;
        }
        
        /* Last Updated Info */
        .update-info {
            background-color: var(--cream);
            padding: 0.5rem 1rem;
            border-radius: 6px;
            display: inline-block;
        }
        
        .update-info i {
            color: var(--brown-medium);
        }
        
        /* Item Code Badge */
        .item-code-badge {
            background-color: var(--brown-primary);
            color: white;
            padding: 0.5rem 1rem;
            border-radius: 6px;
            font-family: monospace;
            font-weight: 600;
            display: inline-block;
        }
        
        /* Focus Animation */
        @keyframes focusPulse {
            0% { transform: scale(1); }
            50% { transform: scale(1.02); }
            100% { transform: scale(1); }
        }
        
        .form-control:focus:not([readonly]) {
            animation: focusPulse 0.3s ease;
        }
        
        /* Icon styling for labels */
        .label-icon {
            color: var(--brown-medium);
            margin-right: 0.25rem;
        }
    </style>
</head>
<body>
    <jsp:include page="/includes/navbar.jsp" />
    
    <div class="container-fluid">
            <main class="px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom border-brown">
                    <h1 class="h2 text-brown-dark fw-bold">
                        <i class="fas fa-edit me-2" style="color: var(--brown-medium);"></i>
                        Edit Item
                    </h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <a href="${pageContext.request.contextPath}/item/list" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back to List
                        </a>
                    </div>
                </div>
                
                <jsp:include page="/includes/messages.jsp" />
                
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold">
                            <i class="fas fa-info-circle me-2"></i>
                            Item Information
                        </h6>
                    </div>
                    <div class="card-body">
                        <!-- Info Alert -->
                        <div class="info-alert">
                            <i class="fas fa-info-circle"></i>
                            <strong>Note:</strong> Item Code cannot be modified after creation. To change stock levels, use the Stock Adjustment feature.
                        </div>
                        
                        <form action="${pageContext.request.contextPath}/item/edit" method="post" id="editItemForm">
                            <input type="hidden" name="itemId" value="${item.itemId}">
                            <input type="hidden" name="itemCode" value="${item.itemCode}">
                            
                            <!-- System Information Section (Read-only) -->
                            <div class="form-section">
                                <div class="form-section-title">
                                    <i class="fas fa-lock"></i>
                                    System Information
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label class="form-label">
                                            <i class="fas fa-barcode label-icon"></i>
                                            Item Code
                                        </label>
                                        <div class="readonly-field">
                                            <div class="item-code-badge">
                                                <i class="fas fa-tag me-2"></i>${item.itemCode}
                                            </div>
                                        </div>
                                        <small class="text-muted">Item code cannot be changed</small>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label">
                                            <i class="fas fa-toggle-on label-icon"></i>
                                            Current Status
                                        </label>
                                        <div class="status-display">
                                            <span class="badge ${item.active ? 'bg-success' : 'bg-danger'} px-3 py-2">
                                                <i class="fas ${item.active ? 'fa-check-circle' : 'fa-times-circle'} me-1"></i>
                                                ${item.active ? 'Active' : 'Inactive'}
                                            </span>
                                        </div>
                                        <small class="text-muted">Status cannot be changed from this form</small>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Basic Information Section -->
                            <div class="form-section">
                                <div class="form-section-title">
                                    <i class="fas fa-tag"></i>
                                    Basic Information
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-12">
                                        <label for="itemName" class="form-label">
                                            <i class="fas fa-cube label-icon"></i>
                                            Item Name <span class="required-indicator">*</span>
                                        </label>
                                        <input type="text" class="form-control ${validationErrors.itemName != null ? 'is-invalid' : ''}" 
                                               id="itemName" name="itemName" value="${item.itemName}" 
                                               placeholder="Enter item name" required maxlength="100">
                                        <c:if test="${validationErrors.itemName != null}">
                                            <div class="invalid-feedback">${validationErrors.itemName}</div>
                                        </c:if>
                                    </div>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="description" class="form-label">
                                        <i class="fas fa-align-left label-icon"></i>
                                        Description
                                    </label>
                                    <textarea class="form-control ${validationErrors.description != null ? 'is-invalid' : ''}" 
                                              id="description" name="description" rows="3" maxlength="500"
                                              placeholder="Enter item description">${item.description}</textarea>
                                    <small class="text-muted">Optional: Provide a detailed description of the item</small>
                                    <c:if test="${validationErrors.description != null}">
                                        <div class="invalid-feedback">${validationErrors.description}</div>
                                    </c:if>
                                </div>
                            </div>
                            
                            <!-- Category & Book Information Section -->
                            <div class="form-section">
                                <div class="form-section-title">
                                    <i class="fas fa-book"></i>
                                    Category & Book Information
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-4">
                                        <label for="categoryId" class="form-label">
                                            <i class="fas fa-folder label-icon"></i>
                                            Category <span class="required-indicator">*</span>
                                        </label>
                                        <select class="form-select ${validationErrors.categoryId != null ? 'is-invalid' : ''}" 
                                                id="categoryId" name="categoryId" required>
                                            <option value="">Select Category</option>
                                            <c:forEach items="${categories}" var="category">
                                                <option value="${category.categoryId}" 
                                                    ${item.categoryId == category.categoryId ? 'selected' : ''}>
                                                    ${category.categoryName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <c:if test="${validationErrors.categoryId != null}">
                                            <div class="invalid-feedback">${validationErrors.categoryId}</div>
                                        </c:if>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="author" class="form-label">
                                            <i class="fas fa-user-edit label-icon"></i>
                                            Author
                                        </label>
                                        <input type="text" class="form-control ${validationErrors.author != null ? 'is-invalid' : ''}" 
                                               id="author" name="author" value="${item.author}" maxlength="100"
                                               placeholder="For books only">
                                        <small class="text-muted">For books only</small>
                                        <c:if test="${validationErrors.author != null}">
                                            <div class="invalid-feedback">${validationErrors.author}</div>
                                        </c:if>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="publisher" class="form-label">
                                            <i class="fas fa-building label-icon"></i>
                                            Publisher
                                        </label>
                                        <input type="text" class="form-control ${validationErrors.publisher != null ? 'is-invalid' : ''}" 
                                               id="publisher" name="publisher" value="${item.publisher}" maxlength="100"
                                               placeholder="For books only">
                                        <small class="text-muted">For books only</small>
                                        <c:if test="${validationErrors.publisher != null}">
                                            <div class="invalid-feedback">${validationErrors.publisher}</div>
                                        </c:if>
                                    </div>
                                </div>
                                
                                <div class="row mb-3">
                                    <div class="col-md-12">
                                        <label for="isbn" class="form-label">
                                            <i class="fas fa-fingerprint label-icon"></i>
                                            ISBN
                                        </label>
                                        <input type="text" class="form-control ${validationErrors.isbn != null ? 'is-invalid' : ''}" 
                                               id="isbn" name="isbn" value="${item.isbn}" maxlength="20"
                                               placeholder="10 or 13 digit ISBN">
                                        <small class="text-muted">10 or 13 digit ISBN (for books)</small>
                                        <c:if test="${validationErrors.isbn != null}">
                                            <div class="invalid-feedback">${validationErrors.isbn}</div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Pricing Section -->
                            <div class="form-section">
                                <div class="form-section-title">
                                    <i class="fas fa-dollar-sign"></i>
                                    Pricing Information
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-4">
                                        <label for="unitPrice" class="form-label">
                                            <i class="fas fa-tag label-icon"></i>
                                            Unit Price (LKR) <span class="required-indicator">*</span>
                                        </label>
                                        <div class="input-group">
                                            <span class="input-group-text">
                                                <i class="fas fa-rupee-sign"></i>
                                            </span>
                                            <input type="number" step="0.01" min="0" 
                                                   class="form-control ${validationErrors.unitPrice != null ? 'is-invalid' : ''}" 
                                                   id="unitPrice" name="unitPrice" value="${item.unitPrice}" required>
                                            <c:if test="${validationErrors.unitPrice != null}">
                                                <div class="invalid-feedback">${validationErrors.unitPrice}</div>
                                            </c:if>
                                        </div>
                                        <small class="text-muted">Cost price of the item</small>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="sellingPrice" class="form-label">
                                            <i class="fas fa-shopping-cart label-icon"></i>
                                            Selling Price (LKR) <span class="required-indicator">*</span>
                                        </label>
                                        <div class="input-group">
                                            <span class="input-group-text">
                                                <i class="fas fa-rupee-sign"></i>
                                            </span>
                                            <input type="number" step="0.01" min="0" 
                                                   class="form-control ${validationErrors.sellingPrice != null ? 'is-invalid' : ''}" 
                                                   id="sellingPrice" name="sellingPrice" value="${item.sellingPrice}" required>
                                            <c:if test="${validationErrors.sellingPrice != null}">
                                                <div class="invalid-feedback">${validationErrors.sellingPrice}</div>
                                            </c:if>
                                        </div>
                                        <small class="text-muted">Price for customers</small>
                                    </div>
                                    <div class="col-md-4">
                                        <label class="form-label">
                                            <i class="fas fa-chart-line label-icon"></i>
                                            Profit Margin
                                        </label>
                                        <div class="input-group">
                                            <span class="input-group-text">
                                                <i class="fas fa-rupee-sign"></i>
                                            </span>
                                            <div class="form-control bg-light" id="profitDisplay">
                                                <span id="profitValue"><fmt:formatNumber value="${item.profit}" pattern="#,##0.00"/></span>
                                            </div>
                                            <span class="input-group-text" id="profitPercentage">
                                                (<fmt:formatNumber value="${item.profitMargin}" pattern="#0.0"/>%)
                                            </span>
                                        </div>
                                        <small class="text-muted">Auto-calculated profit</small>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Stock Information Section -->
                            <div class="form-section">
                                <div class="form-section-title">
                                    <i class="fas fa-warehouse"></i>
                                    Stock Information
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="quantityInStock" class="form-label">
                                            <i class="fas fa-boxes label-icon"></i>
                                            Quantity in Stock <span class="required-indicator">*</span>
                                        </label>
                                        <div class="input-group">
                                            <span class="input-group-text">
                                                <i class="fas fa-layer-group"></i>
                                            </span>
                                            <input type="number" min="0" 
                                                   class="form-control ${validationErrors.quantityInStock != null ? 'is-invalid' : ''}" 
                                                   id="quantityInStock" name="quantityInStock" value="${item.quantityInStock}" required>
                                            <span class="input-group-text">units</span>
                                            <c:if test="${validationErrors.quantityInStock != null}">
                                                <div class="invalid-feedback">${validationErrors.quantityInStock}</div>
                                            </c:if>
                                        </div>
                                        <small class="text-muted">Current stock level</small>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="reorderLevel" class="form-label">
                                            <i class="fas fa-exclamation-triangle label-icon"></i>
                                            Reorder Level <span class="required-indicator">*</span>
                                        </label>
                                        <div class="input-group">
                                            <span class="input-group-text">
                                                <i class="fas fa-bell"></i>
                                            </span>
                                            <input type="number" min="0" 
                                                   class="form-control ${validationErrors.reorderLevel != null ? 'is-invalid' : ''}" 
                                                   id="reorderLevel" name="reorderLevel" value="${item.reorderLevel}" required>
                                            <span class="input-group-text">units</span>
                                            <c:if test="${validationErrors.reorderLevel != null}">
                                                <div class="invalid-feedback">${validationErrors.reorderLevel}</div>
                                            </c:if>
                                        </div>
                                        <small class="text-muted">Alert when stock falls below this level</small>
                                    </div>
                                </div>
                                
                                <!-- Stock Status Alert -->
                                <c:if test="${item.quantityInStock <= item.reorderLevel}">
                                    <div class="alert alert-warning d-flex align-items-center" role="alert">
                                        <i class="fas fa-exclamation-triangle me-2 fs-4"></i>
                                        <div>
                                            <c:choose>
                                                <c:when test="${item.quantityInStock == 0}">
                                                    <strong>Out of Stock!</strong> This item is currently out of stock.
                                                </c:when>
                                                <c:otherwise>
                                                    <strong>Low Stock Alert!</strong> Current stock (${item.quantityInStock}) is at or below reorder level.
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                            
                            <hr class="my-4">
                            
                            <div class="row align-items-center">
                                <div class="col-md-6">
                                    <div class="update-info">
                                        <i class="fas fa-clock"></i> 
                                        Last updated: <strong><fmt:formatDate value="${item.updatedAt}" pattern="yyyy-MM-dd HH:mm"/></strong>
                                    </div>
                                </div>
                                <div class="col-md-6 text-end">
                                    <button type="reset" class="btn btn-secondary me-2">
                                        <i class="fas fa-undo"></i> Reset Changes
                                    </button>
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fas fa-save"></i> Update Item
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </main>
    </div>
    
    <jsp:include page="/includes/footer.jsp" />
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Original script content remains the same -->
    <script>
    $(document).ready(function() {
        // Function to calculate and display profit
        function calculateProfit() {
            var unitPrice = parseFloat($('#unitPrice').val()) || 0;
            var sellingPrice = parseFloat($('#sellingPrice').val()) || 0;
            var profit = sellingPrice - unitPrice;
            var profitMargin = unitPrice > 0 ? ((profit / unitPrice) * 100) : 0;
            
            // Format profit value with commas
            $('#profitValue').text(profit.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ","));
            $('#profitPercentage').text('(' + profitMargin.toFixed(1) + '%)');
            
            // Apply color coding based on profit
            var profitDisplay = $('#profitDisplay');
            profitDisplay.removeClass('bg-light bg-success bg-warning bg-danger text-white text-dark');
            
            if (profit < 0) {
                profitDisplay.addClass('bg-danger text-white');
            } else if (profit === 0) {
                profitDisplay.addClass('bg-warning');
            } else if (profitMargin > 20) {
                profitDisplay.addClass('bg-success text-white');
            } else {
                profitDisplay.addClass('bg-light');
            }
        }
        
        // Function to check stock levels
        function checkStockLevel() {
            var quantity = parseInt($('#quantityInStock').val()) || 0;
            var reorderLevel = parseInt($('#reorderLevel').val()) || 0;
            
            // Remove existing alert if any
            $('.stock-alert-dynamic').remove();
            
            if (quantity <= reorderLevel && quantity >= 0) {
                var alertMessage = quantity === 0 ? 
                    '<strong>Out of Stock!</strong> This item will be out of stock after update.' :
                    '<strong>Low Stock Warning!</strong> Stock level will be at or below reorder level.';
                
                var alertHtml = '<div class="alert alert-warning d-flex align-items-center stock-alert-dynamic mb-3" role="alert">' +
                               '<i class="fas fa-exclamation-triangle me-2"></i>' +
                               '<div>' + alertMessage + '</div></div>';
                
                $(alertHtml).insertBefore('hr.my-4');
            }
        }
        
        // Calculate profit on page load
        calculateProfit();
        
        // Recalculate when prices change
        $('#unitPrice, #sellingPrice').on('input', calculateProfit);
        
        // Check stock levels when quantities change
        $('#quantityInStock, #reorderLevel').on('input', checkStockLevel);
        
        // Form validation before submission
        $('#editItemForm').on('submit', function(e) {
            var unitPrice = parseFloat($('#unitPrice').val()) || 0;
            var sellingPrice = parseFloat($('#sellingPrice').val()) || 0;
            var quantity = parseInt($('#quantityInStock').val()) || 0;
            var reorderLevel = parseInt($('#reorderLevel').val()) || 0;
            
            // Validate prices
            if (unitPrice < 0 || sellingPrice < 0) {
                alert('Prices cannot be negative!');
                e.preventDefault();
                return false;
            }
            
            // Warning for selling below cost
            if (sellingPrice < unitPrice) {
                if (!confirm('Warning: Selling price is less than unit price. This will result in a loss of Rs. ' + 
                           (unitPrice - sellingPrice).toFixed(2) + ' per unit.\n\nDo you want to continue?')) {
                    e.preventDefault();
                    return false;
                }
            }
            
            // Validate quantities
            if (quantity < 0 || reorderLevel < 0) {
                alert('Quantities cannot be negative!');
                e.preventDefault();
                return false;
            }
            
            // Warning for out of stock
            if (quantity === 0) {
                if (!confirm('Warning: This item is currently out of stock. Continue with update?')) {
                    e.preventDefault();
                    return false;
                }
            }
            
            return true;
        });
        
        // ISBN formatting (optional)
        $('#isbn').on('blur', function() {
            var isbn = $(this).val().replace(/[^0-9X]/gi, '');
            if (isbn.length === 10 || isbn.length === 13) {
                $(this).removeClass('is-invalid');
            } else if (isbn.length > 0) {
                $(this).addClass('is-invalid');
                if (!$(this).next('.invalid-feedback').length) {
                    $(this).after('<div class="invalid-feedback">ISBN must be 10 or 13 digits</div>');
                }
            }
        });
    });
    </script>
</body>
</html>