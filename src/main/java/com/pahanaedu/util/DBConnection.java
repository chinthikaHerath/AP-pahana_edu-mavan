package com.pahanaedu.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Factory class for database connection management
 * This class creates new database connections using the Factory pattern
 */
public class DBConnection {
    
    // Database configuration constants
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pahana_edu_db?useSSL=false&serverTimezone=UTC";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "977901901Vc@#";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Static block to load driver once when class is first loaded
    static {
        try {
            // Load MySQL JDBC driver
            Class.forName(DB_DRIVER);
            System.out.println("MySQL JDBC Driver loaded successfully!");
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Database Driver not found: " + ex.getMessage(), ex);
        }
    }
    
    /**
     * Private constructor to prevent instantiation
     * This class only provides static methods
     */
    private DBConnection() {
        // Private constructor - no instances needed
    }
    
    /**
     * Create and return a new database connection
     * @return New Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            return connection;
        } catch (SQLException ex) {
            throw new SQLException("Failed to connect to database: " + ex.getMessage(), ex);
        }
    }
    
    /**
     * Check if a connection is valid
     * @param connection Connection to check
     * @return true if connection is valid, false otherwise
     */
    public static boolean isConnectionValid(Connection connection) {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Close a connection safely
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}