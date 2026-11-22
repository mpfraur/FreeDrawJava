package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    static int idCount;
    int id;
    String username;
    String password;
    List<Drawings> drawingsList;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        drawingsList = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Drawings> getDrawingList() {
        return this.drawingsList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Drawings> getDrawingsList() {
        return drawingsList;
    }

    public void setDrawingsList(List<Drawings> drawingsList) {
        this.drawingsList = drawingsList;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' + this.id +
                ", password='" + password + " Drawings: " + drawingsList + '\'' +
                '}';
    }

    public Drawings getDrawingByID(int id) {
        return getDrawingList().stream().filter(d -> d.getID() == id).findFirst().orElse(null);
    }
}
