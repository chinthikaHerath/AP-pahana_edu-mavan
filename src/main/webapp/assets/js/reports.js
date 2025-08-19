/**
 * Reports JavaScript Functions
 * Common functions for report generation and display
 */

$(document).ready(function() {
    // Initialize date inputs with today's date if empty
    initializeDateInputs();
    
    // Add print styles
    addPrintStyles();
    
    // Initialize tooltips
    $('[data-bs-toggle="tooltip"]').tooltip();
    
    // Date range validation
    $('#startDate, #endDate').on('change', validateDateRange);
});

/**
 * Initialize date inputs
 */
function initializeDateInputs() {
    var today = new Date();
    var dateStr = today.toISOString().split('T')[0];
    
    // Set max date to today for all date inputs
    $('input[type="date"]').attr('max', dateStr);
    
    // Set default date if empty
    $('input[type="date"]').each(function() {
        if (!$(this).val()) {
            $(this).val(dateStr);
        }
    });
}

/**
 * Validate date range
 */
function validateDateRange() {
    var startDate = $('#startDate').val();
    var endDate = $('#endDate').val();
    
    if (startDate && endDate) {
        if (startDate > endDate) {
            showAlert('Start date cannot be after end date', 'warning');
            $('#endDate').val(startDate);
        }
        
        // Check if range is more than 1 year
        var start = new Date(startDate);
        var end = new Date(endDate);
        var diffDays = (end - start) / (1000 * 60 * 60 * 24);
        
        if (diffDays > 365) {
            showAlert('Date range cannot exceed 365 days', 'warning');
        }
    }
}

/**
 * Show alert message
 */
function showAlert(message, type) {
    var alertHtml = '<div class="alert alert-' + type + ' alert-dismissible fade show" role="alert">' +
                    message +
                    '<button type="button" class="btn-close" data-bs-dismiss="alert"></button>' +
                    '</div>';
    
    // Remove existing alerts
    $('.alert').remove();
    
    // Add new alert after page header
    $('main h1').after(alertHtml);
}

/**
 * Export table to CSV
 */
function exportTableToCSV(tableId, filename) {
    var csv = [];
    var rows = document.querySelectorAll("#" + tableId + " tr");
    
    for (var i = 0; i < rows.length; i++) {
        var row = [], cols = rows[i].querySelectorAll("td, th");
        
        for (var j = 0; j < cols.length; j++) {
            var data = cols[j].innerText.replace(/,/g, '');
            data = data.replace(/"/g, '""');
            if (data.indexOf(',') > -1 || data.indexOf('"') > -1) {
                data = '"' + data + '"';
            }
            row.push(data);
        }
        
        csv.push(row.join(","));
    }
    
    downloadCSV(csv.join("\n"), filename);
}

/**
 * Download CSV file
 */
function downloadCSV(csv, filename) {
    var csvFile;
    var downloadLink;
    
    csvFile = new Blob([csv], {type: "text/csv"});
    downloadLink = document.createElement("a");
    downloadLink.download = filename;
    downloadLink.href = window.URL.createObjectURL(csvFile);
    downloadLink.style.display = "none";
    document.body.appendChild(downloadLink);
    downloadLink.click();
}

/**
 * Print report
 */
function printReport() {
    window.print();
}

/**
 * Add print styles
 */
function addPrintStyles() {
    var style = document.createElement('style');
    style.innerHTML = `
        @media print {
            /* Hide navigation and buttons */
            .navbar, .sidebar, .btn-toolbar, .card-header .btn, 
            form, .alert, footer {
                display: none !important;
            }
            
            /* Remove margins and shadows */
            .card {
                border: 1px solid #ddd !important;
                box-shadow: none !important;
                margin-bottom: 20px !important;
            }
            
            /* Ensure tables fit on page */
            table {
                font-size: 12px !important;
            }
            
            /* Page break control */
            .card {
                page-break-inside: avoid;
            }
            
            /* Report header styling */
            #reportContent {
                margin: 0;
                padding: 20px;
            }
            
            /* Show print date */
            #reportContent::after {
                content: "Printed on: " attr(data-print-date);
                display: block;
                text-align: center;
                margin-top: 30px;
                font-size: 10px;
                color: #666;
            }
        }
    `;
    document.head.appendChild(style);
    
    // Set print date
    $('#reportContent').attr('data-print-date', new Date().toLocaleString());
}

/**
 * Format currency
 */
function formatCurrency(amount) {
    return 'LKR ' + parseFloat(amount).toLocaleString('en-US', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    });
}

/**
 * Generate chart colors
 */
function generateChartColors(count) {
    var colors = [
        'rgba(255, 99, 132, 0.5)',
        'rgba(54, 162, 235, 0.5)',
        'rgba(255, 206, 86, 0.5)',
        'rgba(75, 192, 192, 0.5)',
        'rgba(153, 102, 255, 0.5)',
        'rgba(255, 159, 64, 0.5)',
        'rgba(199, 199, 199, 0.5)',
        'rgba(83, 102, 255, 0.5)',
        'rgba(255, 99, 255, 0.5)',
        'rgba(99, 255, 132, 0.5)'
    ];
    
    return colors.slice(0, count);
}

/**
 * Update chart data
 */
function updateChart(chart, labels, data) {
    chart.data.labels = labels;
    chart.data.datasets[0].data = data;
    chart.update();
}

/**
 * Toggle report sections
 */
function toggleReportSection(sectionId) {
    $('#' + sectionId).slideToggle();
    var icon = $('[data-toggle-section="' + sectionId + '"] i');
    icon.toggleClass('fa-chevron-down fa-chevron-up');
}

/**
 * Filter table rows
 */
function filterTable(inputId, tableId) {
    var input = document.getElementById(inputId);
    var filter = input.value.toUpperCase();
    var table = document.getElementById(tableId);
    var tr = table.getElementsByTagName("tr");
    
    for (var i = 1; i < tr.length; i++) {
        var visible = false;
        var td = tr[i].getElementsByTagName("td");
        
        for (var j = 0; j < td.length; j++) {
            if (td[j]) {
                var txtValue = td[j].textContent || td[j].innerText;
                if (txtValue.toUpperCase().indexOf(filter) > -1) {
                    visible = true;
                    break;
                }
            }
        }
        
        tr[i].style.display = visible ? "" : "none";
    }
}

/**
 * Sort table
 */
function sortTable(tableId, columnIndex) {
    var table = document.getElementById(tableId);
    var rows = table.rows;
    var switching = true;
    var shouldSwitch;
    var i;
    var x, y;
    var switchcount = 0;
    var dir = "asc";
    
    while (switching) {
        switching = false;
        
        for (i = 1; i < (rows.length - 1); i++) {
            shouldSwitch = false;
            x = rows[i].getElementsByTagName("TD")[columnIndex];
            y = rows[i + 1].getElementsByTagName("TD")[columnIndex];
            
            if (dir == "asc") {
                if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                    shouldSwitch = true;
                    break;
                }
            } else if (dir == "desc") {
                if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                    shouldSwitch = true;
                    break;
                }
            }
        }
        
        if (shouldSwitch) {
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
            switchcount++;
        } else {
            if (switchcount == 0 && dir == "asc") {
                dir = "desc";
                switching = true;
            }
        }
    }
}