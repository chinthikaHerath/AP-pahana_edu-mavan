package com.pahanaedu.service.impl;

import com.pahanaedu.service.interfaces.AuthService;
import com.pahanaedu.dao.interfaces.UserDAO;
import com.pahanaedu.dao.impl.UserDAOImpl;
import com.pahanaedu.model.User;
import com.pahanaedu.exception.AuthenticationException;
import com.pahanaedu.exception.ValidationException;
import com.pahanaedu.exception.DatabaseException;
import com.pahanaedu.util.ValidationUtil;
import com.pahanaedu.util.PasswordUtil;
import com.pahanaedu.constant.SystemConstants;
import com.pahanaedu.constant.UserRole;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Implementation of AuthService interface
 * Handles user authentication and session management
 */
public class AuthServiceImpl implements AuthService {
    
    private UserDAO userDAO;
    
    public AuthServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }
    
    @Override
    public User login(String username, String password) throws AuthenticationException, DatabaseException {
        // Validate input
        if (ValidationUtil.isNullOrEmpty(username) || ValidationUtil.isNullOrEmpty(password)) {
            throw new AuthenticationException(AuthenticationException.AuthErrorType.INVALID_CREDENTIALS);
        }
        
        // Authenticate user
        User user = userDAO.authenticateUser(username, password);
        
        if (user == null) {
            throw new AuthenticationException(AuthenticationException.AuthErrorType.INVALID_CREDENTIALS, username);
        }
        
        if (!user.isActive()) {
            throw new AuthenticationException(AuthenticationException.AuthErrorType.ACCOUNT_DISABLED, username);
        }
        
        // Update last login
        userDAO.updateLastLogin(user.getUserId());
        user.setLastLogin(new Date());
        
        return user;
    }
    
    @Override
    public User login(String username, String password, HttpSession session) 
            throws AuthenticationException, DatabaseException {
        
        User user = login(username, password);
        
        // Store user information in session
        session.setAttribute(SystemConstants.SESSION_USER, user);
        session.setAttribute(SystemConstants.SESSION_USER_ID, user.getUserId());
        session.setAttribute(SystemConstants.SESSION_USERNAME, user.getUsername());
        session.setAttribute(SystemConstants.SESSION_USER_ROLE, user.getRole());
        session.setAttribute(SystemConstants.SESSION_LOGIN_TIME, new Date());
        
        // Set session timeout
        session.setMaxInactiveInterval(SystemConstants.SESSION_TIMEOUT_MINUTES * 60);
        
        return user;
    }
    
    @Override
    public void logout(HttpSession session) {
        if (session != null) {
            // Clear session attributes
            session.removeAttribute(SystemConstants.SESSION_USER);
            session.removeAttribute(SystemConstants.SESSION_USER_ID);
            session.removeAttribute(SystemConstants.SESSION_USERNAME);
            session.removeAttribute(SystemConstants.SESSION_USER_ROLE);
            session.removeAttribute(SystemConstants.SESSION_LOGIN_TIME);
            
            // Invalidate session
            session.invalidate();
        }
    }
    
    @Override
    public int register(User user) throws ValidationException, DatabaseException {
        // Validate user data
        validateUserRegistration(user);
        
        // Check if username already exists
        if (!isUsernameAvailable(user.getUsername())) {
            ValidationException ve = new ValidationException("username", "Username already exists");
            throw ve;
        }
        
        // Check if email already exists
        if (!isEmailAvailable(user.getEmail())) {
            ValidationException ve = new ValidationException("email", "Email already exists");
            throw ve;
        }
        
        // Add user to database
        return userDAO.addUser(user);
    }
    
    @Override
    public boolean changePassword(int userId, String currentPassword, String newPassword) 
            throws AuthenticationException, ValidationException, DatabaseException {
        
        // Validate new password
        if (!ValidationUtil.isValidPassword(newPassword)) {
            throw new ValidationException("newPassword", 
                "Password must be at least 6 characters with uppercase, lowercase, and digit");
        }
        
        // Get user
        User user = userDAO.getUserById(userId);
        if (user == null) {
            throw new AuthenticationException("User not found");
        }
        
        // Verify current password
        if (!PasswordUtil.verifyPassword(currentPassword, user.getPassword())) {
            throw new AuthenticationException(AuthenticationException.AuthErrorType.INVALID_CREDENTIALS);
        }
        
        // Change password
        return userDAO.changePassword(userId, newPassword);
    }
    
    @Override
    public boolean resetPassword(int userId, String newPassword) 
            throws ValidationException, DatabaseException {
        
        // Validate new password
        if (!ValidationUtil.isValidPassword(newPassword)) {
            throw new ValidationException("newPassword", 
                "Password must be at least 6 characters with uppercase, lowercase, and digit");
        }
        
        // Reset password
        return userDAO.changePassword(userId, newPassword);
    }
    
    @Override
    public boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute(SystemConstants.SESSION_USER) != null;
    }
    
    @Override
    public User getLoggedInUser(HttpSession session) {
        if (session != null) {
            return (User) session.getAttribute(SystemConstants.SESSION_USER);
        }
        return null;
    }
    
    @Override
    public boolean hasRole(HttpSession session, String role) {
        User user = getLoggedInUser(session);
        return user != null && user.hasRole(role);
    }
    
    @Override
    public boolean isAdmin(HttpSession session) {
        return hasRole(session, UserRole.ADMIN.name());
    }
    
    @Override
    public boolean isManager(HttpSession session) {
        return hasRole(session, UserRole.MANAGER.name());
    }
    
    @Override
    public boolean isStaff(HttpSession session) {
        return hasRole(session, UserRole.STAFF.name());
    }
    
    @Override
    public void updateUserSession(HttpSession session, User user) {
        if (session != null && user != null) {
            session.setAttribute(SystemConstants.SESSION_USER, user);
            session.setAttribute(SystemConstants.SESSION_USER_ID, user.getUserId());
            session.setAttribute(SystemConstants.SESSION_USERNAME, user.getUsername());
            session.setAttribute(SystemConstants.SESSION_USER_ROLE, user.getRole());
        }
    }
    
    @Override
    public void validateUserRegistration(User user) throws ValidationException {
        ValidationException validationException = new ValidationException("User validation failed");
        
        // Validate username
        if (ValidationUtil.isNullOrEmpty(user.getUsername())) {
            validationException.addFieldError("username", "Username is required");
        } else if (!ValidationUtil.isValidUsername(user.getUsername())) {
            validationException.addFieldError("username", 
                "Username must be 3-20 characters with only letters, numbers, and underscore");
        }
        
        // Validate password
        if (ValidationUtil.isNullOrEmpty(user.getPassword())) {
            validationException.addFieldError("password", "Password is required");
        } else if (!ValidationUtil.isValidPassword(user.getPassword())) {
            validationException.addFieldError("password", 
                "Password must be at least 6 characters with uppercase, lowercase, and digit");
        }
        
        // Validate email
        if (ValidationUtil.isNullOrEmpty(user.getEmail())) {
            validationException.addFieldError("email", "Email is required");
        } else if (!ValidationUtil.isValidEmail(user.getEmail())) {
            validationException.addFieldError("email", "Invalid email format");
        }
        
        // Validate full name
        if (ValidationUtil.isNullOrEmpty(user.getFullName())) {
            validationException.addFieldError("fullName", "Full name is required");
        } else if (!ValidationUtil.isValidLength(user.getFullName(), 3, 100)) {
            validationException.addFieldError("fullName", "Full name must be 3-100 characters");
        }
        
        // Validate role
        if (ValidationUtil.isNullOrEmpty(user.getRole())) {
            validationException.addFieldError("role", "Role is required");
        } else {
            try {
                UserRole.valueOf(user.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                validationException.addFieldError("role", "Invalid role");
            }
        }
        
        // Throw exception if there are validation errors
        if (validationException.hasFieldErrors()) {
            throw validationException;
        }
    }
    
    @Override
    public boolean isUsernameAvailable(String username) throws DatabaseException {
        return !userDAO.isUsernameExists(username);
    }
    
    @Override
    public boolean isEmailAvailable(String email) throws DatabaseException {
        return !userDAO.isEmailExists(email);
    }
}