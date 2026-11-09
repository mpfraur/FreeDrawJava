package com.example.demo.model;

import java.util.List;

public class FreeDraw extends Draw {
    boolean isDrawing;
    List<PositionDTO> pointsArray;

    // color, lineWidth, position, ctx, isDrawing, pointsArray
    public FreeDraw(String col, int lineWidth, PositionDTO position, boolean isDrawing, List<PositionDTO> pointArrays) {
        super(col, lineWidth, position);
        this.isDrawing = isDrawing;
        this.pointsArray = pointArrays;
    }

    @Override
    public String toString() {
        return "FreeDrawDTO{" +
                "isDrawing=" + isDrawing +
                ", pointsArray=" + pointsArray +
                ", color='" + color + '\'' +
                ", lineWidth=" + lineWidth +
                ", position=" + position +
                '}';
    }
}
