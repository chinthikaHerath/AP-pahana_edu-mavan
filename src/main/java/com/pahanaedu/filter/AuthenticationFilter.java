package com.pahanaedu.filter;

import com.pahanaedu.util.SessionUtil;
import com.pahanaedu.constant.SystemConstants;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Filter to check if user is authenticated
 * Applies to all URLs except login and static resources
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {
    
    private List<String> excludedUrls;
    private List<String> excludedExtensions;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // URLs that don't require authentication
        excludedUrls = new ArrayList<>();
        excludedUrls.add("/login");
        excludedUrls.add("/logout");
        excludedUrls.add("/register");
        excludedUrls.add("/forgot-password");
        excludedUrls.add("/reset-password");
        excludedUrls.add("/");
        excludedUrls.add("/index.jsp");
        
        // File extensions that don't require authentication
        excludedExtensions = new ArrayList<>();
        excludedExtensions.add(".css");
        excludedExtensions.add(".js");
        excludedExtensions.add(".jpg");
        excludedExtensions.add(".jpeg");
        excludedExtensions.add(".png");
        excludedExtensions.add(".gif");
        excludedExtensions.add(".ico");
        excludedExtensions.add(".woff");
        excludedExtensions.add(".woff2");
        excludedExtensions.add(".ttf");
        excludedExtensions.add(".eot");
        excludedExtensions.add(".svg");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        
        // Check if the request should be excluded from authentication
        if (isExcluded(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Check if user is logged in
        HttpSession session = httpRequest.getSession(false);
        if (SessionUtil.isLoggedIn(session)) {
            // User is logged in, continue with request
            chain.doFilter(request, response);
        } else {
            // User is not logged in
            
            // Store the original request for redirect after login
            if (!path.equals("/") && !path.equals("/index.jsp")) {
                SessionUtil.storeOriginalRequest(httpRequest);
            }
            
            // Set error message
            if (session == null) {
                session = httpRequest.getSession(true);
            }
            SessionUtil.setErrorMessage(session, SystemConstants.ERROR_SESSION_EXPIRED);
            
            // Redirect to login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + SystemConstants.URL_LOGIN);
        }
    }
    
    @Override
    public void destroy() {
        excludedUrls = null;
        excludedExtensions = null;
    }
    
    /**
     * Check if the URL should be excluded from authentication
     * @param path Request path
     * @return true if excluded
     */
    private boolean isExcluded(String path) {
        // Check if it's a static resource
        for (String extension : excludedExtensions) {
            if (path.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        
        // Check if it's in the assets folder
        if (path.startsWith("/assets/")) {
            return true;
        }
        
        // Check if it's an excluded URL
        for (String excludedUrl : excludedUrls) {
            if (path.equals(excludedUrl) || path.startsWith(excludedUrl + "/")) {
                return true;
            }
        }
        
        return false;
    }
}