package com.example.demo.DTO;

public class ShapeDTO extends DrawDTO {

    String shapeName;
    boolean isFilled;
    int size;

    public ShapeDTO(String col, int lineWidth, PositionDTO position, String shapename, boolean isFilled, int size) {
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