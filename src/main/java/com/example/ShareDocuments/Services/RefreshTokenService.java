package com.example.ShareDocuments.Services;

import com.example.ShareDocuments.Entities.RefreshToken;
import com.example.ShareDocuments.Entities.User;
import com.example.ShareDocuments.Repositories.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {

    final RefreshTokenRepository repository;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    public RefreshToken createToken (String token, User user) {
        RefreshToken refreshToken = new RefreshToken(token, user);
        repository.save(refreshToken);
        return refreshToken;
    }
}
