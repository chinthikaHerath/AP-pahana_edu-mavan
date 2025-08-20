<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agent Login - Pahana Edu Online Billing System</title>
    
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    
    <style>
        :root {
            --primary-color: #19D467;
            --primary-dark: #0DA35B;
            --primary-light: #41F793;
            --secondary-color: #64F89C;
            --success-color: #4CAF50;
            --error-color: #F44336;
            --warning-color: #FF9800;
            --text-primary: #1a1a1a;
            --text-secondary: #6b7280;
            --background-light: #f8fafc;
            --surface-white: #ffffff;
            --border-light: #e5e7eb;
            --shadow-subtle: 0 1px 3px rgba(0, 0, 0, 0.1);
            --shadow-elevated: 0 10px 25px rgba(0, 0, 0, 0.1);
            --border-radius: 12px;
            --transition-standard: 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            --accent-orange: #fb923c;
            --accent-dots: #fbbf24;
        }
        
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            background: linear-gradient(75deg, #41CB71 0%, #e2e8f0 100%);
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            line-height: 1.6;
            color: var(--text-primary);
            min-height: 100vh;
            position: relative;
            overflow-x: hidden;
        }
        
        /* Decorative Elements */
        .decoration-elements {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            pointer-events: none;
            z-index: 1;
        }
        
        .floating-shape {
            position: absolute;
            border-radius: var(--border-radius);
            opacity: 0.6;
        }
        
        .shape-1 {
            width: 120px;
            height: 80px;
            background: var(--surface-white);
            border: 2px solid var(--border-light);
            top: 10%;
            left: 8%;
            transform: rotate(-15deg);
        }
        
        .shape-2 {
            width: 100px;
            height: 100px;
            background: var(--accent-orange);
            top: 20%;
            right: 15%;
            border-radius: 20px;
            display: flex;
            flex-wrap: wrap;
            padding: 8px;
            gap: 6px;
        }
        
        .dot {
            width: 8px;
            height: 8px;
            background: rgba(0, 0, 0, 0.3);
            border-radius: 50%;
        }
        
        .shape-3 {
            width: 80px;
            height: 100px;
            background: var(--surface-white);
            border: 2px solid var(--border-light);
            bottom: 25%;
            left: 12%;
            transform: rotate(20deg);
        }
        
        .shape-4 {
            width: 90px;
            height: 90px;
            background: var(--accent-orange);
            bottom: 15%;
            right: 10%;
            border-radius: 15px;
            display: flex;
            flex-wrap: wrap;
            padding: 6px;
            gap: 4px;
        }
        
        .wavy-line {
            position: absolute;
            width: 200px;
            height: 2px;
            background: var(--border-light);
            border-radius: 1px;
        }
        
        .wavy-1 {
            top: 35%;
            left: 5%;
            transform: rotate(-25deg);
        }
        
        .wavy-2 {
            top: 60%;
            right: 8%;
            transform: rotate(15deg);
        }
        
        .wavy-3 {
            bottom: 40%;
            left: 15%;
            transform: rotate(-10deg);
        }
        
        .main-container {
            position: relative;
            z-index: 10;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }
        
        /* Header */
        .page-header {
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            padding: 20px 40px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            z-index: 20;
        }
        
        .logo-section {
            display: flex;
            align-items: center;
            font-weight: 600;
            color: var(--text-primary);
            font-size: 25px;
        }
        
        .logo-section i {
            margin-right: 8px;
            color: var(--text-primary);
        }
        
        .header-actions {
            display: flex;
            gap: 16px;
            align-items: center;
        }
        
        .header-link {
            color: var(--text-secondary);
            text-decoration: none;
            font-weight: 500;
            font-size: 14px;
            transition: color var(--transition-standard);
        }
        
        .header-link:hover {
            color: var(--primary-color);
        }
        
        .demo-btn {
            background: var(--accent-orange);
            color: white;
            padding: 8px 20px;
            border-radius: 20px;
            text-decoration: none;
            font-weight: 500;
            font-size: 14px;
            transition: all var(--transition-standard);
        }
        
        .demo-btn:hover {
            background: #ea580c;
            transform: translateY(-1px);
            box-shadow: 0 4px 12px rgba(251, 146, 60, 0.3);
        }
        
        /* Login Container */
        .login-container {
            background: var(--surface-white);
            border-radius: 24px;
            box-shadow: var(--shadow-elevated);
            overflow: hidden;
            width: 100%;
            max-width: 380px;
            border: 1px solid rgba(255, 255, 255, 0.2);
            backdrop-filter: blur(10px);
        }
        
        /* Character Illustration */
        .character-section {
            position: relative;
            padding: 40px 32px 20px;
            text-align: center;
            background: linear-gradient(135deg, rgba(25, 118, 210, 0.05) 0%, rgba(25, 118, 210, 0.02) 100%);
        }
        
        .character-image {
            width: 120px;
            height: 140px;
            margin: 0 auto 20px;
            background: linear-gradient(135deg, var(--text-primary) 0%, #374151 100%);
            border-radius: 20px 20px 60px 20px;
            position: relative;
            overflow: hidden;
        }
        
        .character-image::before {
            content: '';
            position: absolute;
            top: 30%;
            left: 50%;
            transform: translateX(-50%);
            width: 60px;
            height: 40px;
            background: var(--surface-white);
            border-radius: 50% 50% 50% 50% / 60% 60% 40% 40%;
        }
        
        .character-image::after {
            content: '';
            position: absolute;
            top: 25%;
            left: 50%;
            transform: translateX(-50%);
            width: 80px;
            height: 80px;
            background: var(--text-primary);
            border-radius: 50%;
        }
        
        .character-hair {
            position: absolute;
            top: 15%;
            left: 50%;
            transform: translateX(-50%);
            width: 90px;
            height: 50px;
            background: var(--text-primary);
            border-radius: 50% 50% 20% 20%;
        }
        
        .character-body {
            position: absolute;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
            width: 100px;
            height: 80px;
            background: linear-gradient(135deg, var(--surface-white) 0%, #f3f4f6 100%);
            border-radius: 20px 20px 0 0;
        }
        
        .login-title {
            font-size: 28px;
            font-weight: 700;
            color: var(--text-primary);
            margin-bottom: 8px;
            letter-spacing: -0.02em;
        }
        
        .login-subtitle {
            color: var(--text-secondary);
            font-size: 16px;
            font-weight: 400;
            margin-bottom: 0;
        }
        
        /* Form Section */
        .login-body {
            padding: 32px;
        }
        
        .form-group {
            margin-bottom: 24px;
            position: relative;
        }
        
        .form-label {
            display: block;
            font-size: 14px;
            font-weight: 600;
            color: var(--text-primary);
            margin-bottom: 8px;
        }
        
        .input-wrapper {
            position: relative;
        }
        
        .form-control {
            width: 100%;
            height: 52px;
            padding: 0 16px;
            font-size: 16px;
            border: 2px solid var(--border-light);
            border-radius: var(--border-radius);
            background: var(--surface-white);
            transition: all var(--transition-standard);
            outline: none;
            font-weight: 400;
        }
        
        .form-control::placeholder {
            color: var(--text-secondary);
            font-weight: 400;
        }
        
        .form-control:focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 0 4px rgba(25, 118, 210, 0.1);
            transform: translateY(-1px);
        }
        
        .form-control:invalid:not(:focus):not(:placeholder-shown) {
            border-color: var(--error-color);
            box-shadow: 0 0 0 4px rgba(244, 67, 54, 0.1);
        }
        
        .input-icon {
            position: absolute;
            right: 16px;
            top: 50%;
            transform: translateY(-50%);
            color: var(--text-secondary);
            font-size: 18px;
            pointer-events: none;
            transition: color var(--transition-standard);
        }
        
        .form-control:focus ~ .input-icon {
            color: var(--primary-color);
        }
        
        .password-toggle {
            position: absolute;
            right: 16px;
            top: 50%;
            transform: translateY(-50%);
            color: var(--text-secondary);
            cursor: pointer;
            font-size: 18px;
            transition: color var(--transition-standard);
            padding: 4px;
        }
        
        .password-toggle:hover {
            color: var(--primary-color);
        }
        
        .form-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin: 24px 0;
        }
        
        .form-check {
            display: flex;
            align-items: center;
        }
        
        .form-check-input {
            width: 18px;
            height: 18px;
            margin-right: 10px;
            border: 2px solid var(--border-light);
            border-radius: 4px;
            cursor: pointer;
            transition: all var(--transition-standard);
        }
        
        .form-check-input:checked {
            background-color: var(--primary-color);
            border-color: var(--primary-color);
        }
        
        .form-check-label {
            font-size: 14px;
            color: var(--text-secondary);
            cursor: pointer;
            user-select: none;
            font-weight: 500;
        }
        
        .forgot-link {
            color: var(--primary-color);
            text-decoration: none;
            font-size: 14px;
            font-weight: 500;
            transition: color var(--transition-standard);
        }
        
        .forgot-link:hover {
            color: var(--primary-dark);
        }
        
        .btn-login {
            width: 100%;
            height: 52px;
            background: var(--primary-color);
            color: #1a1a1a;
            border: none;
            border-radius: var(--border-radius);
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all var(--transition-standard);
            margin-bottom: 24px;
            position: relative;
        }
        
        .btn-login:hover:not(:disabled) {
            background: var(--primary-dark);
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(25, 118, 210, 0.3);
        }
        
        .btn-login:active:not(:disabled) {
            transform: translateY(0);
        }
        
        .btn-login:disabled {
            opacity: 0.7;
            cursor: not-allowed;
        }
        
        .btn-login.loading {
            color: transparent;
        }
        
        .btn-login.loading::after {
            content: '';
            position: absolute;
            top: 50%;
            left: 50%;
            width: 20px;
            height: 20px;
            margin: -10px 0 0 -10px;
            border: 2px solid transparent;
            border-top: 2px solid white;
            border-radius: 50%;
            animation: spin 0.8s linear infinite;
        }
        
        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
        
        /* Social Login */
        .social-divider {
            text-align: center;
            margin: 24px 0;
            position: relative;
            color: var(--text-secondary);
            font-size: 14px;
            font-weight: 500;
        }
        
        .social-divider::before {
            content: '';
            position: absolute;
            top: 50%;
            left: 0;
            right: 0;
            height: 1px;
            background: var(--border-light);
            z-index: 1;
        }
        
        .social-divider span {
            background: var(--surface-white);
            padding: 0 20px;
            position: relative;
            z-index: 2;
        }
        
        .social-buttons {
            display: flex;
            gap: 12px;
            margin-bottom: 24px;
        }
        
        .social-btn {
            flex: 1;
            height: 48px;
            border: 2px solid var(--border-light);
            border-radius: var(--border-radius);
            background: var(--surface-white);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 14px;
            font-weight: 500;
            text-decoration: none;
            color: var(--text-primary);
            transition: all var(--transition-standard);
        }
        
        .social-btn:hover {
            border-color: var(--primary-color);
            color: var(--primary-color);
            transform: translateY(-1px);
        }
        
        .social-btn i {
            margin-right: 8px;
            font-size: 16px;
        }
        
        /* Footer */
        .login-footer {
            background: rgba(248, 250, 252, 0.8);
            padding: 24px 32px;
            text-align: center;
            border-top: 1px solid var(--border-light);
        }
        
        .footer-text {
            font-size: 13px;
            color: var(--text-secondary);
            margin-bottom: 12px;
        }
        
        .signup-link {
            color: var(--primary-color);
            text-decoration: none;
            font-weight: 600;
        }
        
        .signup-link:hover {
            color: var(--primary-dark);
        }
        
        .credentials-info {
            display: inline-flex;
            align-items: center;
            background: rgba(25, 118, 210, 0.1);
            color: var(--primary-color);
            padding: 8px 16px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            margin-top: 8px;
        }
        
        .credentials-info i {
            margin-right: 6px;
            font-size: 12px;
        }
        
        /* Alert Styles */
        .alert {
            padding: 16px 20px;
            border-radius: var(--border-radius);
            margin-bottom: 24px;
            font-size: 14px;
            border: none;
            display: flex;
            align-items: center;
            font-weight: 500;
        }
        
        .alert i {
            margin-right: 12px;
            font-size: 16px;
        }
        
        .alert-danger {
            background: rgba(244, 67, 54, 0.1);
            color: var(--error-color);
        }
        
        .alert-success {
            background: rgba(76, 175, 80, 0.1);
            color: var(--success-color);
        }
        
        .alert-warning {
            background: rgba(255, 152, 0, 0.1);
            color: var(--warning-color);
        }
        
        .alert-info {
            background: rgba(25, 118, 210, 0.1);
            color: var(--primary-color);
        }
        
        /* Error states */
        .form-group.error .form-control {
            border-color: var(--error-color);
            box-shadow: 0 0 0 4px rgba(244, 67, 54, 0.1);
        }
        
        .error-message {
            font-size: 12px;
            color: var(--error-color);
            margin-top: 6px;
            display: none;
            font-weight: 500;
        }
        
        .form-group.error .error-message {
            display: block;
        }
        
        /* Focus visible for keyboard navigation */
        .btn-login:focus-visible,
        .form-control:focus-visible,
        .form-check-input:focus-visible {
            outline: 2px solid var(--primary-color);
            outline-offset: 2px;
        }
        
        /* Responsive design */
        @media (max-width: 768px) {
            .page-header {
                padding: 16px 20px;
            }
            
            .logo-section {
                font-size: 16px;
            }
            
            .demo-btn {
                padding: 6px 16px;
                font-size: 13px;
            }
            
            .floating-shape {
                display: none;
            }
            
            .wavy-line {
                display: none;
            }
        }
        
        @media (max-width: 480px) {
            body {
                padding: 0;
            }
            
            .main-container {
                padding: 16px;
            }
            
            .login-container {
                max-width: none;
                border-radius: 16px;
            }
            
            .character-section {
                padding: 32px 24px 16px;
            }
            
            .character-image {
                width: 100px;
                height: 120px;
            }
            
            .login-title {
                font-size: 24px;
            }
            
            .login-body {
                padding: 24px;
            }
            
            .login-footer {
                padding: 20px 24px;
            }
            
            .social-buttons {
                flex-direction: column;
            }
            
            .form-actions {
                flex-direction: column;
                gap: 16px;
                align-items: stretch;
            }
        }
        
        /* High contrast mode support */
        @media (prefers-contrast: high) {
            :root {
                --border-light: #333;
                --text-secondary: #333;
            }
        }
        
        /* Reduced motion support */
        @media (prefers-reduced-motion: reduce) {
            * {
                transition: none !important;
                animation: none !important;
            }
        }
    </style>
