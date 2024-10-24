package com.example.ShareDocuments.DTO;

import com.example.ShareDocuments.Enums.UserRole;

public record SignUpDto(
        String login,
        String password,
        UserRole role) {
}
