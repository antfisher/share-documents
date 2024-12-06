package com.example.ShareDocuments.DTO;

import com.example.ShareDocuments.Enums.UserRole;

import java.util.Set;

public record UserDto(
        Long id,
        String login,
        String firstName,
        String lastName,
        UserRole role) {
}