</head>
<body>
    <!-- Header -->
    <div class="page-header">
        <div class="logo-section">
            <i class="fas fa-graduation-cap"></i>
            Pahana Edu
        </div>
        <div class="header-actions">
            <a href="#" class="header-link">
                <i class="fas fa-globe"></i>
            </a>
        </div>
    </div>

    <!-- Decorative Elements -->
    <div class="decoration-elements">
        <div class="floating-shape shape-1"></div>
        <div class="floating-shape shape-2">
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
        </div>
        <div class="floating-shape shape-3"></div>
        <div class="floating-shape shape-4">
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
            <div class="dot"></div>
        </div>
        <div class="wavy-line wavy-1"></div>
        <div class="wavy-line wavy-2"></div>
        <div class="wavy-line wavy-3"></div>
    </div>

    <!-- Main Container -->
    <div class="main-container">
        <div class="login-container">
            <!-- Character Section -->
            <div class="character-section">
                <div class="character-image">
                    <div class="character-hair"></div>
                    <div class="character-body"></div>
                </div>
                <h1 class="login-title">Agent Login</h1>
                <p class="login-subtitle">Hey, Enter your details to get sign in<br>to your account</p>
            </div>
            
            <!-- Form Section -->
            <div class="login-body">
                <!-- Include messages -->
                <jsp:include page="/includes/messages.jsp" />
                
                <form action="${pageContext.request.contextPath}/login" method="POST" id="loginForm" novalidate>
                    <div class="form-group">
                        <label for="username" class="form-label">Enter User name</label>
                        <div class="input-wrapper">
                            <input type="text" 
                                   class="form-control" 
                                   id="username" 
                                   name="username" 
                                   value="${username}" 
                                   placeholder="Enter your user name"
                                   required 
                                   autofocus
                                   autocomplete="username">
                            <i class="fas fa-user input-icon"></i>
                        </div>
                        <div class="error-message">Please enter a valid user name</div>
                    </div>
                    
                    <div class="form-group">
                        <label for="password" class="form-label">Passcode</label>
                        <div class="input-wrapper">
                            <input type="password" 
                                   class="form-control" 
                                   id="password" 
                                   name="password" 
                                   placeholder="Enter your password"
                                   required
                                   autocomplete="current-password">
                            <i class="fas fa-eye password-toggle" 
                               onclick="togglePassword()" 
                               title="Show/Hide Password"
                               role="button"
                               tabindex="0"></i>
                        </div>
                        <div class="error-message">Please enter your password</div>
                    </div>
                    
                    <div class="form-actions">
                        <div class="form-check">
                            <input type="checkbox" 
                                   class="form-check-input" 
                                   id="rememberMe" 
                                   name="rememberMe">
                            <label class="form-check-label" for="rememberMe">
                              Remember Me
                            </label>
                        </div>
                       
                    </div>
                    
                    <button type="submit" class="btn-login" id="loginBtn">
                        Sign in
                    </button>
                </form>
            </div>
            
            <!-- Footer -->
            <div class="login-footer">
             <p>&copy; 2024 Pahana Edu. All rights reserved.</p>
                <p class="mb-0">
                
                <div class="credentials-info">
               
                    <i class="fas fa-info-circle"></i>
                    Default: admin/admin123
                </div>
            </div>
        </div>
    </div>
    
    <!-- jQuery -->
    <script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>
    <!-- Bootstrap JS -->
    <script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>
    
    <script>
        $(document).ready(function() {
            // Auto-hide alerts after 5 seconds
            setTimeout(function() {
                $('.alert').fadeOut('slow');
            }, 5000);
            
            // Form validation
            const form = document.getElementById('loginForm');
            const usernameField = document.getElementById('username');
            const passwordField = document.getElementById('password');
            const loginBtn = document.getElementById('loginBtn');
            
            // Real-time validation
            function validateField(field) {
                const formGroup = field.closest('.form-group');
                if (field.validity.valid && field.value.trim() !== '') {
                    formGroup.classList.remove('error');
                    return true;
                } else {
                    formGroup.classList.add('error');
                    return false;
                }
            }
            
            usernameField.addEventListener('blur', () => validateField(usernameField));
            passwordField.addEventListener('blur', () => validateField(passwordField));
            
            // Form submission
            form.addEventListener('submit', function(e) {
                e.preventDefault();
                
                const isUsernameValid = validateField(usernameField);
                const isPasswordValid = validateField(passwordField);
                
                if (isUsernameValid && isPasswordValid) {
                    loginBtn.classList.add('loading');
                    loginBtn.disabled = true;
                    
                    // Submit form after short delay for UX
                    setTimeout(() => {
                        form.submit();
                    }, 500);
                }
            });
            
            // Prevent double submission
            let isSubmitting = false;
            form.addEventListener('submit', function() {
                if (isSubmitting) {
                    return false;
                }
                isSubmitting = true;
            });
        });
        
        // Password toggle function
        function togglePassword() {
            const passwordField = document.getElementById('password');
            const toggleIcon = document.querySelector('.password-toggle');
            
            if (passwordField.type === 'password') {
                passwordField.type = 'text';
                toggleIcon.classList.remove('fa-eye');
                toggleIcon.classList.add('fa-eye-slash');
                toggleIcon.title = 'Hide Password';
            } else {
                passwordField.type = 'password';
                toggleIcon.classList.remove('fa-eye-slash');
                toggleIcon.classList.add('fa-eye');
                toggleIcon.title = 'Show Password';
            }
        }
        
        // Keyboard accessibility for password toggle
        document.querySelector('.password-toggle').addEventListener('keydown', function(e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                togglePassword();
            }
        });
        
        // Add subtle animations to decorative elements
        function animateDecorations() {
            const shapes = document.querySelectorAll('.floating-shape');
            const lines = document.querySelectorAll('.wavy-line');
            
            shapes.forEach((shape, index) => {
                shape.style.animation = `float ${3 + index * 0.5}s ease-in-out infinite alternate`;
            });
            
            lines.forEach((line, index) => {
                line.style.animation = `sway ${4 + index * 0.3}s ease-in-out infinite alternate`;
            });
        }
        
        // CSS animations for decorative elements
        const style = document.createElement('style');
        style.textContent = `
            @keyframes float {
                0% { transform: translateY(0px) rotate(var(--start-rotation, 0deg)); }
                100% { transform: translateY(-10px) rotate(var(--end-rotation, 5deg)); }
            }
            
            @keyframes sway {
                0% { transform: rotate(var(--start-rotation, 0deg)); }
                100% { transform: rotate(var(--end-rotation, 2deg)); }
            }
            
            .shape-1 {
                --start-rotation: -15deg;
                --end-rotation: -10deg;
            }
            
            .shape-2 {
                --start-rotation: 0deg;
                --end-rotation: 5deg;
            }
            
            .shape-3 {
                --start-rotation: 20deg;
                --end-rotation: 25deg;
            }
            
            .shape-4 {
                --start-rotation: 0deg;
                --end-rotation: -5deg;
            }
            
            .wavy-1 {
                --start-rotation: -25deg;
                --end-rotation: -23deg;
            }
            
            .wavy-2 {
                --start-rotation: 15deg;
                --end-rotation: 17deg;
            }
            
            .wavy-3 {
                --start-rotation: -10deg;
                --end-rotation: -8deg;
            }
        `;
        document.head.appendChild(style);
        
        // Initialize animations when page loads
        window.addEventListener('load', animateDecorations);
    </script>
</body>
</html>