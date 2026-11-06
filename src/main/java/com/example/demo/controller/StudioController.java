package com.example.demo.controller;

import com.example.demo.DTO.DrawDTO;
import com.example.demo.model.Drawings;
import com.example.demo.model.User;
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

    private final UserService userService;
    private final DrawingService drawingService;

    public StudioController(UserService userService, DrawingService drawingService) {
        this.userService = userService;
        this.drawingService = drawingService;
    }

    @GetMapping("/canva/{id}")
    public String openEditController(@PathVariable("id") int id, Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        User user = userService.getUserByUsername(username);
        if (user != null) {
            model.addAttribute("drawingID", id);

            Drawings drawingData = user.getDrawingByID(id);
            if (drawingData != null) {
                String drawingDataSetAsJSON = new Gson().toJson(drawingData);
                model.addAttribute("JSON_test_print", drawingDataSetAsJSON);
            }

            return "studio/canvas";
        }
        return "redirect:/home";
    }

    @PostMapping("/canvas/save")
    public String fetchDataSaveStringJSON(@RequestParam String jsonData, HttpSession session, @RequestParam int drawingID) {
        String username = (String) session.getAttribute("username");
        User user = userService.getUserByUsername(username);
        if (user != null) {
            List<DrawDTO> data = DrawingService.parseJsonService(jsonData);
            System.out.println(data);
            userService.updateDrawing(user, drawingID, data);
            System.out.println("succsessfully saved");
            System.out.println(user);
        }

        return "redirect:/home";
    }
}


























