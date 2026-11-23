package com.example.demo.controller;

import com.example.demo.DTO.SharedDrawingDTO;
import com.example.demo.DTO.SharedUserDTO;
import com.example.demo.model.DrawingObject;
import com.example.demo.model.PermissionsObject;
import com.example.demo.model.User;
import com.example.demo.services.DrawingObjectService;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/settings")
public class DrawingSettingsController {

    @Autowired
    UserService userService;

    @Autowired
    DrawingObjectService drawingObjectService;

    @GetMapping("/{drawingID}")
    public String openDrawingSettings(@PathVariable int drawingID, HttpSession session, Model model) {
        User user = userService.getUserByUsername((String) session.getAttribute("username"));
        if (user == null) return "redirect:/login";
        DrawingObject drawing = drawingObjectService.getDrawingByID(drawingID);
        if (drawing.getUser_id() != user.getId()) return "redirect:/error404";
        model.addAttribute("drawing", drawing);

        List<SharedUserDTO> sharedUserList = drawingObjectService.getPermissionByDrawingID(drawingID);
        model.addAttribute("sharedUsers", sharedUserList);
        return "settings/settings";
    }


}
