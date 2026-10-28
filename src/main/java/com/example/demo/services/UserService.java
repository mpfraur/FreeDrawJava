
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
        if (doesUserExist(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        User newUser = new User(username, password);
        return userRepository.save(newUser);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public boolean doesUserExist(String username) {
        return userRepository.getAllUsers().stream().anyMatch(existingUser -> existingUser.getUsername().equals(username));
    }

    public boolean authenticated(String username, String password) {
        User user;
        User fetchedUser;

        if (!doesUserExist(username)) {
            return false;
        } else {
            user = new User(username, password);
            fetchedUser = userRepository.getAllUsersHashMap().get(user.getUsername());
        }

        return fetchedUser != null && user.getPassword().equals(fetchedUser.getPassword());
    }
}
