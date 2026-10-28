package com.example.demo.repository;

import com.example.demo.model.User;

import java.util.List;
import java.util.Map;

public interface UserRepository {
    void createUser(User user);

    User save(User newUser);
    List<User> getAllUsers();
    Map<String, User> getAllUsersHashMap();
}
