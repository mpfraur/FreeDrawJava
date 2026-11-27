package com.example.demo.services;

import com.example.demo.DTO.SharedDrawingDTO;
import com.example.demo.DTO.SharedUserDTO;
import com.example.demo.DTO.UserDrawingDTO;
import com.example.demo.model.*;
import com.example.demo.repository.DrawingsRepository;
import com.example.demo.repository.DrawingsRepositoryImpl;
import com.example.demo.repository.UserRepositoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

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

    // --------------- DRAWING STUFF ------------------------------------- //

    public DrawingObject getDrawingByID(int drawingID) {
        return drawingsRepository.findById(drawingID);
    }

    public List<DrawingObject> getUsersDrawingByID(int userID) {
        List<DrawingObject> list = drawingsRepository.findAllByUserId(userID);
        list = list.stream().filter(d -> d.getDeleted_at() == null).toList();
        return list;
    }

    // ----------- Gets the Drawings shared by a user ID
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

    // ------------- Hard Delete
    public void hardDeleteDrawing(int drawingId) {
        drawingsRepository.delete(drawingId);
    }

    // ------------ Restore deleted one!
    public void restoreDeletedDrawing(int drawingID) {
        DrawingObject drawing = drawingsRepository.findById(drawingID);
        drawing.setDeleted_at(null);
        drawingsRepository.update(drawing);
    }

    // ------------ create new Drawing
    public DrawingObject createNewDrawing(int userID, String name, DrawingObject.Status visibility) {
        DrawingObject drawing = new DrawingObject(userID, name, visibility);
        return drawingsRepository.save(drawing);
    }

    // ------------- fetches all Public Drawings
    public List<DrawingObject> fetchAllPublicDrawings() {
        return drawingsRepository.findAllPublic();
    }
    public List<UserDrawingDTO> parseDTODrawingList(List<DrawingObject> list) {
        List<UserDrawingDTO> userDrawingList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            DrawingObject drawing = list.get(i);
            Drawing_versionObject drawingData = fetchLatestVersion(drawing.getDrawing_id());
            if (drawingData == null) continue;
            String json = drawingData.getDrawing_data();
            JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
            userDrawingList.add(new UserDrawingDTO(userService.getUserByID(drawing.getUser_id()), drawing, jsonArray.size()));
        }
        return userDrawingList;
    }


    // ------------- PERMISSIONS SECTION ------------------


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


    // ------------- Create new Permission to User
    public void createNewPermission(String username, int drawingID, int ownerID, DrawingsRepositoryImpl.PermissionType permission) {
        User userPerm = userService.getUserByUsername(username);
        DrawingObject drawing = getDrawingByID(drawingID);
        // make sure User and owner is not the same user
        if (drawing != null && userPerm != null && drawing.getUser_id() == ownerID && userPerm.getId() != ownerID) {
            drawingsRepository.createPermission(drawingID, userPerm.getId(), permission);
        }
    }

    // ------------- Revoke user Permission
    public void revokeUserPermission(int userID, int drawingId) {
        drawingsRepository.deletePermission(drawingId, userID);
    }

    // -------------- Check WRITE permission
    public boolean checkForPermissionWriteByUser(int userId, int drawingId) {
        PermissionsObject permission = drawingsRepository.findPermission(drawingId, userId);

        if (permission != null && permission.getPermission_type() == PermissionsObject.PermissionType.WRITE) {
            System.out.println("WRITE permission successful");
            return true;
        }

        System.out.println("User: " + userId + " lacks WRITE permission for drawing " + drawingId);
        return false;
    }

    // -------------- Check READ permission
    public boolean checkForPermissionReadByUser(int userId, int drawingId) {
        PermissionsObject permission = drawingsRepository.findPermission(drawingId, userId);

        if (permission != null &&
                (permission.getPermission_type() == PermissionsObject.PermissionType.READ || permission.getPermission_type() == PermissionsObject.PermissionType.WRITE)) {
            System.out.println("READ permission successful");
            return true;
        }

        System.out.println("User: " + userId + " lacks READ permission for drawing " + drawingId);
        return false;
    }



    // -------------------- VERSIONS SECTION ----------------------------- //




    // --------- Create new Drawing version
    public void createNewVersion(int drawingId, String jsonData) {
        drawingsRepository.saveVersion(drawingId, jsonData);
    }

    // -------- fetching Latest Drawing Version
    public Drawing_versionObject fetchLatestVersion(int drawingId) {
        return drawingsRepository.getLatestVersion(drawingId);
    }

    // --------- Get All Versions
    public List<Drawing_versionObject> getAllDrawingVersions(int drawingId) {
        return drawingsRepository.findVersionsByID(drawingId);
    }

    // --------- Gets Version based off version and drawingID
    public Drawing_versionObject getVersionByID(int versionId, int drawingId) {
        return drawingsRepository.getVersionByID(drawingId, versionId);
    }

    // ----------- Creates copy of Drawing on Version ID
    public DrawingObject createNewDrawingFromPrevDrawing(int versionDrawingId, int drawingId, int userId, DrawingObject.Status status) {
        DrawingObject drawing = getDrawingByID(drawingId);
        if (drawing != null) {
            Drawing_versionObject version = drawingsRepository.getVersionByID(drawingId, versionDrawingId);
            User user = userService.getUserByID(userId);
            boolean isAuthorizedWrite = checkForPermissionWriteByUser(userId, drawingId);
            boolean isAuthorizedRead = checkForPermissionReadByUser(userId, drawingId);
            boolean isOwner = (userId == drawing.getUser_id());
            DrawingObject newDrawingCopy = null;
            if (isAuthorizedRead || isAuthorizedWrite || isOwner) {
                newDrawingCopy = createNewDrawing(userId, "Copy of : " + drawing.getName(), status);
            }
            return newDrawingCopy;
        }
        return null;
    }

    // --------------- Reverts Drawing into a version version
    public void revertToOldDrawingVersion(int drawingId, int versionId) {
        DrawingObject drawing = getDrawingByID(drawingId);
        Drawing_versionObject version = getVersionByID(versionId,drawingId);
        if (drawing != null && version != null && version.getDrawing_data() != null) {
            createNewVersion(drawingId, version.getDrawing_data());
        }
    }



}
