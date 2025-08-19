<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add Item - Pahana Edu</title>
    <jsp:include page="/includes/header.jsp" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    
    <style>
        :root {
            --brown-dark: #F54927;
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
        
        .form-control[readonly] {
            background-color: #F5F5F5;
            border: 1px solid #E0E0E0;
            color: var(--brown-medium);
            cursor: not-allowed;
            font-weight: 500;
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
        
        /* Input Icons */
        .input-icon {
            position: relative;
        }
        
        .input-icon i {
            position: absolute;
            left: 12px;
            top: 50%;
            transform: translateY(-50%);
            color: var(--brown-light);
            pointer-events: none;
            z-index: 1;
        }
        
        .input-icon .form-control {
            padding-left: 35px;
        }
        
        /* Price Input Group */
        .input-group-text {
            background-color: var(--cream);
            border: 1px solid var(--brown-pale);
            color: var(--brown-medium);
            font-weight: 600;
        }
        
        /* Profit Display */
        #profitDisplay {
            background-color: var(--cream);
            border: 1px solid var(--brown-pale);
            padding: 0.375rem 0.75rem;
            border-radius: 0.25rem;
            font-weight: 600;
            transition: all 0.3s ease;
        }
        
        #profitDisplay.profit-positive {
            background-color: #E8F5E9;
            border-color: var(--accent-green);
            color: var(--accent-green);
        }
        
        #profitDisplay.profit-negative {
            background-color: #FFEBEE;
            border-color: var(--accent-red);
            color: var(--accent-red);
        }
        
        #profitDisplay.profit-zero {
            background-color: #FFF3E0;
            border-color: var(--accent-orange);
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
        .form-text {
            color: var(--brown-medium);
            font-size: 0.875rem;
            margin-top: 0.25rem;
        }
        
        /* ISBN Badge */
        .isbn-detected {
            background-color: var(--accent-green);
            color: white;
            padding: 0.25rem 0.5rem;
            border-radius: 4px;
            font-size: 0.75rem;
            display: inline-block;
            margin-left: 0.5rem;
            animation: fadeIn 0.3s ease;
        }
        
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(-5px); }
            to { opacity: 1; transform: translateY(0); }
        }
        
        /* Stock Alert */
        .stock-alert {
            background-color: #FFF3E0;
            border-left: 3px solid var(--accent-orange);
            padding: 0.75rem;
            border-radius: 4px;
            margin-top: 0.5rem;
            display: none;
        }
        
        .stock-alert.show {
            display: block;
            animation: slideDown 0.3s ease;
        }
        
        @keyframes slideDown {
            from { opacity: 0; max-height: 0; }
            to { opacity: 1; max-height: 100px; }
        }
        
        /* Focus Animation */
        @keyframes focusPulse {
            0% { transform: scale(1); }
            50% { transform: scale(1.02); }
            100% { transform: scale(1); }
        }
        
        .form-control:focus {
            animation: focusPulse 0.3s ease;
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
                        <i class="fas fa-box-open me-2" style="color: var(--brown-medium);"></i>
                        Add New Item
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
                        <form action="${pageContext.request.contextPath}/item/add" method="post" id="addItemForm">
                            
                            <!-- Basic Information Section -->
                            <div class="form-section">
                                <div class="form-section-title">
                                    <i class="fas fa-tag"></i>
                                    Basic Information
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="itemCode" class="form-label">
                                            Item Code <span class="required-indicator">*</span>
                                        </label>
                                        <div class="input-icon">
                                            <i class="fas fa-barcode"></i>
                                            <input type="text" class="form-control ${validationErrors.itemCode != null ? 'is-invalid' : ''}" 
                                                   id="itemCode" name="itemCode" value="${item.itemCode}" 
                                                   placeholder="Enter unique item code" required>
                                        </div>
                                        <c:if test="${validationErrors.itemCode != null}">
                                            <div class="invalid-feedback">${validationErrors.itemCode}</div>
                                        </c:if>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="itemName" class="form-label">
                                            Item Name <span class="required-indicator">*</span>
                                        </label>
                                        <div class="input-icon">
                                            <i class="fas fa-cube"></i>
                                            <input type="text" class="form-control ${validationErrors.itemName != null ? 'is-invalid' : ''}" 
                                                   id="itemName" name="itemName" value="${item.itemName}" 
                                                   placeholder="Enter item name" required>
                                        </div>
                                        <c:if test="${validationErrors.itemName != null}">
                                            <div class="invalid-feedback">${validationErrors.itemName}</div>
                                        </c:if>
                                    </div>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="description" class="form-label">
                                        <i class="fas fa-align-left me-1" style="color: var(--brown-medium);"></i>
                                        Description
                                    </label>
                                    <textarea class="form-control" id="description" name="description" rows="3" 
                                              placeholder="Enter item description (optional)">${item.description}</textarea>
                                    <small class="form-text">Provide a detailed description of the item</small>
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
                                            Category <span class="required-indicator">*</span>
                                        </label>
                                        <div class="input-icon">
                                            <i class="fas fa-folder"></i>
                                            <select class="form-select ${validationErrors.categoryId != null ? 'is-invalid' : ''}" 
                                                    id="categoryId" name="categoryId" required style="padding-left: 35px;">
                                                <option value="">Select Category</option>
                                                <c:forEach items="${categories}" var="category">
                                                    <option value="${category.categoryId}" 
                                                        ${item.categoryId == category.categoryId ? 'selected' : ''}>
                                                        ${category.categoryName}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <c:if test="${validationErrors.categoryId != null}">
                                            <div class="invalid-feedback">${validationErrors.categoryId}</div>
                                        </c:if>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="author" class="form-label">
                                            <i class="fas fa-user-edit me-1" style="color: var(--brown-medium);"></i>
                                            Author
                                        </label>
                                        <input type="text" class="form-control" id="author" name="author" 
                                               value="${item.author}" placeholder="For books only">
                                        <small class="form-text">Leave blank for non-book items</small>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="publisher" class="form-label">
                                            <i class="fas fa-building me-1" style="color: var(--brown-medium);"></i>
                                            Publisher
                                        </label>
                                        <input type="text" class="form-control" id="publisher" name="publisher" 
                                               value="${item.publisher}" placeholder="For books only">
                                        <small class="form-text">Leave blank for non-book items</small>
                                    </div>
                                </div>
                                
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="isbn" class="form-label">
                                            <i class="fas fa-fingerprint me-1" style="color: var(--brown-medium);"></i>
                                            ISBN
                                            <span id="isbnBadge" class="isbn-detected" style="display: none;">Book Detected</span>
                                        </label>
                                        <input type="text" class="form-control" id="isbn" name="isbn" 
                                               value="${item.isbn}" placeholder="10 or 13 digit ISBN">
                                        <small class="form-text">Enter ISBN for books</small>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label">
                                            <i class="fas fa-bookmark me-1" style="color: var(--brown-medium);"></i>
                                            Item Type
                                        </label>
                                        <input type="text" class="form-control" id="itemType" 
                                               value="${item.isBook() ? 'Book' : 'Other'}" readonly>
                                        <small class="form-text">Automatically detected based on ISBN</small>
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
                                            Unit Price (Cost) <span class="required-indicator">*</span>
                                        </label>
                                        <div class="input-group">
                                            <span class="input-group-text">LKR</span>
                                            <input type="number" step="0.01" min="0" 
                                                   class="form-control ${validationErrors.unitPrice != null ? 'is-invalid' : ''}" 
                                                   id="unitPrice" name="unitPrice" value="${item.unitPrice}" 
                                                   placeholder="0.00" required>
                                        </div>
                                        <c:if test="${validationErrors.unitPrice != null}">
                                            <div class="invalid-feedback">${validationErrors.unitPrice}</div>
                                        </c:if>
                                        <small class="form-text">Cost price of the item</small>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="sellingPrice" class="form-label">
                                            Selling Price <span class="required-indicator">*</span>
                                        </label>
                                        <div class="input-group">
                                            <span class="input-group-text">LKR</span>
                                            <input type="number" step="0.01" min="0" 
                                                   class="form-control ${validationErrors.sellingPrice != null ? 'is-invalid' : ''}" 
                                                   id="sellingPrice" name="sellingPrice" value="${item.sellingPrice}" 
                                                   placeholder="0.00" required>
                                        </div>
                                        <c:if test="${validationErrors.sellingPrice != null}">
                                            <div class="invalid-feedback">${validationErrors.sellingPrice}</div>
                                        </c:if>
                                        <small class="form-text">Price for customers</small>
                                    </div>
                                    <div class="col-md-4">
                                        <label class="form-label">
                                            <i class="fas fa-chart-line me-1" style="color: var(--brown-medium);"></i>
                                            Profit Margin
                                        </label>
                                        <div id="profitDisplay">
                                            <i class="fas fa-calculator me-1"></i>
                                            <span id="profitAmount">0.00</span> 
                                            (<span id="profitPercentage">0%</span>)
                                        </div>
                                        <small class="form-text">Automatically calculated</small>
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
                                            Initial Stock Quantity <span class="required-indicator">*</span>
                                        </label>
                                        <div class="input-group">
                                            <span class="input-group-text">
                                                <i class="fas fa-boxes"></i>
                                            </span>
                                            <input type="number" min="0" 
                                                   class="form-control ${validationErrors.quantityInStock != null ? 'is-invalid' : ''}" 
                                                   id="quantityInStock" name="quantityInStock" 
                                                   value="${item.quantityInStock}" placeholder="0" required>
                                            <span class="input-group-text">units</span>
                                        </div>
                                        <c:if test="${validationErrors.quantityInStock != null}">
                                            <div class="invalid-feedback">${validationErrors.quantityInStock}</div>
                                        </c:if>
                                        <small class="form-text">Starting inventory quantity</small>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="reorderLevel" class="form-label">
                                            Reorder Level <span class="required-indicator">*</span>
                                        </label>
                                        <div class="input-group">
                                            <span class="input-group-text">
                                                <i class="fas fa-exclamation-triangle"></i>
                                            </span>
                                            <input type="number" min="0" 
                                                   class="form-control ${validationErrors.reorderLevel != null ? 'is-invalid' : ''}" 
                                                   id="reorderLevel" name="reorderLevel" 
                                                   value="${item.reorderLevel}" placeholder="10" required>
                                            <span class="input-group-text">units</span>
                                        </div>
                                        <c:if test="${validationErrors.reorderLevel != null}">
                                            <div class="invalid-feedback">${validationErrors.reorderLevel}</div>
                                        </c:if>
                                        <small class="form-text">Alert when stock falls below this level</small>
                                    </div>
                                </div>
                                
                                <div class="stock-alert" id="stockAlert">
                                    <i class="fas fa-info-circle me-2"></i>
                                    <strong>Stock Alert:</strong> <span id="stockAlertMessage"></span>
                                </div>
                            </div>
                            
                            <hr>
                            
                            <div class="text-end">
                                <button type="reset" class="btn btn-secondary me-2">
                                    <i class="fas fa-undo"></i> Reset
                                </button>
                                <button type="submit" class="btn btn-primary" id="submitBtn">
                                    <i class="fas fa-save"></i> Save Item
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </main>
        
    </div>
    
    <jsp:include page="/includes/footer.jsp" />
    
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/validation.js"></script>
    
    <script>
        $(document).ready(function() {
            // Calculate profit on page load
            calculateProfit();
            
            // Calculate profit when prices change
            $('#unitPrice, #sellingPrice').on('input', calculateProfit);
            
            // Check stock levels
            $('#quantityInStock, #reorderLevel').on('input', checkStockLevels);
            
            // Auto-detect book type based on ISBN
            $('#isbn').on('input blur', function() {
                var isbn = $(this).val().trim();
                if(isbn !== '') {
                    $('#itemType').val('Book');
                    $('#isbnBadge').fadeIn();
                } else {
                    $('#itemType').val('Other');
                    $('#isbnBadge').fadeOut();
                }
            });
            
            // Form submission
            $('#addItemForm').on('submit', function(e) {
                // Add loading state
                $('#submitBtn').addClass('btn-loading').prop('disabled', true).html('<i class="fas fa-spinner fa-spin"></i> Saving...');
                
                // Validate prices
                var unitPrice = parseFloat($('#unitPrice').val()) || 0;
                var sellingPrice = parseFloat($('#sellingPrice').val()) || 0;
                
                if (sellingPrice < unitPrice) {
                    if (!confirm('Warning: Selling price is less than unit price. This will result in a loss. Continue?')) {
                        $('#submitBtn').removeClass('btn-loading').prop('disabled', false).html('<i class="fas fa-save"></i> Save Item');
                        e.preventDefault();
                        return false;
                    }
                }
            });
        });
        
        function calculateProfit() {
            var unitPrice = parseFloat($('#unitPrice').val()) || 0;
            var sellingPrice = parseFloat($('#sellingPrice').val()) || 0;
            var profit = sellingPrice - unitPrice;
            var profitMargin = unitPrice > 0 ? ((profit / unitPrice) * 100) : 0;
            
            $('#profitAmount').text('LKR ' + profit.toFixed(2));
            $('#profitPercentage').text(profitMargin.toFixed(1) + '%');
            
            // Apply color coding
            var profitDisplay = $('#profitDisplay');
            profitDisplay.removeClass('profit-positive profit-negative profit-zero');
            
            if (profit > 0) {
                profitDisplay.addClass('profit-positive');
            } else if (profit < 0) {
                profitDisplay.addClass('profit-negative');
            } else {
                profitDisplay.addClass('profit-zero');
            }
        }
        
        function checkStockLevels() {
            var quantity = parseInt($('#quantityInStock').val()) || 0;
            var reorderLevel = parseInt($('#reorderLevel').val()) || 0;
            var stockAlert = $('#stockAlert');
            var alertMessage = $('#stockAlertMessage');
            
            if (quantity === 0) {
                alertMessage.text('Item will be created with zero stock. Consider adding initial inventory.');
                stockAlert.addClass('show');
            } else if (quantity <= reorderLevel && quantity > 0) {
                alertMessage.text('Initial stock is at or below reorder level. Consider increasing initial stock.');
                stockAlert.addClass('show');
            } else {
                stockAlert.removeClass('show');
            }
        }
    </script>
</body>
</html>