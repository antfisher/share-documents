package com.example.ShareDocuments.Repositories;

import com.example.ShareDocuments.Entities.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
   @Override
   Optional<File> findById(Long id);
   List<File> findByOwnerId(Long ownerId);

   @Query("SELECT f FROM files f JOIN f.coworkers c WHERE c.id = :coworkerId")
   List<File> findAllByCoworkerId(@Param("coworkerId") Long coworkerId);
}