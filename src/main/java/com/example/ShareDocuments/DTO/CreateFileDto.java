package com.example.ShareDocuments.DTO;

public record CreateFileDto(
        String name,
        String path,
        String type
) {
}
