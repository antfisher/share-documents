package com.example.ShareDocuments.Repositories;

import com.example.ShareDocuments.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByLogin(String login);
    User findUserByLogin(String email);
    User findUserById(Long id);
}
