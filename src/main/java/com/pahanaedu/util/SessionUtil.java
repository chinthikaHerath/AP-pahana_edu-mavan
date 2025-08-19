package com.pahanaedu.util;

import com.pahanaedu.model.User;
import com.pahanaedu.constant.SystemConstants;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Utility class for session management
 */
public class SessionUtil {
    
    /**
     * Create user session
     * @param session HttpSession
     * @param user User object
     */
    public static void createUserSession(HttpSession session, User user) {
        if (session != null && user != null) {
            session.setAttribute(SystemConstants.SESSION_USER, user);
            session.setAttribute(SystemConstants.SESSION_USER_ID, user.getUserId());
            session.setAttribute(SystemConstants.SESSION_USERNAME, user.getUsername());
            session.setAttribute(SystemConstants.SESSION_USER_ROLE, user.getRole());
            session.setAttribute(SystemConstants.SESSION_LOGIN_TIME, new Date());
            
            // Set session timeout
            session.setMaxInactiveInterval(SystemConstants.SESSION_TIMEOUT_MINUTES * 60);
        }
    }
    
    /**
     * Clear user session
     * @param session HttpSession
     */
    public static void clearUserSession(HttpSession session) {
        if (session != null) {
            session.removeAttribute(SystemConstants.SESSION_USER);
            session.removeAttribute(SystemConstants.SESSION_USER_ID);
            session.removeAttribute(SystemConstants.SESSION_USERNAME);
            session.removeAttribute(SystemConstants.SESSION_USER_ROLE);
            session.removeAttribute(SystemConstants.SESSION_LOGIN_TIME);
        }
    }
    
    /**
     * Get logged in user from session
     * @param session HttpSession
     * @return User object or null
     */
    public static User getLoggedInUser(HttpSession session) {
        if (session != null) {
            return (User) session.getAttribute(SystemConstants.SESSION_USER);
        }
        return null;
    }
    
    /**
     * Get logged in user ID from session
     * @param session HttpSession
     * @return User ID or -1
     */
    public static int getLoggedInUserId(HttpSession session) {
        if (session != null) {
            Integer userId = (Integer) session.getAttribute(SystemConstants.SESSION_USER_ID);
            return userId != null ? userId : -1;
        }
        return -1;
    }
    
    /**
     * Get logged in username from session
     * @param session HttpSession
     * @return Username or null
     */
    public static String getLoggedInUsername(HttpSession session) {
        if (session != null) {
            return (String) session.getAttribute(SystemConstants.SESSION_USERNAME);
        }
        return null;
    }
    
    /**
     * Get user role from session
     * @param session HttpSession
     * @return User role or null
     */
    public static String getUserRole(HttpSession session) {
        if (session != null) {
            return (String) session.getAttribute(SystemConstants.SESSION_USER_ROLE);
        }
        return null;
    }
    
    /**
     * Check if user is logged in
     * @param session HttpSession
     * @return true if logged in
     */
    public static boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute(SystemConstants.SESSION_USER) != null;
    }
    
    /**
     * Check if user has specific role
     * @param session HttpSession
     * @param role Role to check
     * @return true if user has role
     */
    public static boolean hasRole(HttpSession session, String role) {
        String userRole = getUserRole(session);
        return userRole != null && userRole.equalsIgnoreCase(role);
    }
    
    /**
     * Check if user is admin
     * @param session HttpSession
     * @return true if admin
     */
    public static boolean isAdmin(HttpSession session) {
        return hasRole(session, "ADMIN");
    }
    
    /**
     * Check if user is manager
     * @param session HttpSession
     * @return true if manager
     */
    public static boolean isManager(HttpSession session) {
        return hasRole(session, "MANAGER");
    }
    
    /**
     * Check if user is staff
     * @param session HttpSession
     * @return true if staff
     */
    public static boolean isStaff(HttpSession session) {
        return hasRole(session, "STAFF");
    }
    
    /**
     * Get login time from session
     * @param session HttpSession
     * @return Login time or null
     */
    public static Date getLoginTime(HttpSession session) {
        if (session != null) {
            return (Date) session.getAttribute(SystemConstants.SESSION_LOGIN_TIME);
        }
        return null;
    }
    
    /**
     * Calculate session duration in minutes
     * @param session HttpSession
     * @return Duration in minutes or 0
     */
    public static long getSessionDurationMinutes(HttpSession session) {
        Date loginTime = getLoginTime(session);
        if (loginTime != null) {
            long duration = System.currentTimeMillis() - loginTime.getTime();
            return duration / (60 * 1000);
        }
        return 0;
    }
    
    /**
     * Set success message in session
     * @param session HttpSession
     * @param message Success message
     */
    public static void setSuccessMessage(HttpSession session, String message) {
        if (session != null) {
            session.setAttribute(SystemConstants.REQUEST_SUCCESS_MESSAGE, message);
        }
    }
    
    /**
     * Set error message in session
     * @param session HttpSession
     * @param message Error message
     */
    public static void setErrorMessage(HttpSession session, String message) {
        if (session != null) {
            session.setAttribute(SystemConstants.REQUEST_ERROR_MESSAGE, message);
        }
    }
    
    /**
     * Set warning message in session
     * @param session HttpSession
     * @param message Warning message
     */
    public static void setWarningMessage(HttpSession session, String message) {
        if (session != null) {
            session.setAttribute(SystemConstants.REQUEST_WARNING_MESSAGE, message);
        }
    }
    
    /**
     * Set info message in session
     * @param session HttpSession
     * @param message Info message
     */
    public static void setInfoMessage(HttpSession session, String message) {
        if (session != null) {
            session.setAttribute(SystemConstants.REQUEST_INFO_MESSAGE, message);
        }
    }
    
    /**
     * Store original request URL for redirect after login
     * @param request HttpServletRequest
     */
    public static void storeOriginalRequest(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        
        // Get the full URL including query string
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();
        
        if (queryString != null) {
            requestURL.append("?").append(queryString);
        }
        
        session.setAttribute("redirectUrl", requestURL.toString());
    }
    
    /**
     * Get and clear stored redirect URL
     * @param session HttpSession
     * @return Redirect URL or null
     */
    public static String getAndClearRedirectUrl(HttpSession session) {
        if (session != null) {
            String redirectUrl = (String) session.getAttribute("redirectUrl");
            session.removeAttribute("redirectUrl");
            return redirectUrl;
        }
        return null;
    }
    
    /**
     * Invalidate session safely
     * @param session HttpSession
     */
    public static void invalidateSession(HttpSession session) {
        if (session != null) {
            try {
                session.invalidate();
            } catch (IllegalStateException e) {
                // Session already invalidated
            }
        }
    }
}