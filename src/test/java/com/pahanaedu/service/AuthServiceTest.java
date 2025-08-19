package com.pahanaedu.service;

import com.pahanaedu.service.impl.AuthServiceImpl;
import com.pahanaedu.service.interfaces.AuthService;
import com.pahanaedu.model.User;
import com.pahanaedu.testdata.TestDataProvider;
import com.pahanaedu.exception.AuthenticationException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {
    
    private AuthService authService;
    
    @BeforeEach
    public void setUp() {
        authService = new AuthServiceImpl();
    }
    
    @Test
    @DisplayName("Test successful login with valid credentials")
    public void testSuccessfulLogin() {
        try {
            User user = authService.login("admin", "admin123");
            assertNotNull(user);
            assertEquals("admin", user.getUsername());
            assertEquals("ADMIN", user.getRole().toString());
        } catch (Exception e) {
            fail("Login should succeed with valid credentials");
        }
    }
    
    @Test
    @DisplayName("Test failed login with invalid username")
    public void testLoginInvalidUsername() {
        assertThrows(AuthenticationException.class, () -> {
            authService.login("nonexistent", "password");
        });
    }
    
    @Test
    @DisplayName("Test failed login with invalid password")
    public void testLoginInvalidPassword() {
        assertThrows(AuthenticationException.class, () -> {
            authService.login("admin", "wrongpassword");
        });
    }
    
    @Test
    @DisplayName("Test login with empty credentials")
    public void testLoginEmptyCredentials() {
        assertThrows(AuthenticationException.class, () -> {
            authService.login("", "");
        });
    }
    
    @Test
    @DisplayName("Test login with null credentials")
    public void testLoginNullCredentials() {
        assertThrows(AuthenticationException.class, () -> {
            authService.login(null, null);
        });
    }
    
    @Test
    @DisplayName("Test inactive user login")
    public void testInactiveUserLogin() {
        // Assuming there's an inactive user in the test database
        assertThrows(AuthenticationException.class, () -> {
            authService.login("inactive_user", "password");
        });
    }
}