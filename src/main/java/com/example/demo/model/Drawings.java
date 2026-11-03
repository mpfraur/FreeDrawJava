package com.example.demo.model;

import java.util.List;

public class Drawings {
    static int idsCount = 0;
    int ID;
    User owner;
    List<Draw> content;

    public Drawings(User userAcc, List<Draw> drawing) {
        this.owner = userAcc;
        this.content = drawing;
        idsCount++;
        this.ID = idsCount;
    }
}
