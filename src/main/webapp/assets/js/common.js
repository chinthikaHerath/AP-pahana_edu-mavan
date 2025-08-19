// Common JavaScript Functions for Pahana Edu Online Billing System

// Document Ready
$(document).ready(function() {
    // Initialize components
    initializeTooltips();
    initializePopovers();
    initializeDataTables();
    initializeSidebarToggle();
    initializeFormValidation();
    
    // Auto-hide alerts
    autoHideAlerts();
    
    // Set active menu item
    setActiveMenuItem();
});

// Initialize Bootstrap tooltips
function initializeTooltips() {
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

// Initialize Bootstrap popovers
function initializePopovers() {
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
}

// Initialize DataTables
function initializeDataTables() {
    if ($.fn.DataTable) {
        $('.datatable').DataTable({
            "pageLength": 10,
            "lengthMenu": [[10, 25, 50, 100], [10, 25, 50, 100]],
            "language": {
                "search": "Search:",
                "lengthMenu": "Show _MENU_ entries",
                "info": "Showing _START_ to _END_ of _TOTAL_ entries",
                "paginate": {
                    "first": "First",
                    "last": "Last",
                    "next": "Next",
                    "previous": "Previous"
                }
            },
            "responsive": true,
            "order": [[0, "desc"]]
        });
    }
}

// Sidebar toggle for mobile
function initializeSidebarToggle() {
    $('#sidebarToggle').on('click', function() {
        $('body').toggleClass('sidebar-toggled');
        $('.sidebar').toggleClass('toggled');
    });
}

// Form validation
function initializeFormValidation() {
    // Bootstrap form validation
    var forms = document.querySelectorAll('.needs-validation');
    Array.prototype.slice.call(forms).forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
}

// Auto-hide alerts after 5 seconds
function autoHideAlerts() {
    setTimeout(function() {
        $('.alert:not(.alert-permanent)').fadeOut('slow', function() {
            $(this).remove();
        });
    }, 5000);
}

// Set active menu item based on current URL
function setActiveMenuItem() {
    var currentPath = window.location.pathname;
    $('.nav-link').each(function() {
        var href = $(this).attr('href');
        if (href && currentPath.indexOf(href) > -1) {
            $(this).addClass('active');
        }
    });
}

// Show loading spinner
function showLoading() {
    $('body').append('<div class="loading-overlay"><div class="spinner-border text-primary" role="status"><span class="visually-hidden">Loading...</span></div></div>');
}

// Hide loading spinner
function hideLoading() {
    $('.loading-overlay').remove();
}

// Confirm dialog
function confirmAction(message, callback) {
    if (confirm(message)) {
        callback();
    }
}

// Show toast notification
function showToast(message, type = 'info') {
    var toastHtml = `
        <div class="toast align-items-center text-white bg-${type} border-0" role="alert" aria-live="assertive" aria-atomic="true">
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    `;
    
    var toastContainer = $('#toast-container');
    if (toastContainer.length === 0) {
        $('body').append('<div id="toast-container" class="toast-container position-fixed bottom-0 end-0 p-3"></div>');
        toastContainer = $('#toast-container');
    }
    
    var toastElement = $(toastHtml);
    toastContainer.append(toastElement);
    
    var toast = new bootstrap.Toast(toastElement[0]);
    toast.show();
    
    // Remove toast after it's hidden
    toastElement.on('hidden.bs.toast', function () {
        $(this).remove();
    });
}

// Format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-LK', {
        style: 'currency',
        currency: 'LKR'
    }).format(amount);
}

// Format date
function formatDate(date) {
    if (!date) return '';
    var d = new Date(date);
    return d.toLocaleDateString('en-GB');
}

// Format datetime
function formatDateTime(date) {
    if (!date) return '';
    var d = new Date(date);
    return d.toLocaleDateString('en-GB') + ' ' + d.toLocaleTimeString('en-GB');
}

// AJAX error handler
function handleAjaxError(xhr, status, error) {
    hideLoading();
    var message = 'An error occurred. Please try again.';
    if (xhr.responseJSON && xhr.responseJSON.message) {
        message = xhr.responseJSON.message;
    } else if (xhr.responseText) {
        message = xhr.responseText;
    }
    showToast(message, 'danger');
}

// Debounce function
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Print function
function printContent(elementId) {
    var content = document.getElementById(elementId).innerHTML;
    var originalContent = document.body.innerHTML;
    
    document.body.innerHTML = content;
    window.print();
    document.body.innerHTML = originalContent;
    
    // Reinitialize JavaScript components
    location.reload();
}

// Export to CSV
function exportToCSV(tableId, filename) {
    var csv = [];
    var rows = document.querySelectorAll("#" + tableId + " tr");
    
    for (var i = 0; i < rows.length; i++) {
        var row = [], cols = rows[i].querySelectorAll("td, th");
        
        for (var j = 0; j < cols.length; j++) {
            row.push('"' + cols[j].innerText.replace(/"/g, '""') + '"');
        }
        
        csv.push(row.join(","));
    }
    
    // Download CSV
    var csvContent = csv.join("\n");
    var blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    var link = document.createElement("a");
    
    if (link.download !== undefined) {
        var url = URL.createObjectURL(blob);
        link.setAttribute("href", url);
        link.setAttribute("download", filename + ".csv");
        link.style.visibility = 'hidden';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }
}

// Loading styles
$('<style>')
    .prop('type', 'text/css')
    .html(`
        .loading-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 9999;
        }
        .loading-overlay .spinner-border {
            width: 3rem;
            height: 3rem;
        }
    `)
    .appendTo('head');