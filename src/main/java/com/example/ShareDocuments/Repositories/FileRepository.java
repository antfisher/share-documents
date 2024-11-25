package com.example.ShareDocuments.Repositories;

import com.example.ShareDocuments.Entities.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
   @Override
   Optional<File> findById(Long id);
   List<File> findByOwnerId(Long ownerId);
}