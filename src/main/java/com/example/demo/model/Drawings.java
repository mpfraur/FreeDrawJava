package com.example.demo.model;

import java.util.List;

public class Drawings {
    static int idsCount = 0;
    int ID;
    String name;
    List<Draw> content;
    String user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawings(List<Draw> drawing, String name) {
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


    public List<Draw> getContent() {
        return content;
    }

    public void setContent(List<Draw> content) {
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
