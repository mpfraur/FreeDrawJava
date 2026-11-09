package com.example.demo.model;



public class Draw {
    // color, lineWidth, position
    String color;
    int lineWidth;
    PositionDTO position;
    // ctx
    public Draw(String col, int lineWidth, PositionDTO position) {
        this.color = col;
        this.lineWidth = lineWidth;
        this.position = position;
    }
}
