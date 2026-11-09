package com.example.demo.services;

import com.example.demo.model.Drawings;
import com.example.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GalleryService {
    static final int ammountOfDrawingsPerUser = 5;
    static final int ammountOfUsersShownGallery = 10;

    private final UserService userService;

    public GalleryService(UserService userService) {
        this.userService = userService;
    }

    // this function is deprecated. oh, well
    public List<Drawings> getDrawingsGallery(User user) {
        List<Drawings> list = userService.getUserByUsername(user.getUsername()).getDrawingList();
        if (list.size() > ammountOfDrawingsPerUser) {
            list = list.stream().limit(ammountOfDrawingsPerUser).toList();
        }
        return list;
    }

    public List<Drawings> getGallery() {
        List<Drawings> gallery = new ArrayList<>();
        List<User> users = userService.getAllUsers().stream().limit(ammountOfUsersShownGallery).toList();

        for (int i = 0; i < users.size(); i++) {
            User currentUser = users.get(i);
            List<Drawings> userDrawings = currentUser.getDrawingList().stream().limit(ammountOfDrawingsPerUser).toList();
            gallery.addAll(userDrawings);
        }

        return gallery;
    }
}
