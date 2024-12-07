package com.example.ShareDocuments.Controllers;

import com.example.ShareDocuments.DTO.RefreshTokenDto;
import com.example.ShareDocuments.DTO.UpdateUserDto;
import com.example.ShareDocuments.DTO.UserDto;
import com.example.ShareDocuments.Entities.User;
import com.example.ShareDocuments.Repositories.UserRepository;
import com.example.ShareDocuments.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public User getUser(@RequestParam Long id) {
        return userService.findUserById(id);
    }

    @GetMapping("/me")
    public UserDto getMe() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return user.getUserDto();
    }

    @PostMapping("/update")
    public UserDto updateUser(@RequestBody @Valid UpdateUserDto data) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return userService.updateUser(user, data.firstName(), data.lastName()).getUserDto();
    }
}
