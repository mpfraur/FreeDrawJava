package com.example.demo.DTO;

import java.util.List;

public class FreeDrawDTO extends DrawDTO {
    boolean isDrawing;
    List<PositionDTO> pointsArray;

    // color, lineWidth, position, ctx, isDrawing, pointsArray
    public FreeDrawDTO(String col, int lineWidth, PositionDTO position, boolean isDrawing, List<PositionDTO> pointArrays) {
        super(col, lineWidth, position);
        this.isDrawing = isDrawing;
        this.pointsArray = pointArrays;
    }

}
