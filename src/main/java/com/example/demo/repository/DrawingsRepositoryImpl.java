package com.example.demo.repository;

import com.example.demo.model.*;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.security.Permission;
import java.util.List;

@Repository
public class DrawingsRepositoryImpl implements DrawingsRepository {

    private final JdbcTemplate database;
    private final UserService userService;

    public DrawingsRepositoryImpl(JdbcTemplate database, UserService userService) {
        this.database = database;
        this.userService = userService;
    }

    @Override
    public DrawingObject save(DrawingObject drawing) {
        database.update(
                "insert into Drawings (user_id, name, status) values (?, ?, ?)",
                drawing.getUser_id(),
                drawing.getName(),
                drawing.getStatus().name()
        );
        DrawingObject newDrawing = database.queryForObject("select * from Drawings where drawing_id = last_insert_id()", new BeanPropertyRowMapper<>(DrawingObject.class));
        return newDrawing;
    }

    @Override
    public DrawingObject findById(int drawingID) {
//        Drawings drawings = database.queryForObject("select * from Drawings where Drawings.drawing_id == ?",
//                (rs, rowNum) -> new Drawings(
//                        rs.getInt("drawing_id"),
//                        rs.getString("name")
//                ),
//                );
        DrawingObject drawing = database.queryForObject("select * from Drawings where drawing_id = ? limit 1", new BeanPropertyRowMapper<>(DrawingObject.class), drawingID);
        return drawing;
    }

    @Override
    public List<DrawingObject> findAllByUserId(int userID) {
        List<DrawingObject> userDrawings = database.query("select * from Drawings where user_id = ?", new BeanPropertyRowMapper<>(DrawingObject.class), userID);
        return userDrawings;
    }

    @Override
    public List<DrawingObject> findAllPublic() {
        List<DrawingObject> allPublic = database.query("select * from Drawings where status = 'PUBLIC'", new BeanPropertyRowMapper<>(DrawingObject.class));
        return allPublic;
    }

    @Override
    public void update(DrawingObject drawing) {
        database.update("update Drawings set user_id = ?, name = ?,  creation_date = ?, status = ?, deleted_at = ? where drawing_id = ?",
                drawing.getUser_id(),
                drawing.getName(),
                drawing.getCreation_date(),
                drawing.getStatus().name(),
                drawing.getDeleted_at(),
                drawing.getDrawing_id()
        );
    }

    @Override
    public List<DrawingObject> findAll() {
        List<DrawingObject> drawings = database.query(
                "select * from Drawings",
                new BeanPropertyRowMapper<>(DrawingObject.class)
        );
        return drawings;
    }

    @Override
    public void delete(int drawingID) {
        database.update("delete from Drawings where Drawings.drawing_id = ?", drawingID);
    }

    @Override
    public boolean existsById(int drawingID) {
        Integer count = database.queryForObject("select count(*) from Drawings where drawing_id = ?", Integer.class, drawingID);
        return count > 0;
    }

    @Override
    public void saveVersion(int drawingId, String jsonData) {
        database.update("insert into Drawing_versions (drawing_id, drawing_data) values (?, ?)", drawingId, jsonData);
    }

    @Override
    public List<Drawing_versionObject> findVersionsByID(int drawingId) {
        List<Drawing_versionObject> versions = database.query("select * from Drawing_versions where Drawing_versions.drawing_id = ?", new BeanPropertyRowMapper<>(Drawing_versionObject.class), drawingId);
        return versions;
    }

    @Override
    public Drawing_versionObject getVersionByID(int drawingId, int versionId) {
        Drawing_versionObject version = database.queryForObject("select * from Drawing_versions where drawing_id = ? and version_id = ? limit 1", new BeanPropertyRowMapper<>(Drawing_versionObject.class), drawingId, versionId);
        return version;
    }

    @Override
    public Drawing_versionObject getLatestVersion(int drawingId) {
        Drawing_versionObject latestVersion;
        try {
            latestVersion = database.queryForObject("select * from Drawing_versions where Drawing_versions.drawing_id = ? order by Drawing_versions.version_id desc limit 1", new BeanPropertyRowMapper<>(Drawing_versionObject.class), drawingId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return latestVersion;
    }

    @Override
    public void deleteVersions(int drawingID) {
        database.update("delete from Drawing_versions where drawing_id = ?", drawingID);
    }

    @Override
    public void createPermission(int drawingId, int userId, PermissionType permissionType) {
        database.update("insert into Permissions (user_id, drawing_id, permission_type) values (?, ?, ?)", userId, drawingId, permissionType.name());
    }

    @Override
    public void updatePermission(int drawingId, int userId, PermissionType permissionType) {
        database.update(
                "update Permissions set permission_type = ? where drawing_id = ? and user_id = ?",
                permissionType.name(),
                drawingId,
                userId
        );
    }

    @Override
    public void deletePermission(int drawingId, int userId) {
        database.update("delete from Permissions where drawing_id = ? and user_id = ?", drawingId, userId);
    }

    @Override
    public List<PermissionsObject> findPermissionsByDrawingId(int drawingId) {
        List<PermissionsObject> allPermissions = database.query("select * from Permissions where drawing_id = ?", new BeanPropertyRowMapper<>(PermissionsObject.class), drawingId);
        return allPermissions;
    }

    @Override
    public PermissionsObject findPermission(int drawingId, int userId) {
        PermissionsObject permission;
        try {
            permission = database.queryForObject("select * from Permissions where drawing_id = ? and user_id = ?", new BeanPropertyRowMapper<>(PermissionsObject.class), drawingId, userId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return permission;
    }

    @Override
    public boolean hasPermission(int drawingId, int userId) {
        PermissionsObject permission;
        try {
            permission = database.queryForObject("select * from Permissions where user_id = ? and drawing_id = ? limit 1", new BeanPropertyRowMapper<>(PermissionsObject.class), userId, drawingId);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canRead(int drawingId, int userId) {
        PermissionsObject permission;
        try {
            permission = database.queryForObject("select * from Permissions where user_id = ? and drawing_id = ? limit 1", new BeanPropertyRowMapper<>(PermissionsObject.class), userId, drawingId);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return permission.getPermission_type() == PermissionsObject.PermissionType.READ;
    }

    @Override
    public boolean canWrite(int drawingId, int userId) {
        PermissionsObject permission;
        try {
            permission = database.queryForObject("select * from Permissions where user_id = ? and drawing_id = ? limit 1", new BeanPropertyRowMapper<>(PermissionsObject.class), userId, drawingId);
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return permission.getPermission_type() == PermissionsObject.PermissionType.WRITE;
    }

    public enum PermissionType {
        READ,
        WRITE
    }
}


