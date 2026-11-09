package com.example.demo.controller;

import com.example.demo.model.Drawings;
import com.example.demo.model.User;
import com.example.demo.services.DrawingService;
import com.example.demo.services.GalleryService;
import com.example.demo.services.UserService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/galleries")
public class GalleryController {

    DrawingService drawingService;
    UserService userService;
    GalleryService galleryService;

    public GalleryController(DrawingService drawingService, UserService userService, GalleryService galleryService) {
        this.drawingService = drawingService;
        this.userService = userService;
        this.galleryService = galleryService;
    }

    @GetMapping("/list")
    public String listAllUserDrawings(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return "redirect:/home";
        }

        List<Drawings> otherUsersDrawings = galleryService.getGallery();
        model.addAttribute("drawingsList", otherUsersDrawings);
        return "galleries/list";
    }

    @GetMapping("/canvaview/{id}")
    public String getDrawingController(HttpSession session, @PathVariable("id") int id, Model model) {
        String username = (String) session.getAttribute("username");
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return "redirect:/home";
        }
        Drawings fetchedDrawing = galleryService.getAllDrawings().stream().filter(d -> d.getID() == id).findFirst().orElse(null);
        if (fetchedDrawing == null) return "redirect:/home";
        String jsonDataDrawing = new Gson().toJson(fetchedDrawing);

        model.addAttribute("json_data_view", jsonDataDrawing);
        model.addAttribute("drawingID", fetchedDrawing.getID());
        return "galleries/canvaview";
    }


}
