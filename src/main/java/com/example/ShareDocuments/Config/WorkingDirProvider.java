package com.example.ShareDocuments.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class WorkingDirProvider {

    @Value("${app.working-dir}")
    private String workingDir;

    public Path getWorkingDir() {
        return Paths.get(workingDir);
    }
}