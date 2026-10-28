package com.example.demo.repository;

import com.example.demo.model.User;

import java.util.List;

public interface UserRepository {
    void createUser(User user);

    User save(User newUser);
    List<User> getAllUsers();
}
