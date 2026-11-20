package com.example.demo.model;

public class PermissionsObject {
    int permission_id;
    int user_id;
    int drawing_id;
    PermissionType permission_type;

    public PermissionsObject(int permission_id, int user_id, int drawing_id, PermissionType permission_type) {
        this.permission_id = permission_id;
        this.user_id = user_id;
        this.drawing_id = drawing_id;
        this.permission_type = permission_type;
    }

    public PermissionType getPermission_type() {
        return permission_type;
    }

    public void setPermission_type(PermissionType permission_type) {
        this.permission_type = permission_type;
    }

    public int getPermission_id() {
        return permission_id;
    }

    public void setPermission_id(int permission_id) {
        this.permission_id = permission_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getDrawing_id() {
        return drawing_id;
    }

    public void setDrawing_id(int drawing_id) {
        this.drawing_id = drawing_id;
    }

    public enum PermissionType {
        READ,
        WRITE
    }
}
