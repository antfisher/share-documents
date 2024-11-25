package com.example.ShareDocuments.Services;

import com.example.ShareDocuments.DTO.CreateFileDto;
import com.example.ShareDocuments.DTO.FileResponseDto;
import com.example.ShareDocuments.Entities.File;
import com.example.ShareDocuments.Entities.User;
import com.example.ShareDocuments.Repositories.FileRepository;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    private final UserService userService;
    private final FileRepository fileRepository;

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

    public FileResponseDto createFile(CreateFileDto createFileDto) {
        File file = new File();
        file.setName(createFileDto.name());
        file.setType(createFileDto.type());
        file.setPath(createFileDto.path());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        file.setOwner(user);

        fileRepository.save(file);

        FileResponseDto fileResponseDto = new FileResponseDto(file.getId(), file.getPath(), file.getName(), file.getType(), file.getOwner().getId());

        return fileResponseDto;
    }

    public List<File> getFilesByUserId(Long userId) {
        return fileRepository.findByOwnerId(userId);
    }
}