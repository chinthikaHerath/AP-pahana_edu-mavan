package com.pahanaedu.constant;

/**
 * Enumeration for user roles in the system
 */
public enum UserRole {
    
    ADMIN("Administrator", "Full system access"),
    MANAGER("Manager", "Management functions and reports"),
    STAFF("Staff", "Basic operations and billing");
    
    private final String displayName;
    private final String description;
    
    /**
     * Constructor
     * @param displayName Display name for the role
     * @param description Description of the role
     */
    UserRole(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    /**
     * Get display name
     * @return Display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Get description
     * @return Description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Check if this role has higher or equal authority than another role
     * @param other Other role to compare
     * @return true if this role has higher or equal authority
     */
    public boolean hasAuthorityOver(UserRole other) {
        if (this == ADMIN) {
            return true; // Admin has authority over all
        }
        if (this == MANAGER) {
            return other != ADMIN; // Manager has authority over all except Admin
        }
        if (this == STAFF) {
            return other == STAFF; // Staff only has authority over other Staff
        }
        return false;
    }
    
    /**
     * Get role by name (case-insensitive)
     * @param name Role name
     * @return UserRole or null if not found
     */
    public static UserRole fromString(String name) {
        if (name == null) {
            return null;
        }
        
        for (UserRole role : UserRole.values()) {
            if (role.name().equalsIgnoreCase(name)) {
                return role;
            }
        }
        return null;
    }
    
    /**
     * Check if role name is valid
     * @param name Role name to check
     * @return true if valid
     */
    public static boolean isValidRole(String name) {
        return fromString(name) != null;
    }
    
    /**
     * Get all role names as array
     * @return Array of role names
     */
    public static String[] getRoleNames() {
        UserRole[] roles = values();
        String[] names = new String[roles.length];
        for (int i = 0; i < roles.length; i++) {
            names[i] = roles[i].name();
        }
        return names;
    }
    
    /**
     * Get all roles as display options
     * @return Array of display options
     */
    public static String[][] getRoleOptions() {
        UserRole[] roles = values();
        String[][] options = new String[roles.length][2];
        for (int i = 0; i < roles.length; i++) {
            options[i][0] = roles[i].name();
            options[i][1] = roles[i].getDisplayName();
        }
        return options;
    }
}