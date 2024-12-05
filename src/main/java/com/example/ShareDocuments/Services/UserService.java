package com.example.ShareDocuments.Services;

import com.example.ShareDocuments.Entities.User;
import com.example.ShareDocuments.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User findUserById(Long id) {
        return repository.findUserById(id);
    }

    public User findUserByLogin(String login) {
        return repository.findUserByLogin(login);
    }

    public User updateUser(User user, String firstName, String lastName) {
        if (!firstName.isEmpty() && firstName != null) {
            user.setFirstName(firstName);
        }
        if (!lastName.isEmpty() && lastName != null) {
            user.setLastName(lastName);
        }
        repository.save(user);

        return user;
    }
}
