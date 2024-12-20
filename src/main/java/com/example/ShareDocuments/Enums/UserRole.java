package com.example.ShareDocuments.Enums;

public enum UserRole {

    ADMIN("admin"),
    USER("user");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getValue() {
        return role;
    }

    public boolean isAdmin() {
        return role.equals("admin");
    }
}
