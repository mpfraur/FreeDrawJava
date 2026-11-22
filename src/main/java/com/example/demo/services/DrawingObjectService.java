package com.example.demo.services;

import com.example.demo.DTO.SharedDrawingDTO;
import com.example.demo.model.Draw;
import com.example.demo.model.DrawingObject;
import com.example.demo.model.PermissionsObject;
import com.example.demo.repository.DrawingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DrawingObjectService {

    private final DrawingsRepository drawingsRepository;

    public DrawingObjectService(DrawingsRepository drawingsRepository) {
        this.drawingsRepository = drawingsRepository;
    }

    public DrawingObject getDrawingByID(int drawingID) {
        return drawingsRepository.findById(drawingID);
    }

    public List<DrawingObject> getUsersDrawingByID(int userID) {
        List<DrawingObject> list = drawingsRepository.findAllByUserId(userID);
        return list;
    }

    public List<SharedDrawingDTO> getDrawingsSharedByUser(int userID) {
        List<DrawingObject> AllDrawings = drawingsRepository.findAll();
        List<SharedDrawingDTO> sharedWithUser = new ArrayList<>();

        for (int i = 0; i < AllDrawings.size(); i++) {
            DrawingObject current = AllDrawings.get(i);
            if (drawingsRepository.hasPermission(current.getDrawing_id(), userID)) {
                sharedWithUser.add(parseDrawingIntoDTO(current, userID));
            }
        }
        return sharedWithUser;
    }

    public List<DrawingObject> drawingsInTheBin(int userID) {
        return drawingsRepository.findAllByUserId(userID).stream().filter(d -> d.getDeleted_at() != null).toList();
    }
    // ----------- Parse into DTO
    public SharedDrawingDTO parseDrawingIntoDTO(DrawingObject drawing, int userID) {
        PermissionsObject permission =
                drawingsRepository.findPermission(drawing.getDrawing_id(), userID);

        return new SharedDrawingDTO(permission, drawing);
    }

    // ------------ Restore deleted one!
    public void restoreDeletedDrawing(int drawingID) {
        DrawingObject drawing = drawingsRepository.findById(drawingID);
        drawing.setDeleted_at(null);
        drawingsRepository.update(drawing);
    }



}
