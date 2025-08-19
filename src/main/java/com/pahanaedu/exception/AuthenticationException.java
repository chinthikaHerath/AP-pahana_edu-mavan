package com.pahanaedu.exception;

/**
 * Custom exception class for authentication-related errors
 */
public class AuthenticationException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    private String username;
    private AuthErrorType errorType;
    private int failedAttempts;
    
    /**
     * Enum for authentication error types
     */
    public enum AuthErrorType {
        INVALID_CREDENTIALS("Invalid username or password"),
        ACCOUNT_LOCKED("Account is locked due to multiple failed attempts"),
        ACCOUNT_DISABLED("Account has been disabled"),
        SESSION_EXPIRED("Session has expired"),
        UNAUTHORIZED_ACCESS("You are not authorized to access this resource"),
        TOKEN_INVALID("Authentication token is invalid"),
        TOKEN_EXPIRED("Authentication token has expired");
        
        private final String message;
        
        AuthErrorType(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
    }
    
    /**
     * Constructor with message
     */
    public AuthenticationException(String message) {
        super(message);
    }
    
    /**
     * Constructor with error type
     */
    public AuthenticationException(AuthErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
    
    /**
     * Constructor with message and cause
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructor with error type and username
     */
    public AuthenticationException(AuthErrorType errorType, String username) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.username = username;
    }
    
    /**
     * Constructor with all parameters
     */
    public AuthenticationException(AuthErrorType errorType, String username, int failedAttempts) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.username = username;
        this.failedAttempts = failedAttempts;
    }
    
    /**
     * Get username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Set username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Get error type
     */
    public AuthErrorType getErrorType() {
        return errorType;
    }
    
    /**
     * Set error type
     */
    public void setErrorType(AuthErrorType errorType) {
        this.errorType = errorType;
    }
    
    /**
     * Get failed attempts
     */
    public int getFailedAttempts() {
        return failedAttempts;
    }
    
    /**
     * Set failed attempts
     */
    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }
    
    /**
     * Check if error is due to invalid credentials
     */
    public boolean isInvalidCredentials() {
        return errorType == AuthErrorType.INVALID_CREDENTIALS;
    }
    
    /**
     * Check if account is locked
     */
    public boolean isAccountLocked() {
        return errorType == AuthErrorType.ACCOUNT_LOCKED;
    }
    
    /**
     * Check if account is disabled
     */
    public boolean isAccountDisabled() {
        return errorType == AuthErrorType.ACCOUNT_DISABLED;
    }
    
    /**
     * Check if session expired
     */
    public boolean isSessionExpired() {
        return errorType == AuthErrorType.SESSION_EXPIRED;
    }
    
    /**
     * Check if unauthorized access
     */
    public boolean isUnauthorizedAccess() {
        return errorType == AuthErrorType.UNAUTHORIZED_ACCESS;
    }
    
    /**
     * Get detailed error message
     */
    public String getDetailedMessage() {
        StringBuilder details = new StringBuilder();
        
        if (errorType != null) {
            details.append(errorType.getMessage());
        } else {
            details.append(getMessage());
        }
        
        if (username != null) {
            details.append(" [Username: ").append(username).append("]");
        }
        
        if (failedAttempts > 0) {
            details.append(" [Failed Attempts: ").append(failedAttempts).append("]");
        }
        
        if (getCause() != null) {
            details.append(" [Cause: ").append(getCause().getMessage()).append("]");
        }
        
        return details.toString();
    }
    
    @Override
    public String toString() {
        return getDetailedMessage();
    }
}