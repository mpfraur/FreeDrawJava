
package com.example.demo.services;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository = new UserRepositoryImpl();

    public User createUser(String username, String password) {
        if (userRepository.getAllUsers().stream()
                .anyMatch(user -> user.getUsername().equals(username))) {
            throw new IllegalArgumentException("Username already exists");
        }

        User newUser = new User(username, password);
        return userRepository.save(newUser);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public boolean authenticated(String username, String password) {
        User user;
        if (userRepository.getAllUsers().stream()
                .anyMatch(user -> user.getUsername().equals(username))) {
            // you have
            throw new IllegalArgumentException("Username already exists");
        }

        return false;
    }
}
