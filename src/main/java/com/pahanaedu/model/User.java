package com.pahanaedu.model;

import java.io.Serializable;
import java.util.Date;

/**
 * User model class representing the users table
 */
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int userId;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String role;
    private boolean isActive;
    private Date lastLogin;
    private Date createdAt;
    private Date updatedAt;
    
    // Default constructor
    public User() {
        this.isActive = true;
    }
    
    // Constructor with required fields
    public User(String username, String password, String email, String fullName, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.isActive = true;
    }
    
    // Full constructor
    public User(int userId, String username, String password, String email, 
                String fullName, String role, boolean isActive, Date lastLogin, 
                Date createdAt, Date updatedAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.isActive = isActive;
        this.lastLogin = lastLogin;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public Date getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Utility methods
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.role);
    }
    
    public boolean isManager() {
        return "MANAGER".equalsIgnoreCase(this.role);
    }
    
    public boolean isStaff() {
        return "STAFF".equalsIgnoreCase(this.role);
    }
    
    public boolean hasRole(String role) {
        return this.role != null && this.role.equalsIgnoreCase(role);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                ", isActive=" + isActive +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        User user = (User) o;
        
        if (userId != user.userId) return false;
        return username != null ? username.equals(user.username) : user.username == null;
    }
    
    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}