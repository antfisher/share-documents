package com.example.ShareDocuments.Controllers;

import com.example.ShareDocuments.Config.Auth.TokenProvider;
import com.example.ShareDocuments.DTO.*;
import com.example.ShareDocuments.Entities.User;
import com.example.ShareDocuments.Services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private AuthService service;
    private TokenProvider tokenService;

    AuthController(AuthenticationManager authenticationManager, AuthService service, TokenProvider tokenService) {
        this.authenticationManager = authenticationManager;
        this.service = service;
        this.tokenService = tokenService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody @Valid SignUpDto data) {
        UserDto user = service.signUp(data);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtDto> signIn(@RequestBody @Valid SignInDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var authUser = authenticationManager.authenticate(usernamePassword);
        var user = (User) authUser.getPrincipal();
        var accessToken = tokenService.generateAccessToken(user);
        var refreshToken = tokenService.generateRefreshToken(user);
        return ResponseEntity.ok(new JwtDto(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refresh(@RequestBody @Valid RefreshTokenDto data) {
        String refreshToken = data.refreshToken();
        var accessToken = tokenService.getNewAccessToken(refreshToken);
        return ResponseEntity.ok(new JwtDto(accessToken, refreshToken));
    }
}
