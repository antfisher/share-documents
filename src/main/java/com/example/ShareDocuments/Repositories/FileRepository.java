package com.example.ShareDocuments.Repositories;

import com.example.ShareDocuments.Entities.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
   Optional<File> findById(Long id);
}