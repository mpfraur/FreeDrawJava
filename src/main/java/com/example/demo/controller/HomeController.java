package com.example.demo.controller;

import com.example.demo.DTO.SharedDrawingDTO;
import com.example.demo.model.DrawingObject;
import com.example.demo.model.User;
import com.example.demo.services.DrawingObjectService;
import com.example.demo.services.DrawingService;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/app")
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private DrawingObjectService drawingObjectService;

    @GetMapping("/dashboard")
    public String getDashboard(Model model, HttpSession session) {
        User user = userService.getUserByUsername(session.getAttribute("user").toString());
        if (user == null) {
            return "redirect:/login";
        }
        List<DrawingObject> userDrawings = drawingObjectService.getUsersDrawingByID(user.getId());
        List<SharedDrawingDTO> userSharedDrawings = drawingObjectService.getDrawingsSharedByUser(user.getId());
        List<DrawingObject> userDrawingsBin = drawingObjectService.drawingsInTheBin(user.getId());

        model.addAttribute("drawings", userDrawings);
        model.addAttribute("shared", userSharedDrawings);
        model.addAttribute("bin", userDrawingsBin);

        return "app/dashboard";
    }
}
