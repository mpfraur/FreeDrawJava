package com.example.demo.controller;

import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/studio")
public class StudioController {

    private final UserService userService;

    public StudioController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/canva/{id}")
    public String openEditController(@PathVariable int id, Model model) {
        model.addAttribute("drawingID", id);
        return "studio/canvas";
    }
}
