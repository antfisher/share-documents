package com.example.ShareDocuments.Controllers;

import com.example.ShareDocuments.Config.Auth.TokenProvider;
import com.example.ShareDocuments.DTO.JwtDto;
import com.example.ShareDocuments.DTO.SignInDto;
import com.example.ShareDocuments.DTO.SignUpDto;
import com.example.ShareDocuments.DTO.UserDto;
import com.example.ShareDocuments.Entities.User;
import com.example.ShareDocuments.Repositories.UserRepository;
import com.example.ShareDocuments.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private UserRepository userRepository;

    AuthController(AuthenticationManager authenticationManager, AuthService service, TokenProvider tokenService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.service = service;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
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

    @GetMapping("/user")
    public User getUser(@RequestParam Long id) {
        return userRepository.findUserById(id);
    }
}
