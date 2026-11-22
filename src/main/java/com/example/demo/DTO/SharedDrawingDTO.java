package com.example.demo.DTO;

import com.example.demo.model.DrawingObject;
import com.example.demo.model.PermissionsObject;

public record SharedDrawingDTO(PermissionsObject perm, DrawingObject drawingObject) {
}
