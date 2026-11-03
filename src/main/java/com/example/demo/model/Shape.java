package com.example.demo.model;

public class Shape extends Draw{

    //shape, isFilled, color, lineWidth, position, ctx
    String shapeName;
    boolean isFilled;

    public Shape(String col, int lineWidth, PositionDTO position, String shapename, boolean isFilled) {
        super(col, lineWidth, position);
        this.shapeName = shapename;
        this.isFilled = isFilled;
    }
}
