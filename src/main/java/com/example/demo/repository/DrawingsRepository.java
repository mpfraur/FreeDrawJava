package com.example.demo.repository;

import com.example.demo.model.Drawings;
import com.example.demo.model.User;

import java.util.List;

public interface DrawingsRepository {
    Drawings save(Drawings drawing);
    Drawings findById(int drawingID);
    List<Drawings> findAllByUserId(int userID);
    List<Drawings> findAllPublic();
    void update(Drawings drawing);
    void delete(int drawingID);
    boolean existsById(int drawingID);

}
