package com.example.ShareDocuments.DTO;

import com.example.ShareDocuments.Enums.UserRole;

public record UserDto(
        Long id,
        String login,
        String password,
        UserRole role) {
}
