package com.pahanaedu.service.interfaces;

import com.pahanaedu.model.User;
import com.pahanaedu.exception.AuthenticationException;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.exception.DatabaseException;
import javax.servlet.http.HttpSession;

/**
 * Interface for Authentication Service
 * Handles user authentication and session management
 */
public interface AuthService {
    
    /**
     * Authenticate user with username and password
     * @param username Username
     * @param password Password
     * @return Authenticated User object
     * @throws AuthenticationException if authentication fails
     * @throws DatabaseException if database operation fails
     */
    User login(String username, String password) throws AuthenticationException, DatabaseException;
    
    /**
     * Authenticate user and create session
     * @param username Username
     * @param password Password
     * @param session HttpSession to store user information
     * @return Authenticated User object
     * @throws AuthenticationException if authentication fails
     * @throws DatabaseException if database operation fails
     */
    User login(String username, String password, HttpSession session) throws AuthenticationException, DatabaseException;
    
    /**
     * Logout user and invalidate session
     * @param session HttpSession to invalidate
     */
    void logout(HttpSession session);
    
    /**
     * Register a new user
     * @param user User object with registration details
     * @return Created user ID
     * @throws ValidationException if validation fails
     * @throws DatabaseException if database operation fails
     */
    int register(User user) throws ValidationException, DatabaseException;
    
    /**
     * Change user password
     * @param userId User ID
     * @param currentPassword Current password for verification
     * @param newPassword New password
     * @return true if password changed successfully
     * @throws AuthenticationException if current password is incorrect
     * @throws ValidationException if new password is invalid
     * @throws DatabaseException if database operation fails
     */
    boolean changePassword(int userId, String currentPassword, String newPassword) 
            throws AuthenticationException, ValidationException, DatabaseException;
    
    /**
     * Reset user password (admin function)
     * @param userId User ID
     * @param newPassword New password
     * @return true if password reset successfully
     * @throws ValidationException if new password is invalid
     * @throws DatabaseException if database operation fails
     */
    boolean resetPassword(int userId, String newPassword) 
            throws ValidationException, DatabaseException;
    
    /**
     * Check if user is logged in
     * @param session HttpSession to check
     * @return true if user is logged in
     */
    boolean isLoggedIn(HttpSession session);
    
    /**
     * Get logged in user from session
     * @param session HttpSession containing user information
     * @return User object if logged in, null otherwise
     */
    User getLoggedInUser(HttpSession session);
    
    /**
     * Check if user has specific role
     * @param session HttpSession containing user information
     * @param role Role to check
     * @return true if user has the specified role
     */
    boolean hasRole(HttpSession session, String role);
    
    /**
     * Check if user is admin
     * @param session HttpSession containing user information
     * @return true if user is admin
     */
    boolean isAdmin(HttpSession session);
    
    /**
     * Check if user is manager
     * @param session HttpSession containing user information
     * @return true if user is manager
     */
    boolean isManager(HttpSession session);
    
    /**
     * Check if user is staff
     * @param session HttpSession containing user information
     * @return true if user is staff
     */
    boolean isStaff(HttpSession session);
    
    /**
     * Update user session with latest user information
     * @param session HttpSession to update
     * @param user Updated user object
     */
    void updateUserSession(HttpSession session, User user);
    
    /**
     * Validate user registration data
     * @param user User object to validate
     * @throws ValidationException if validation fails
     */
    void validateUserRegistration(User user) throws ValidationException;
    
    /**
     * Check if username is available
     * @param username Username to check
     * @return true if username is available
     * @throws DatabaseException if database operation fails
     */
    boolean isUsernameAvailable(String username) throws DatabaseException;
    
    /**
     * Check if email is available
     * @param email Email to check
     * @return true if email is available
     * @throws DatabaseException if database operation fails
     */
    boolean isEmailAvailable(String email) throws DatabaseException;
}