package com.example.demo.DTO;

import com.example.demo.model.PermissionsObject;
import com.example.demo.model.User;

public record SharedUserDTO(PermissionsObject permissionsObject, User user) {
    public User getUser() {
        return user;
    }

    public PermissionsObject getPermission() {
        return permissionsObject;
    }
}
