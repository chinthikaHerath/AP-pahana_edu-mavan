<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Footer -->
<footer class="footer mt-auto py-3 bg-light">
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-6">
                <span class="text-muted">
                    &copy; 2025 Pahana Edu Bookshop. All rights reserved.
                </span>
            </div>
            <div class="col-md-6 text-md-end">
                <span class="text-muted">
                    Version 1.0.0 
                </span>
            </div>
        </div>
    </div>
</footer>

<!-- Core JavaScript -->
<!-- jQuery (if not already included) -->
<script src="${pageContext.request.contextPath}/assets/js/jquery.min.js"></script>

<!-- Bootstrap Bundle with Popper -->
<script src="${pageContext.request.contextPath}/assets/js/bootstrap.bundle.min.js"></script>

<!-- DataTables JS (for tables) -->
<script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.11.5/js/dataTables.bootstrap5.min.js"></script>

<!-- Common JavaScript -->
<script src="${pageContext.request.contextPath}/assets/js/common.js"></script>

<!-- Auto-hide alerts after 5 seconds -->
<script>
    $(document).ready(function() {
        // Auto-hide alerts
        setTimeout(function() {
            $('.alert').fadeOut('slow');
        }, 5000);
        
        // Initialize tooltips
        var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
        var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl)
        });
        
        // Initialize popovers
        var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
        var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
            return new bootstrap.Popover(popoverTriggerEl)
        });
    });
</script>

<!-- Additional page-specific JavaScript -->
<c:if test="${not empty pageJS}">
    ${pageJS}
</c:if>