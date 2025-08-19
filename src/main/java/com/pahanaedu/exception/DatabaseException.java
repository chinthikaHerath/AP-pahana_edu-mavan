package com.pahanaedu.exception;

/**
 * Custom exception class for database-related errors
 */
public class DatabaseException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    private String errorCode;
    private String sqlState;
    
    /**
     * Constructor with message
     */
    public DatabaseException(String message) {
        super(message);
    }
    
    /**
     * Constructor with message and cause
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructor with message, cause, and error code
     */
    public DatabaseException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    /**
     * Constructor with all parameters
     */
    public DatabaseException(String message, Throwable cause, String errorCode, String sqlState) {
        super(message, cause);
        this.errorCode = errorCode;
        this.sqlState = sqlState;
    }
    
    /**
     * Get error code
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Set error code
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    /**
     * Get SQL state
     */
    public String getSqlState() {
        return sqlState;
    }
    
    /**
     * Set SQL state
     */
    public void setSqlState(String sqlState) {
        this.sqlState = sqlState;
    }
    
    /**
     * Get detailed error information
     */
    public String getDetailedMessage() {
        StringBuilder details = new StringBuilder();
        details.append("Database Error: ").append(getMessage());
        
        if (errorCode != null) {
            details.append(" [Error Code: ").append(errorCode).append("]");
        }
        
        if (sqlState != null) {
            details.append(" [SQL State: ").append(sqlState).append("]");
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