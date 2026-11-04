package com.example.demo.model;

import java.util.List;

public class Drawings {
    static int idsCount = 0;
    int ID;
    String name;
    User owner;
    List<Draw> content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawings(User userAcc, List<Draw> drawing, String name) {
        this.owner = userAcc;
        this.content = drawing;
        idsCount++;
        this.ID = idsCount;
        this.name = name;
    }

    public static int getIdsCount() {
        return idsCount;
    }

    public static void setIdsCount(int idsCount) {
        Drawings.idsCount = idsCount;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Draw> getContent() {
        return content;
    }

    public void setContent(List<Draw> content) {
        this.content = content;
    }
}
