package com.pahanaedu.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for password hashing and verification
 * Uses SHA-256 with salt for secure password storage
 */
public class PasswordUtil {
    
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 16;
    private static final String DELIMITER = ":";
    
    /**
     * Hash a password with a random salt
     * @param password Plain text password
     * @return Hashed password with salt in format: salt:hash
     */
    public static String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        
        try {
            // Generate random salt
            byte[] salt = generateSalt();
            
            // Hash password with salt
            byte[] hash = hashWithSalt(password, salt);
            
            // Encode salt and hash to Base64
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String hashBase64 = Base64.getEncoder().encodeToString(hash);
            
            // Return combined string
            return saltBase64 + DELIMITER + hashBase64;
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password: " + e.getMessage(), e);
        }
    }
    
    /**
     * Verify a password against a hashed password
     * @param password Plain text password to verify
     * @param hashedPassword Hashed password with salt
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) {
            return false;
        }
        
        try {
            // Split the hashed password to get salt and hash
            String[] parts = hashedPassword.split(DELIMITER);
            if (parts.length != 2) {
                // Handle legacy passwords without salt (for migration)
                return hashedPassword.equals(password);
            }
            
            // Decode salt and hash from Base64
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] storedHash = Base64.getDecoder().decode(parts[1]);
            
            // Hash the input password with the same salt
            byte[] inputHash = hashWithSalt(password, salt);
            
            // Compare hashes
            return MessageDigest.isEqual(storedHash, inputHash);
            
        } catch (Exception e) {
            // Log error and return false for security
            System.err.println("Error verifying password: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Generate a random salt
     * @return Random salt bytes
     */
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }
    
    /**
     * Hash password with salt
     * @param password Password to hash
     * @param salt Salt bytes
     * @return Hash bytes
     * @throws NoSuchAlgorithmException if algorithm not found
     */
    private static byte[] hashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        digest.reset();
        digest.update(salt);
        return digest.digest(password.getBytes());
    }
    
    /**
     * Generate a random password
     * @param length Length of password to generate
     * @return Random password
     */
    public static String generateRandomPassword(int length) {
        if (length < 6) {
            throw new IllegalArgumentException("Password length must be at least 6");
        }
        
        String upperChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerChars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "!@#$%^&*";
        String allChars = upperChars + lowerChars + numbers + specialChars;
        
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        
        // Ensure at least one character from each category
        password.append(upperChars.charAt(random.nextInt(upperChars.length())));
        password.append(lowerChars.charAt(random.nextInt(lowerChars.length())));
        password.append(numbers.charAt(random.nextInt(numbers.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));
        
        // Fill the rest randomly
        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        
        // Shuffle the password
        return shuffleString(password.toString());
    }
    
    /**
     * Shuffle a string randomly
     * @param input String to shuffle
     * @return Shuffled string
     */
    private static String shuffleString(String input) {
        char[] chars = input.toCharArray();
        SecureRandom random = new SecureRandom();
        
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        
        return new String(chars);
    }
    
    /**
     * Check password strength
     * @param password Password to check
     * @return Strength level (0-5)
     */
    public static int checkPasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            return 0;
        }
        
        int strength = 0;
        
        // Length check
        if (password.length() >= 8) strength++;
        if (password.length() >= 12) strength++;
        
        // Character variety checks
        if (password.matches(".*[a-z].*")) strength++;
        if (password.matches(".*[A-Z].*")) strength++;
        if (password.matches(".*[0-9].*")) strength++;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) strength++;
        
        return Math.min(strength, 5);
    }
    
    /**
     * Get password strength description
     * @param strength Strength level (0-5)
     * @return Description of strength
     */
    public static String getPasswordStrengthDescription(int strength) {
        switch (strength) {
            case 0:
            case 1:
                return "Very Weak";
            case 2:
                return "Weak";
            case 3:
                return "Fair";
            case 4:
                return "Good";
            case 5:
                return "Strong";
            default:
                return "Unknown";
        }
    }
}