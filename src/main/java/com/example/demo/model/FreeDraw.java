package com.example.demo.model;

import java.util.List;

public class FreeDraw extends Draw{
    boolean isDrawing;
    List<PositionDTO> pointArrays;

    // color, lineWidth, position, ctx, isDrawing, pointsArray
    public FreeDraw(String col, int lineWidth, PositionDTO position, boolean isDrawing, List<PositionDTO> pointArrays) {
        super(col, lineWidth, position);
        this.isDrawing = isDrawing;
        this.pointArrays = pointArrays;
    }

}
