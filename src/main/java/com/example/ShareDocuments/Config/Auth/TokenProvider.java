package com.example.ShareDocuments.Config.Auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.ShareDocuments.Entities.RefreshToken;
import com.example.ShareDocuments.Entities.User;
import com.example.ShareDocuments.Repositories.RefreshTokenRepository;
import com.example.ShareDocuments.Services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenProvider {

    final RefreshTokenService refreshTokenService;

    @Value("${security.jwt.token.secret-key}")
    private String JWT_SECRET;

    TokenProvider(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    public String generateAccessToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            return JWT.create()
                    .withSubject(user.getUsername())
                    .withClaim("username", user.getUsername())
                    .withExpiresAt(genAccessExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Error while generating token", exception);
        }
    }

    public String generateRefreshToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            String refreshToken = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(genRefreshExpirationDate())
                    .sign(algorithm);
            refreshTokenService.createToken(refreshToken, user);
            return refreshToken;
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Error while generating token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Error while validating token", exception);
        }
    }

    public String validateRefreshToken(String refreshToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            return JWT.require(algorithm)
                    .build()
                    .verify(refreshToken)
                    .getToken();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Error while validating token", exception);
        }
    }

    public String getNewAccessToken(String refreshToken) {
        String token = validateRefreshToken(refreshToken);
        RefreshToken refreshTokenEntity = refreshTokenService.getRefreshToken(refreshToken);
        User user = refreshTokenEntity.getOwner();
        return generateAccessToken(user);
    }

    private Instant genAccessExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-06:00"));
    }

    private Instant genRefreshExpirationDate() {
        return LocalDateTime.now().plusMonths(2).toInstant(ZoneOffset.of("-06:00"));
    }
}