package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

public class User {
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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + " Drawings: " + drawingsList + '\'' +
                '}';
    }

    public Drawings getDrawingByID(int id) {
        return getDrawingList().stream().filter(d -> d.getID() == id).findFirst().orElse(null);
    }
}
