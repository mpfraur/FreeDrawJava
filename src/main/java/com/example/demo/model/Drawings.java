package com.example.demo.model;

import com.example.demo.DTO.DrawDTO;

import java.util.List;

public class Drawings {
    static int idsCount = 0;
    int ID;
    String name;
    User owner;
    List<DrawDTO> content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawings(User userAcc, List<DrawDTO> drawing, String name) {
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

    public List<DrawDTO> getContent() {
        return content;
    }

    public void setContent(List<DrawDTO> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Drawings{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", content=" + content +
                '}';
    }
}
