
package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<String, User> repository = new HashMap<>();

    public UserRepositoryImpl() {
        repository.put("Admin", new User("Admin", "1234"));
    }

    @Override
    public void createUser(User user) {
        repository.put(user.getUsername(), user);
    }

    @Override
    public User save(User user) {
        repository.put(user.getUsername(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(repository.values());
    }

    public boolean contains(String username) {
        return repository.containsKey(username);
    }
}
