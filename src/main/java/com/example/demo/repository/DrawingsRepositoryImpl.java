package com.example.demo.repository;

import com.example.demo.model.Drawings;
import com.example.demo.model.User;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class DrawingsRepositoryImpl implements DrawingsRepository {

    @Autowired
    private JdbcTemplate database;

    @Autowired
    private UserService userService;

    @Override
    public Drawings save(Drawings drawing) {
        User fetchedUser = userService.getUserByUsername(drawing.getUser());
        database.update("insert into Drawings (user_id, name, status) values (?, ?, ?)", fetchedUser.getId(), drawing.getName());
        return drawing;
    }

    @Override
    public Drawings findById(int drawingID) {
//        Drawings drawings = database.queryForObject("select * from Drawings where Drawings.drawing_id == ?",
//                (rs, rowNum) -> new Drawings(
//                        rs.getInt("drawing_id"),
//                        rs.getString("name")
//                ),
//                );
        return null;
    }

    @Override
    public List<Drawings> findAllByUserId(int userID) {
        return List.of();
    }

    @Override
    public List<Drawings> findAllPublic() {
        return List.of();
    }

    @Override
    public void update(Drawings drawing) {

    }

    @Override
    public void delete(int drawingID) {

    }

    @Override
    public boolean existsById(int drawingID) {
        return false;
    }

    public enum PermissionType {
        READ,
        WRITE
    }
}


