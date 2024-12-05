package com.example.ShareDocuments.Repositories;

import com.example.ShareDocuments.Entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    RefreshToken findByToken(String token);
    List<RefreshToken> findByOwnerId(Long ownerId);
}
