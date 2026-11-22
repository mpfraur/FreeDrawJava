package com.example.demo.repository;

import com.example.demo.model.*;

import java.security.Permission;
import java.util.List;

public interface DrawingsRepository {
    DrawingObject save(DrawingObject drawing);
    DrawingObject findById(int drawingID);
    List<DrawingObject> findAllByUserId(int userID);
    List<DrawingObject> findAllPublic();
    void update(DrawingObject drawing);
    void delete(int drawingID);
    boolean existsById(int drawingID);
    List<DrawingObject> findAll();

    void saveVersion(int drawingId, String jsonData);
    List<Drawing_versionObject> findVersionByID(int drawingId);
    Drawing_versionObject getLatestVersion(int drawingId);
    void deleteVersions(int drawingID);

    void createPermission(int drawingId, int userId, DrawingsRepositoryImpl.PermissionType permissionType);
    void updatePermission(int drawingId, int userId, DrawingsRepositoryImpl.PermissionType permissionType);
    void deletePermission(int drawingId, int userId);
    List<PermissionsObject> findPermissionsByDrawingId(int drawingId);
    PermissionsObject findPermission(int drawingId, int userId);
    boolean hasPermission(int drawingId, int userId);
    boolean canRead(int drawingId, int userId);
    boolean canWrite(int drawingId, int userId);

}
