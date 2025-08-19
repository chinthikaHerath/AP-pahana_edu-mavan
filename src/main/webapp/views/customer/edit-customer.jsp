<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Customer - Pahana Edu</title>
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
            font-size: 1.1rem;
        }
        
        /* Form Styling */
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
        .form-control[readonly] {
            background-color: #F5F5F5;
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
        
        /* Status Badge */
        .status-badge {
            display: inline-block;
            padding: 0.375rem 0.75rem;
            border-radius: 20px;
            font-weight: 600;
            font-size: 0.875rem;
        }
        
        .status-badge.active {
            background-color: var(--accent-green);
            color: white;
        }
        
        .status-badge.inactive {
            background-color: var(--brown-light);
            color: white;
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
        
        /* Required Field Indicator */
        .required-indicator {
            color: var(--accent-red);
            font-weight: bold;
        }
        
        /* Input Group Icons */
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
        
        /* Helper Text */
        .form-text {
            color: var(--brown-medium);
            font-size: 0.875rem;
            margin-top: 0.25rem;
        }
        
        /* Card Body */
        .card-body {
            padding: 2rem;
            background: white;
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
        
        /* Divider */
        hr {
            border-color: var(--brown-pale);
            opacity: 0.3;
            margin: 2rem 0;
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
    </style>
</head>
<body>
    <jsp:include page="/includes/navbar.jsp" />
    
    <div class="container-fluid">
            <main class="px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom border-brown">
                    <h1 class="h2 text-brown-dark fw-bold">
                        <i class="fas fa-user-edit me-2" style="color: var(--brown-medium);"></i>
                        Edit Customer
                    </h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <a href="${pageContext.request.contextPath}/customer/list" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back to List
                        </a>
                    </div>
                </div>
                
                <jsp:include page="/includes/messages.jsp" />
                
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <h6 class="m-0 font-weight-bold">
                            <i class="fas fa-info-circle me-2"></i>
                            Customer Information
                        </h6>
                    </div>
                    <div class="card-body">
                        <!-- Info Alert -->
                        <div class="info-alert">
                            <i class="fas fa-info-circle"></i>
                            <strong>Note:</strong> Account Number and NIC Number cannot be modified after registration.
                        </div>
                        
                        <form action="${pageContext.request.contextPath}/customer/edit" method="post">
                            <input type="hidden" name="customerId" value="${customer.customerId}">
                            
                            <!-- System Information Section (Read-only) -->
                            <div class="form-section">
                                <div class="form-section-title">
                                    <i class="fas fa-lock"></i>
                                    System Information (Read-only)
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label class="form-label">
                                            <i class="fas fa-id-badge text-brown-medium me-1"></i>
                                            Account Number
                                        </label>
                                        <div class="readonly-field">
                                            <input type="text" class="form-control" value="${customer.accountNumber}" readonly>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label">
                                            <i class="fas fa-id-card text-brown-medium me-1"></i>
                                            NIC Number
                                        </label>
                                        <div class="readonly-field">
                                            <input type="text" class="form-control" value="${customer.nicNumber}" readonly>
                                        </div>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label class="form-label">
                                            <i class="fas fa-calendar-plus text-brown-medium me-1"></i>
                                            Registered On
                                        </label>
                                        <div class="readonly-field">
                                            <input type="text" class="form-control" 
                                                   value="<fmt:formatDate value="${customer.registrationDate}" pattern="yyyy-MM-dd"/>" readonly>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label">
                                            <i class="fas fa-toggle-on text-brown-medium me-1"></i>
                                            Current Status
                                        </label>
                                        <div class="mt-2">
                                            <span class="status-badge ${customer.active ? 'active' : 'inactive'}">
                                                <i class="fas ${customer.active ? 'fa-check-circle' : 'fa-times-circle'} me-1"></i>
                                                ${customer.active ? 'Active' : 'Inactive'}
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Personal Information Section (Editable) -->
                            <div class="form-section">
                                <div class="form-section-title">
                                    <i class="fas fa-user"></i>
                                    Personal Information
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-12">
                                        <label for="customerName" class="form-label">
                                            Full Name <span class="required-indicator">*</span>
                                        </label>
                                        <div class="input-icon">
                                            <i class="fas fa-user"></i>
                                            <input type="text" class="form-control ${validationErrors.customerName != null ? 'is-invalid' : ''}" 
                                                   id="customerName" name="customerName" value="${customer.customerName}" 
                                                   placeholder="Enter customer's full name" required>
                                        </div>
                                        <c:if test="${validationErrors.customerName != null}">
                                            <div class="invalid-feedback">${validationErrors.customerName}</div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Address Information Section -->
                            <div class="form-section">
                                <div class="form-section-title">
                                    <i class="fas fa-map-marker-alt"></i>
                                    Address Information
                                </div>
                                <div class="mb-3">
                                    <label for="address" class="form-label">
                                        Address <span class="required-indicator">*</span>
                                    </label>
                                    <textarea class="form-control ${validationErrors.address != null ? 'is-invalid' : ''}" 
                                              id="address" name="address" rows="3" 
                                              placeholder="Enter complete address" required>${customer.address}</textarea>
                                    <c:if test="${validationErrors.address != null}">
                                        <div class="invalid-feedback">${validationErrors.address}</div>
                                    </c:if>
                                </div>
                                
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="city" class="form-label">City</label>
                                        <div class="input-icon">
                                            <i class="fas fa-city"></i>
                                            <input type="text" class="form-control ${validationErrors.city != null ? 'is-invalid' : ''}" 
                                                   id="city" name="city" value="${customer.city}" placeholder="Enter city">
                                        </div>
                                        <c:if test="${validationErrors.city != null}">
                                            <div class="invalid-feedback">${validationErrors.city}</div>
                                        </c:if>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="postalCode" class="form-label">Postal Code</label>
                                        <div class="input-icon">
                                            <i class="fas fa-mail-bulk"></i>
                                            <input type="text" class="form-control ${validationErrors.postalCode != null ? 'is-invalid' : ''}" 
                                                   id="postalCode" name="postalCode" value="${customer.postalCode}" 
                                                   placeholder="Enter postal code">
                                        </div>
                                        <c:if test="${validationErrors.postalCode != null}">
                                            <div class="invalid-feedback">${validationErrors.postalCode}</div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Contact Information Section -->
                            <div class="form-section">
                                <div class="form-section-title">
                                    <i class="fas fa-phone"></i>
                                    Contact Information
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-4">
                                        <label for="telephone" class="form-label">
                                            Telephone <span class="required-indicator">*</span>
                                        </label>
                                        <div class="input-icon">
                                            <i class="fas fa-phone"></i>
                                            <input type="tel" class="form-control ${validationErrors.telephone != null ? 'is-invalid' : ''}" 
                                                   id="telephone" name="telephone" value="${customer.telephone}" 
                                                   placeholder="Enter telephone number" required>
                                        </div>
                                        <c:if test="${validationErrors.telephone != null}">
                                            <div class="invalid-feedback">${validationErrors.telephone}</div>
                                        </c:if>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="mobile" class="form-label">Mobile</label>
                                        <div class="input-icon">
                                            <i class="fas fa-mobile-alt"></i>
                                            <input type="tel" class="form-control ${validationErrors.mobile != null ? 'is-invalid' : ''}" 
                                                   id="mobile" name="mobile" value="${customer.mobile}" 
                                                   placeholder="Enter mobile number">
                                        </div>
                                        <c:if test="${validationErrors.mobile != null}">
                                            <div class="invalid-feedback">${validationErrors.mobile}</div>
                                        </c:if>
                                    </div>
                                    <div class="col-md-4">
                                        <label for="email" class="form-label">Email</label>
                                        <div class="input-icon">
                                            <i class="fas fa-envelope"></i>
                                            <input type="email" class="form-control ${validationErrors.email != null ? 'is-invalid' : ''}" 
                                                   id="email" name="email" value="${customer.email}" 
                                                   placeholder="Enter email address">
                                        </div>
                                        <c:if test="${validationErrors.email != null}">
                                            <div class="invalid-feedback">${validationErrors.email}</div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                            
                            <hr>
                            
                            <div class="text-end">
                                <button type="reset" class="btn btn-secondary me-2">
                                    <i class="fas fa-undo"></i> Reset Changes
                                </button>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save"></i> Update Customer
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
        // Add visual feedback on form submission
        document.querySelector('form').addEventListener('submit', function(e) {
            const submitBtn = document.querySelector('button[type="submit"]');
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Updating...';
            submitBtn.disabled = true;
        });
        
        // Highlight changed fields
        document.addEventListener('DOMContentLoaded', function() {
            const inputs = document.querySelectorAll('input:not([readonly]), textarea:not([readonly])');
            inputs.forEach(input => {
                const originalValue = input.value;
                input.addEventListener('input', function() {
                    if (this.value !== originalValue) {
                        this.style.borderColor = 'var(--accent-green)';
                        this.style.backgroundColor = '#E8F5E9';
                    } else {
                        this.style.borderColor = '';
                        this.style.backgroundColor = '';
                    }
                });
            });
        });
    </script>
</body>
</html>