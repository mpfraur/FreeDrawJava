
package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<String, User> repository = new HashMap<>();
    private JdbcTemplate database;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        repository.put("Admin", new User("Admin", "1234"));
        this.database = jdbcTemplate;

    }

    //Depricated
    @Override
    public void createUser(User user) {
        repository.put(user.getUsername(), user);
    }
    //Depricated

    @Override
    public User save(User user) {
        database.update("insert into Users (username, pass) values (?, ?)", user.getUsername(), user.getPassword());
        repository.put(user.getUsername(), user);
        return user;
    }

    //depricated
    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(repository.values());
    }
    // Depricated

    @Override
    public List<User> getAllUsersFromDatabase() {
        List<User> allUsers = database.query("select * from Users",
                (rs, rowNum) -> {
                    User user = new User(rs.getString("username"), rs.getString("pass"));
                    user.setId(rs.getInt("user_id"));
                    return user;
                }
        );
        return allUsers;
    }

    public boolean contains(String username) {
        Map<String, User> map = new HashMap<>();
        List<User> users = getAllUsersFromDatabase();
        users.forEach(u -> map.put(u.getUsername(), u));
        return map.containsKey(username);
    }

    @Override
    public Map<String, User> getAllUsersHashMap() {
        Map<String, User> map = new HashMap<>();
        List<User> users = getAllUsersFromDatabase();
        users.forEach(u -> map.put(u.getUsername(), u));
        return map;
    }

}
