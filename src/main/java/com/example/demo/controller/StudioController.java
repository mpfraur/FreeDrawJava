package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpSession;
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
    public String openEditController(@PathVariable("id") int id, Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        User user = userService.getUserByUsername(username);
        if (user != null) {
            model.addAttribute("drawingID", id);
            return "studio/canvas";
        }

        return "redirect:/home";
    }
}
