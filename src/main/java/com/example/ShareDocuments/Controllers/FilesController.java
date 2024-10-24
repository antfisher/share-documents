package com.example.ShareDocuments.Controllers;

import com.example.ShareDocuments.DTO.CreateFileDto;
import com.example.ShareDocuments.DTO.FileUploadResponseDTO;
import com.example.ShareDocuments.Entities.File;
import com.example.ShareDocuments.Entities.User;
import com.example.ShareDocuments.Services.FileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/api/v1/files")
public class FilesController {

    FileService fileService;

    public FilesController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<FileUploadResponseDTO> uploadFile(@RequestParam("file") MultipartFile file) {

        try {
            String filePath = fileService.saveFileInFileSystem(file);
            return ResponseEntity.status(HttpStatus.OK).body(new FileUploadResponseDTO(filePath, null));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new FileUploadResponseDTO(null, "Failed to upload file: " + e.getMessage()));
        }
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Collection<File> getFiles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return user.getFiles();
    }

    @PostMapping(value = "/")
    public ResponseEntity<File> createFile(@RequestBody @Valid CreateFileDto data) {

        File file = fileService.createFile(data);

        return ResponseEntity.ok(file);
    }
}
