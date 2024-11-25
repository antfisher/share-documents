package com.example.ShareDocuments.DTO;

import com.example.ShareDocuments.Entities.File;

public record FileResponseDto(
        Long id,
        String path,
        String name,
        String type,
        Long ownerID
) {

    public static FileResponseDto fromFile(File file) {
        return new FileResponseDto(file.getId(), file.getPath(), file.getName(), file.getType(), file.getOwner().getId());
    }
}
