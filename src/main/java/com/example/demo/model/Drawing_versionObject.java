package com.example.demo.model;

import java.time.LocalDateTime;

public class Drawing_versionObject {
    int version_id;
    int drawing_id;
    String drawing_data;
    LocalDateTime created_at;

    public Drawing_versionObject(int version_id, int drawing_id, String drawing_data, LocalDateTime created_at) {
        this.version_id = version_id;
        this.drawing_id = drawing_id;
        this.drawing_data = drawing_data;
        this.created_at = created_at;
    }
    public Drawing_versionObject() {}

    public int getVersion_id() {
        return version_id;
    }

    public void setVersion_id(int version_id) {
        this.version_id = version_id;
    }

    public int getDrawing_id() {
        return drawing_id;
    }

    public void setDrawing_id(int drawing_id) {
        this.drawing_id = drawing_id;
    }

    public String getDrawing_data() {
        return drawing_data;
    }

    public void setDrawing_data(String drawing_data) {
        this.drawing_data = drawing_data;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
