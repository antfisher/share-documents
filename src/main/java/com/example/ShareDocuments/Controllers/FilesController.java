package com.example.ShareDocuments.Controllers;

import com.example.ShareDocuments.DTO.CreateFileDto;
import com.example.ShareDocuments.DTO.FileResponseDto;
import com.example.ShareDocuments.DTO.FileUploadResponseDTO;
import com.example.ShareDocuments.DTO.ShareFileDto;
import com.example.ShareDocuments.Entities.File;
import com.example.ShareDocuments.Entities.User;
import com.example.ShareDocuments.Services.FileService;
import com.example.ShareDocuments.Services.PDFService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

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
            String fileID = fileService.saveFileInFileSystem(file);
            return ResponseEntity.status(HttpStatus.OK).body(new FileUploadResponseDTO(fileID, null));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new FileUploadResponseDTO(null, "Failed to upload file: " + e.getMessage()));
        }
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Collection<FileResponseDto> getFiles() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        List<File> files = fileService.getFilesByUserId(user.getId());
        return files.stream().map(FileResponseDto::fromFile).toList();
    }

    @PostMapping(value = "/")
    public ResponseEntity<FileResponseDto> createFile(@RequestBody @Valid CreateFileDto data) {

        FileResponseDto file = fileService.createFile(data);

        return ResponseEntity.ok(file);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<FileResponseDto> getFileById(@PathVariable("id") Long id) {
        File file = fileService.getFileById(id);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(FileResponseDto.fromFile(file));
    }

    @GetMapping(value = "/download/{fileID}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileID) {
        try {
            Resource resource = fileService.loadFile(fileID);
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(value = "/share")
    public ResponseEntity<?> shareFile(@RequestBody @Valid ShareFileDto data) {
        fileService.addCoworkerToFile(data.fileId(), data.email());
        return ResponseEntity.ok("Coworker added successfully");
    }
}
