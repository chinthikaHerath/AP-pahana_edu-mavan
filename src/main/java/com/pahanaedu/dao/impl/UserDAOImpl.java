package com.pahanaedu.dao.impl;

import com.pahanaedu.dao.interfaces.UserDAO;
import com.pahanaedu.model.User;
import com.pahanaedu.exception.DatabaseException;
import com.pahanaedu.util.DBConnection;
import com.pahanaedu.util.DateUtil;
import com.pahanaedu.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of UserDAO interface
 * Handles all database operations for User entity
 */
public class UserDAOImpl implements UserDAO {
    
    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection();
    }
    
    @Override
    public int addUser(User user) throws DatabaseException {
        String sql = "INSERT INTO users (username, password, email, full_name, role, is_active) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, PasswordUtil.hashPassword(user.getPassword()));
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getFullName());
            pstmt.setString(5, user.getRole());
            pstmt.setBoolean(6, user.isActive());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Creating user failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new DatabaseException("Creating user failed, no ID obtained.");
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error adding user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean updateUser(User user) throws DatabaseException {
        String sql = "UPDATE users SET email = ?, full_name = ?, role = ?, is_active = ?, " +
                    "updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getFullName());
            pstmt.setString(3, user.getRole());
            pstmt.setBoolean(4, user.isActive());
            pstmt.setInt(5, user.getUserId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error updating user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteUser(int userId) throws DatabaseException {
        String sql = "UPDATE users SET is_active = false WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public User getUserById(int userId) throws DatabaseException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting user by ID: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public User getUserByUsername(String username) throws DatabaseException {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting user by username: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public User getUserByEmail(String email) throws DatabaseException {
        String sql = "SELECT * FROM users WHERE email = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting user by email: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    @Override
    public List<User> getAllUsers() throws DatabaseException {
        String sql = "SELECT * FROM users ORDER BY full_name";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting all users: " + e.getMessage(), e);
        }
        
        return users;
    }
    
    @Override
    public List<User> getActiveUsers() throws DatabaseException {
        String sql = "SELECT * FROM users WHERE is_active = true ORDER BY full_name";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting active users: " + e.getMessage(), e);
        }
        
        return users;
    }
    
    @Override
    public List<User> getUsersByRole(String role) throws DatabaseException {
        String sql = "SELECT * FROM users WHERE role = ? ORDER BY full_name";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, role);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(extractUserFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting users by role: " + e.getMessage(), e);
        }
        
        return users;
    }
    
    @Override
    public User authenticateUser(String username, String password) throws DatabaseException {
        User user = getUserByUsername(username);
        
        if (user != null && user.isActive() && PasswordUtil.verifyPassword(password, user.getPassword())) {
            return user;
        }
        
        return null;
    }
    
    @Override
    public boolean updateLastLogin(int userId) throws DatabaseException {
        String sql = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error updating last login: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean changePassword(int userId, String newPassword) throws DatabaseException {
        String sql = "UPDATE users SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, PasswordUtil.hashPassword(newPassword));
            pstmt.setInt(2, userId);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error changing password: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean isUsernameExists(String username) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error checking username existence: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public boolean isEmailExists(String email) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error checking email existence: " + e.getMessage(), e);
        }
        
        return false;
    }
    
    @Override
    public List<User> searchUsers(String searchTerm) throws DatabaseException {
        String sql = "SELECT * FROM users WHERE username LIKE ? OR full_name LIKE ? OR email LIKE ? " +
                    "ORDER BY full_name";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(extractUserFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error searching users: " + e.getMessage(), e);
        }
        
        return users;
    }
    
    @Override
    public int getTotalUserCount() throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM users";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting total user count: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    @Override
    public int getActiveUserCount() throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM users WHERE is_active = true";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting active user count: " + e.getMessage(), e);
        }
        
        return 0;
    }
    
    /**
     * Extract User object from ResultSet
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setFullName(rs.getString("full_name"));
        user.setRole(rs.getString("role"));
        user.setActive(rs.getBoolean("is_active"));
        
        Timestamp lastLogin = rs.getTimestamp("last_login");
        if (lastLogin != null) {
            user.setLastLogin(new Date(lastLogin.getTime()));
        }
        
        user.setCreatedAt(new Date(rs.getTimestamp("created_at").getTime()));
        user.setUpdatedAt(new Date(rs.getTimestamp("updated_at").getTime()));
        
        return user;
    }
}