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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private final UserService userService;

    private final GalleryService galleryService;

    public UserController(UserService userService, GalleryService galleryService) {
        this.userService = userService;
        this.galleryService = galleryService;
    }

    @GetMapping("/register")
    public String showRegister(HttpSession session) {
        if (session.getAttribute("username") != null) {
            return "redirect:/home";
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        if (password.length() < 5) {
            String warn = "Password must be over 5 characters";
            model.addAttribute("warning", warn);
            return "register";
        }
        userService.createUser(username, password);
        System.out.println(username + " Has been created " + userService.getAllUsers());
        model.addAttribute("message", username + " Has been created " + userService.getAllUsers());
        session.setAttribute("username", username);
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        if (session.getAttribute("username") != null) {
            return "redirect:/home";
        }
        return "login";
    }

    @GetMapping("/home")
    public String showHomePage(HttpSession session, Model model) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        User user = userService.getUserByUsername((String) session.getAttribute("username"));
        model.addAttribute("drawingsList", user.getDrawingList());
        return "home";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        if (userService.authenticated(username, password)) {
            session.setAttribute("username", username);
            return "redirect:/home";
        } else {
            model.addAttribute("message", "Password or Username Incorrect");
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/home")
    public String createNewDrawingController(@RequestParam String drawingName, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        User user = userService.getUserByUsername(username);

        if (drawingName.isEmpty()) {
            drawingName = DrawingService.randomNameGenerator();
        }

        if (user == null) {
            model.addAttribute("message", "Error message");
        } else {
            userService.createNewDrawing(user, new Drawings(new ArrayList<>(), drawingName));
        }
        System.out.println(userService.getUserByUsername(username));

        return "redirect:/home";
    }

    @PostMapping("/home/delete")
    public String deleteDrawingController(@RequestParam int drawingID, HttpSession session) {
        String username = (String) session.getAttribute("username");
        User user = userService.getUserByUsername(username);
        if (user != null) {
            userService.deleteDrawing(user, drawingID);
        }
        return "redirect:/home";
    }

    // ------------ Gallery Stuff -------------------

    @GetMapping("/gallery")
    public String GalleryController(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        User user = userService.getUserByUsername(username);

        if (user == null) {
            return "redirect:/login";
        }
        List<Drawings> gallery = galleryService.getGallery();
        String galleryJSON = new Gson().toJson(gallery);
        model.addAttribute("gallerydata", galleryJSON);

        return "gallery";
    }




}
