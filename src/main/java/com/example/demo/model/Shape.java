package com.example.demo.model;

public class Shape extends Draw {

    String shapeName;
    boolean isFilled;
    int size;

    public Shape(String col, int lineWidth, PositionDTO position, String shapename, boolean isFilled, int size) {
        super(col, lineWidth, position);
        this.shapeName = shapename;
        this.isFilled = isFilled;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "ShapeDTO{" +
                "shapeName='" + shapeName + '\'' +
                ", isFilled=" + isFilled +
                ", size=" + size +
                ", color='" + color + '\'' +
                ", lineWidth=" + lineWidth +
                ", position=" + position +
                '}';
    }
}