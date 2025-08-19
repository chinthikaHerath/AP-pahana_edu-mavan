package com.pahanaedu.dao.interfaces;

import com.pahanaedu.model.User;
import com.pahanaedu.exception.DatabaseException;
import java.util.List;

/**
 * Interface for User Data Access Object
 * Defines all database operations for User entity
 */
public interface UserDAO {
    
    /**
     * Add a new user to the database
     * @param user User object to be added
     * @return Generated user ID
     * @throws DatabaseException if database operation fails
     */
    int addUser(User user) throws DatabaseException;
    
    /**
     * Update an existing user
     * @param user User object with updated information
     * @return true if update successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean updateUser(User user) throws DatabaseException;
    
    /**
     * Delete a user by ID (soft delete - sets isActive to false)
     * @param userId ID of the user to delete
     * @return true if deletion successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean deleteUser(int userId) throws DatabaseException;
    
    /**
     * Get a user by ID
     * @param userId ID of the user to retrieve
     * @return User object if found, null otherwise
     * @throws DatabaseException if database operation fails
     */
    User getUserById(int userId) throws DatabaseException;
    
    /**
     * Get a user by username
     * @param username Username to search for
     * @return User object if found, null otherwise
     * @throws DatabaseException if database operation fails
     */
    User getUserByUsername(String username) throws DatabaseException;
    
    /**
     * Get a user by email
     * @param email Email to search for
     * @return User object if found, null otherwise
     * @throws DatabaseException if database operation fails
     */
    User getUserByEmail(String email) throws DatabaseException;
    
    /**
     * Get all users
     * @return List of all users
     * @throws DatabaseException if database operation fails
     */
    List<User> getAllUsers() throws DatabaseException;
    
    /**
     * Get all active users
     * @return List of active users
     * @throws DatabaseException if database operation fails
     */
    List<User> getActiveUsers() throws DatabaseException;
    
    /**
     * Get users by role
     * @param role Role to filter by
     * @return List of users with specified role
     * @throws DatabaseException if database operation fails
     */
    List<User> getUsersByRole(String role) throws DatabaseException;
    
    /**
     * Authenticate user with username and password
     * @param username Username
     * @param password Password (plain text - will be hashed for comparison)
     * @return User object if authentication successful, null otherwise
     * @throws DatabaseException if database operation fails
     */
    User authenticateUser(String username, String password) throws DatabaseException;
    
    /**
     * Update user's last login timestamp
     * @param userId ID of the user
     * @return true if update successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean updateLastLogin(int userId) throws DatabaseException;
    
    /**
     * Change user password
     * @param userId ID of the user
     * @param newPassword New password (will be hashed before storing)
     * @return true if update successful, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean changePassword(int userId, String newPassword) throws DatabaseException;
    
    /**
     * Check if username exists
     * @param username Username to check
     * @return true if username exists, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean isUsernameExists(String username) throws DatabaseException;
    
    /**
     * Check if email exists
     * @param email Email to check
     * @return true if email exists, false otherwise
     * @throws DatabaseException if database operation fails
     */
    boolean isEmailExists(String email) throws DatabaseException;
    
    /**
     * Search users by name or username
     * @param searchTerm Search term
     * @return List of matching users
     * @throws DatabaseException if database operation fails
     */
    List<User> searchUsers(String searchTerm) throws DatabaseException;
    
    /**
     * Get total user count
     * @return Total number of users
     * @throws DatabaseException if database operation fails
     */
    int getTotalUserCount() throws DatabaseException;
    
    /**
     * Get active user count
     * @return Number of active users
     * @throws DatabaseException if database operation fails
     */
    int getActiveUserCount() throws DatabaseException;
}