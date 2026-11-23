package com.example.demo.services;

import com.example.demo.DTO.SharedDrawingDTO;
import com.example.demo.DTO.SharedUserDTO;
import com.example.demo.model.Draw;
import com.example.demo.model.DrawingObject;
import com.example.demo.model.PermissionsObject;
import com.example.demo.model.User;
import com.example.demo.repository.DrawingsRepository;
import com.example.demo.repository.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Permission;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DrawingObjectService {

    private final DrawingsRepository drawingsRepository;
    private final UserService userService;
    private final UserRepositoryImpl userRepositoryImpl;

    public DrawingObjectService(DrawingsRepository drawingsRepository, UserService userService, UserRepositoryImpl userRepositoryImpl) {
        this.drawingsRepository = drawingsRepository;
        this.userService = userService;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    public DrawingObject getDrawingByID(int drawingID) {
        return drawingsRepository.findById(drawingID);
    }

    public List<DrawingObject> getUsersDrawingByID(int userID) {
        List<DrawingObject> list = drawingsRepository.findAllByUserId(userID);
        list = list.stream().filter(d -> d.getDeleted_at() == null).toList();
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

    //------------- Soft Delete
    public void softDeleteDrawing(int drawingID) {
        DrawingObject drawing = drawingsRepository.findById(drawingID);
        drawing.setDeleted_at(LocalDateTime.now());
        drawingsRepository.update(drawing);
    }

    // ------------ Restore deleted one!
    public void restoreDeletedDrawing(int drawingID) {
        DrawingObject drawing = drawingsRepository.findById(drawingID);
        drawing.setDeleted_at(null);
        drawingsRepository.update(drawing);
    }

    // ------------ create new Drawing
    public void createNewDrawing(int userID, String name, DrawingObject.Status visibility) {
        DrawingObject drawing = new DrawingObject(userID, name, visibility);
        drawingsRepository.save(drawing);
    }

    // ------------ Fetch all Permissions based on a Drawing
    public List<SharedUserDTO> parsePermissionDTO(List<PermissionsObject> permissions) {
        return new ArrayList<SharedUserDTO>(permissions.stream().map(p -> new SharedUserDTO(p, userService.getUserByID(p.getUser_id()))).toList());
    }

    public List<SharedUserDTO> getPermissionByDrawingID(int drawingID) {
        List<PermissionsObject> permissions =
                drawingsRepository.findPermissionsByDrawingId(drawingID);

        return permissions.stream()
                .map(p -> {
                    System.out.println("perm=" + p.getPermission_id() + " user_id=" + p.getUser_id());
                    User u = userService.getUserByID(p.getUser_id());
                    System.out.println("-> user=" + u);
                    return new SharedUserDTO(p, u);
                })
                .toList();
    }




}
