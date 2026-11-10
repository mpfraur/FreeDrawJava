package com.example.demo.controller;

import com.example.demo.model.Drawings;
import com.example.demo.model.User;
import com.example.demo.services.DrawingService;
import com.example.demo.services.GalleryService;
import com.example.demo.services.UserService;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/galleries")
public class GalleryController {

    @Autowired
    private DrawingService drawingService;

    @Autowired
    private UserService userService;

    @Autowired
    private GalleryService galleryService;

    @GetMapping("/list")
    public String listAllUserDrawings(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return "redirect:/home";
        }

        List<Drawings> otherUsersDrawings = galleryService.getGallery();
        model.addAttribute("drawingsList", otherUsersDrawings);
        model.addAttribute("amountOfDraws", otherUsersDrawings);
        model.addAttribute("ownerUsername", otherUsersDrawings);
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
