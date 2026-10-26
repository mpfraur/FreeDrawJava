package com.example.demo.repository;

import com.example.demo.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepositoryImpl implements UserRepository{
    Map<String, User> repository = new HashMap<>();

    @Override
    public void createUser(User user) {
        repository.put(user.getUsername(), user);
    }
}
