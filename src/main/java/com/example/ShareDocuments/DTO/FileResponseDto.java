package com.example.ShareDocuments.DTO;

import com.example.ShareDocuments.Entities.File;

import java.util.Set;
import java.util.stream.Collectors;

public record FileResponseDto(
        Long id,
        String path,
        String name,
        String type,
        Long ownerID,
        Set<Long> coworkers
) {

    public static FileResponseDto fromFile(File file) {
        return new FileResponseDto(
                file.getId(),
                file.getPath(),
                file.getName(),
                file.getType(),
                file.getOwner().getId(),
                file.getCoworkers().stream().map(o -> o.getId()).collect(Collectors.toSet())
        );
    }
}
