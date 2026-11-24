package com.example.demo.DTO;

import com.example.demo.model.DrawingObject;
import com.example.demo.model.User;

public record UserDrawingDTO(User user, DrawingObject drawing, int sizeOfDrawing) {
}
