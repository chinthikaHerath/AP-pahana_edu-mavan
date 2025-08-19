package com.pahanaedu.constant;

/**
 * System-wide constants used throughout the application
 */
public class SystemConstants {
    
    // Prevent instantiation
    private SystemConstants() {}
    
    // Session attribute names
    public static final String SESSION_USER = "loggedUser";
    public static final String SESSION_USER_ID = "userId";
    public static final String SESSION_USERNAME = "username";
    public static final String SESSION_USER_ROLE = "userRole";
    public static final String SESSION_LOGIN_TIME = "loginTime";
    
    // Request attribute names
    public static final String REQUEST_ERROR_MESSAGE = "errorMessage";
    public static final String REQUEST_SUCCESS_MESSAGE = "successMessage";
    public static final String REQUEST_WARNING_MESSAGE = "warningMessage";
    public static final String REQUEST_INFO_MESSAGE = "infoMessage";
    
    // Page navigation paths
    public static final String PAGE_LOGIN = "/views/auth/login.jsp";
    public static final String PAGE_DASHBOARD = "/views/dashboard.jsp";
    public static final String PAGE_CUSTOMER_LIST = "/views/customer/customer-list.jsp";
    public static final String PAGE_ITEM_LIST = "/views/item/item-list.jsp";
    public static final String PAGE_CREATE_BILL = "/views/billing/create-bill.jsp";
    public static final String PAGE_ERROR_404 = "/error/404.jsp";
    public static final String PAGE_ERROR_500 = "/error/500.jsp";
    public static final String PAGE_ACCESS_DENIED = "/error/access-denied.jsp";
    
    // Servlet URLs
    public static final String URL_LOGIN = "/login";
    public static final String URL_LOGOUT = "/logout";
    public static final String URL_DASHBOARD = "/dashboard";
    public static final String URL_CUSTOMER_BASE = "/customer";
    public static final String URL_ITEM_BASE = "/item";
    public static final String URL_BILL_BASE = "/bill";
    
    // Default values
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int SESSION_TIMEOUT_MINUTES = 30;
    public static final String DEFAULT_CURRENCY = "LKR";
    public static final double DEFAULT_TAX_RATE = 8.0;
    public static final String DEFAULT_COMPANY_NAME = "Pahana Edu Bookshop";
    
    // Bill number settings
    public static final String BILL_NUMBER_PREFIX = "INV";
    public static final int BILL_NUMBER_LENGTH = 6;
    public static final String BILL_NUMBER_FORMAT = BILL_NUMBER_PREFIX + "%06d";
    
    // Account number settings
    public static final String ACCOUNT_NUMBER_PREFIX = "CUST";
    public static final int ACCOUNT_NUMBER_LENGTH = 6;
    public static final String ACCOUNT_NUMBER_FORMAT = ACCOUNT_NUMBER_PREFIX + "%06d";
    
    // Item code settings
    public static final String ITEM_CODE_PREFIX = "ITM";
    public static final int ITEM_CODE_LENGTH = 5;
    public static final String ITEM_CODE_FORMAT = ITEM_CODE_PREFIX + "%05d";
    
    // Status constants
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_DELETED = "DELETED";
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    
    // Action constants
    public static final String ACTION_ADD = "add";
    public static final String ACTION_EDIT = "edit";
    public static final String ACTION_DELETE = "delete";
    public static final String ACTION_VIEW = "view";
    public static final String ACTION_LIST = "list";
    public static final String ACTION_SEARCH = "search";
    public static final String ACTION_SAVE = "save";
    public static final String ACTION_UPDATE = "update";
    
    // Error messages
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid username or password";
    public static final String ERROR_SESSION_EXPIRED = "Your session has expired. Please login again.";
    public static final String ERROR_UNAUTHORIZED_ACCESS = "You are not authorized to access this resource.";
    public static final String ERROR_DATABASE_CONNECTION = "Database connection error. Please try again later.";
    public static final String ERROR_RECORD_NOT_FOUND = "Record not found.";
    public static final String ERROR_DUPLICATE_RECORD = "Record already exists.";
    public static final String ERROR_INVALID_INPUT = "Invalid input. Please check your data.";
    public static final String ERROR_OPERATION_FAILED = "Operation failed. Please try again.";
    
    // Success messages
    public static final String SUCCESS_LOGIN = "Login successful!";
    public static final String SUCCESS_LOGOUT = "Logout successful!";
    public static final String SUCCESS_RECORD_ADDED = "Record added successfully!";
    public static final String SUCCESS_RECORD_UPDATED = "Record updated successfully!";
    public static final String SUCCESS_RECORD_DELETED = "Record deleted successfully!";
    public static final String SUCCESS_OPERATION_COMPLETED = "Operation completed successfully!";
    
    // File upload settings
    public static final String UPLOAD_DIRECTORY = "uploads";
    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB
    public static final String[] ALLOWED_FILE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".pdf", ".doc", ".docx"};
    
    // Pagination constants
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_SIZE = "size";
    public static final String PARAM_SORT = "sort";
    public static final String PARAM_ORDER = "order";
    public static final String ORDER_ASC = "ASC";
    public static final String ORDER_DESC = "DESC";
    
    // Report formats
    public static final String REPORT_FORMAT_PDF = "PDF";
    public static final String REPORT_FORMAT_EXCEL = "EXCEL";
    public static final String REPORT_FORMAT_CSV = "CSV";
    
    // Stock alert levels
    public static final int LOW_STOCK_THRESHOLD = 10;
    public static final int CRITICAL_STOCK_THRESHOLD = 5;
    
    // Regex patterns
    public static final String REGEX_EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String REGEX_PHONE = "^[0-9]{10}$";
    public static final String REGEX_NIC = "^([0-9]{9}[vVxX]|[0-9]{12})$";
    public static final String REGEX_USERNAME = "^[a-zA-Z0-9_]{3,20}$";
}