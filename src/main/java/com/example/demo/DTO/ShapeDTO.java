package com.example.demo.DTO;

public class ShapeDTO extends DrawDTO {

    //shape, isFilled, color, lineWidth, position, ctx
    String shapeName;
    boolean isFilled;

    public ShapeDTO(String col, int lineWidth, PositionDTO position, String shapename, boolean isFilled) {
        super(col, lineWidth, position);
        this.shapeName = shapename;
        this.isFilled = isFilled;
    }

    @Override
    public String toString() {
        return "ShapeDTO{" +
                "shapeName='" + shapeName + '\'' +
                ", isFilled=" + isFilled +
                ", color='" + color + '\'' +
                ", lineWidth=" + lineWidth +
                ", position=" + position +
                '}';
    }
}
