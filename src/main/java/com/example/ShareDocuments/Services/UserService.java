package com.example.ShareDocuments.Services;

import com.example.ShareDocuments.Entities.User;
import com.example.ShareDocuments.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User findUserByLogin(String login) {
        return repository.findUserByLogin(login);
    }
}
