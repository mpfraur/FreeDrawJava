package com.example.demo.controller;

import com.example.demo.model.Draw;
import com.example.demo.model.DrawingObject;
import com.example.demo.model.Drawings;
import com.example.demo.model.User;
import com.example.demo.services.DrawingObjectService;
import com.example.demo.services.DrawingService;
import com.example.demo.services.UserService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/studio")
public class StudioController {

    @Autowired
    private UserService userService;
    @Autowired
    private DrawingService drawingService;
    @Autowired
    private DrawingObjectService drawingObjectService;


    @GetMapping("/canva/{id}")
    public String openEditController(@PathVariable("id") int id, Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        User user = userService.getUserByUsername(username);

        if (user == null) return "redirect:/login";

        DrawingObject drawing = drawingObjectService.getDrawingByID(id);

        if (drawing == null) return "redirect:/error404";

        boolean isOwner = drawing.getUser_id() == user.getId();
        boolean hasWritePermission = drawingObjectService.checkForPermissionByUser(user.getId(), id);

        if (!isOwner && !hasWritePermission) {
            return "redirect:/app/dashboard";
        }

        model.addAttribute("drawingID", id);

        // Load drawing content here

        return "studio/canvas";
    }

    @PostMapping("/canvas/save")
    public String fetchDataSaveStringJSON(@RequestParam String jsonData, HttpSession session, @RequestParam int drawingID) {
        String username = (String) session.getAttribute("username");
        User user = userService.getUserByUsername(username);
        if (user != null) {
                drawingService.saveDrawing(user, jsonData, drawingID);
//            List<Draw> data = DrawingService.parseJsonService(jsonData);
//            System.out.println(data);
//            userService.updateDrawing(user, drawingID, data);
//            System.out.println("succsessfully saved");
//            System.out.println(user);
        }

        return "redirect:/home";
    }
}


























