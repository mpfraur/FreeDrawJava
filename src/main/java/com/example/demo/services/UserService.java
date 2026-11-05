
package com.example.demo.services;

import com.example.demo.DTO.DrawDTO;
import com.example.demo.model.Drawings;
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

    public void createNewDrawing(User user, Drawings drawings) {
        user.getDrawingList().add(drawings);
    }

    public void deleteDrawing(User user, int drawingID) {
        for (int i = user.getDrawingList().size() - 1; i >= 0; i--) {
            if (user.getDrawingList().get(i).getID() == drawingID) {
                user.getDrawingList().remove(i);
                break;
            }
        }
    }


    public void updateDrawing(User user, int drawingID, List<DrawDTO> data) {
        for (int i = 0; i < user.getDrawingList().size(); i++) {
            if (user.getDrawingList().get(i).getID() == drawingID) {
                user.getDrawingList().get(i).setContent(data);
            }
        }
    }

    public User getUserByUsername(String name) {
        List<User> repo = userRepository.getAllUsers();
        for (int i = 0; i < repo.size(); i++) {
            if (repo.get(i).getUsername().equals(name)) {
                return repo.get(i);
            }
        }
        return null;
    }


}
