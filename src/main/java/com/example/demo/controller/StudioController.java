package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.services.DrawingObjectService;
import com.example.demo.services.DrawingService;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        boolean hasWritePermission = drawingObjectService.checkForPermissionReadByUser(user.getId(), id);

        if (!isOwner && !hasWritePermission) {
            return "redirect:/app/dashboard";
        }

        model.addAttribute("drawingID", id);

        // Load drawing content here
        Drawing_versionObject latestVersion = drawingObjectService.fetchLatestVersion(id);
        if (latestVersion != null) {
            model.addAttribute("JSON_test_print", latestVersion.getDrawing_data());
        }


        return "studio/canvas";
    }

    @PostMapping("/canvas/save")
    public String fetchDataSaveStringJSON(@RequestParam String jsonData, HttpSession session, @RequestParam int drawingID) {
        String username = (String) session.getAttribute("username");
        User user = userService.getUserByUsername(username);
        DrawingObject drawing = drawingObjectService.getDrawingByID(drawingID);
        if (user == null || drawing == null) return "redirect:/login";
        boolean authorized = drawing.getUser_id() == user.getId() || drawingObjectService.checkForPermissionWriteByUser(user.getId(), drawingID);

        if (authorized) {
                //drawingService.saveDrawing(user, jsonData, drawingID);
                drawingObjectService.createNewVersion(drawingID, jsonData);
            System.out.println("successfully Saved by user " + user.getUsername() + " by drawing ID " + drawing.getDrawing_id());
//            List<Draw> data = DrawingService.parseJsonService(jsonData);
//            System.out.println(data);
//            userService.updateDrawing(user, drawingID, data);
//            System.out.println("succsessfully saved");
//            System.out.println(user);
        }

        return "redirect:/app/dashboard";
    }
}


























