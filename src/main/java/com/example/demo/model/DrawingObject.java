package com.example.demo.model;

import java.time.LocalDateTime;

public class DrawingObject {
    int drawing_id;
    int user_id;
    String name;
    LocalDateTime creation_date;
    Permission status;
    LocalDateTime deleted_at;

    public DrawingObject(int drawing_id, int user_id, String name, LocalDateTime creation_date, Permission status, LocalDateTime deleted_at) {
        this.drawing_id = drawing_id;
        this.user_id = user_id;
        this.name = name;
        this.creation_date = creation_date;
        this.status = status;
        this.deleted_at = deleted_at;
    }

    public int getDrawing_id() {
        return drawing_id;
    }

    public void setDrawing_id(int drawing_id) {
        this.drawing_id = drawing_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(LocalDateTime creation_date) {
        this.creation_date = creation_date;
    }

    public Permission getStatus() {
        return status;
    }

    public void setStatus(Permission status) {
        this.status = status;
    }

    public LocalDateTime getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(LocalDateTime deleted_at) {
        this.deleted_at = deleted_at;
    }

    public enum Permission {
        PUBLIC,
        PRIVATE
    }
}
