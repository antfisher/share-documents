package com.example.ShareDocuments.DTO;

public record CoworkerDto(
        Long id,
        String userEmail,
        Long userID,
        String fileAuthority,
        Long fileID
) {
}
