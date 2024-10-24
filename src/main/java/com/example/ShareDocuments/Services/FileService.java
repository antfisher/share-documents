package com.example.ShareDocuments.Services;

import com.example.ShareDocuments.DTO.CreateFileDto;
import com.example.ShareDocuments.Entities.File;
import com.example.ShareDocuments.Entities.User;
import com.example.ShareDocuments.Repositories.FileRepository;
import com.example.ShareDocuments.Repositories.UserRepository;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.List;

@Service
public class FileService {

    @Autowired
    ServletContext context;

    private UserService userService;
    private FileRepository fileRepository;

    public FileService(UserService userService, FileRepository fileRepository) {
        this.userService = userService;
        this.fileRepository = fileRepository;
    }

    Collection<File> getFilesByUser(String login) {
        User user = userService.findUserByLogin(login);

        return user.getFiles();
    }

    public String saveFileInFileSystem(MultipartFile file) throws IOException {

        Path rootPath = Paths.get(context.getRealPath("uploads"));
        if (!Files.exists(rootPath)) {
            Files.createDirectories(rootPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            String filenameWithExtension = Paths.get(file.getOriginalFilename()).getFileName().toString();
            Path path = rootPath.resolve(filenameWithExtension);
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            return path.toString();
        }
    }


    public File createFile(CreateFileDto createFileDto) {
        File file = new File();
        file.setName(createFileDto.name());
        file.setType(createFileDto.type());
        file.setPath(createFileDto.path());

        fileRepository.save(file);

        return file;
    }
//
//    @GetMapping("/download")
//    public ResponseEntity<Resource> download(String param) throws IOException {
//
//        // ...
//
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentLength(file.length())
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(resource);
//    }
}