package com.example.demo.DTO;



public class DrawDTO {
    // color, lineWidth, position
    String color;
    int lineWidth;
    PositionDTO position;
    // ctx
    public DrawDTO(String col, int lineWidth, PositionDTO position) {
        this.color = col;
        this.lineWidth = lineWidth;
        this.position = position;
    }
}
