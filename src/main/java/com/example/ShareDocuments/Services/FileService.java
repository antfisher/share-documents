package com.example.ShareDocuments.Services;

import com.example.ShareDocuments.Config.WorkingDirProvider;
import com.example.ShareDocuments.DTO.CreateFileDto;
import com.example.ShareDocuments.DTO.FileResponseDto;
import com.example.ShareDocuments.Entities.Coworker;
import com.example.ShareDocuments.Entities.File;
import com.example.ShareDocuments.Entities.User;
import com.example.ShareDocuments.Enums.FileAuthority;
import com.example.ShareDocuments.Repositories.FileRepository;
import com.example.ShareDocuments.Repositories.UserRepository;
import jakarta.servlet.ServletContext;
import jakarta.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class FileService {

    private final ServletContext context;
    private final UserService userService;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final WorkingDirProvider workingDirProvider;

    public FileService(UserService userService, UserRepository userRepository,FileRepository fileRepository, ServletContext context, WorkingDirProvider workingDirProvider) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.context = context;
        this.workingDirProvider = workingDirProvider;
    }

    public File getFileById(Long id) {
        return fileRepository.findById(id).orElse(null);
    }

    public String saveFileInFileSystem(MultipartFile file) throws IOException {

        UUID uuid = UUID.randomUUID();
        String fileID = uuid.toString();

        Path filePath = resolvePathById(fileID);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return fileID;
        }
    }

    public Resource loadFile(String fileID) throws IOException {
        Path filePath = this.resolvePathById(fileID);
        return new UrlResource(filePath.toUri());
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

        FileResponseDto fileResponseDto = new FileResponseDto(
                file.getId(),
                file.getPath(),
                file.getName(),
                file.getType(),
                file.getOwner().getId(),
                Collections.emptySet()
        );
        return fileResponseDto;
    }

    @Transactional
    public void deleteFile(Long fileID) throws IOException {
        File file = fileRepository.findById(fileID).orElse(null);

        if (file != null) {
            String filePathId = file.getPath();
            Path filePath = resolvePathById(filePathId);
            try {
                Files.delete(filePath);
                User owner = file.getOwner();
                owner.getFiles().remove(file);
                userRepository.save(owner);

            } catch (IOException e) {
                if (e instanceof NoSuchFileException) {
                    User owner = file.getOwner();
                    owner.getFiles().remove(file);
                    userRepository.save(owner);
                    return;
                }
                throw e;
            }
        }
    }

    public List<File> getFilesByUserId(Long userId) {
        List<File> files = new ArrayList<>();
        files.addAll(fileRepository.findByOwnerId(userId));
        files.addAll(fileRepository.findAllByCoworkerUserId(userId));
        return files;
    }

    @Transactional
    public void addCoworkerToFile(Long fileId, String email) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found with ID: " + fileId));

        if (file.getOwner().getId() == currentUser.getId()) {
            throw new RuntimeException("Can not share to owner");
        }

        User coworkerUser = userService.findUserByLogin(email);

        Coworker coworker = new Coworker();
        coworker.setUserID(coworkerUser.getId());
        coworker.setEmail(coworkerUser.getLogin());
        coworker.setAuthority(FileAuthority.READ);
        coworker.setFile(file);

        file.getCoworkers().add(coworker);
        fileRepository.save(file);
    }

    private Path resolvePathById(String id) throws IOException {
        Path workingDir = workingDirProvider.getWorkingDir();
        Path rootPath = workingDir.resolve("uploads");

        if (!Files.exists(rootPath)) {
            Files.createDirectories(rootPath);
        }

        String filenameWithExtension = id + ".pdf";

        return rootPath.resolve(filenameWithExtension);
    }
}