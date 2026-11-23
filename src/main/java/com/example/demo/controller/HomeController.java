package com.example.demo.controller;

import com.example.demo.DTO.SharedDrawingDTO;
import com.example.demo.model.DrawingObject;
import com.example.demo.model.User;
import com.example.demo.services.DrawingObjectService;
import com.example.demo.services.DrawingService;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        User user = userService.getUserByUsername((String) session.getAttribute("username"));
        if (user == null) {
            return "redirect:/login";
        }
        List<DrawingObject> userDrawings = drawingObjectService.getUsersDrawingByID(user.getId());
        List<SharedDrawingDTO> userSharedDrawings = drawingObjectService.getDrawingsSharedByUser(user.getId());
        List<DrawingObject> userDrawingsBin = drawingObjectService.drawingsInTheBin(user.getId());

        System.out.println(user.getUsername() + " ID: " + user.getId());

        model.addAttribute("drawings", userDrawings);
        model.addAttribute("shared", userSharedDrawings);
        model.addAttribute("bin", userDrawingsBin);

        return "app/dashboard";
    }

    //----- restore
    @PostMapping("/restore")
    public String restoreDrawing(@RequestParam int drawingID, Model model, HttpSession session) {
        User user = userService.getUserByUsername((String) session.getAttribute("username"));
        if (user == null) return "redirect:/login";
        DrawingObject drawing = drawingObjectService.getDrawingByID(drawingID);
        if (drawing.getUser_id() != user.getId()) return "redirect:/error404";
        System.out.println("Checks pass");
        drawingObjectService.restoreDeletedDrawing(drawingID);
        System.out.println("Query successful");
        return "redirect:/app/dashboard";
    }


    //------- soft delete
    @PostMapping("/softdelete")
    public String softDeleteFunc(Model model, HttpSession session, @RequestParam int drawingID) {
        User user = userService.getUserByUsername((String) session.getAttribute("username"));
        if (user == null) return "redirect:/login";
        DrawingObject drawing = drawingObjectService.getDrawingByID(drawingID);
        if (drawing.getUser_id() != user.getId()) return "redirect:/error404";
        drawingObjectService.softDeleteDrawing(drawingID);
        return "redirect:/app/dashboard";
    }

    // ------ create new Drawing
    @PostMapping("/create")
    public String createNewDrawing(@RequestParam String drawingName, @RequestParam(defaultValue = "false") boolean status, HttpSession session) {
        User user = userService.getUserByUsername((String) session.getAttribute("username"));
        if (user == null) return "redirect:/login";
        if (drawingName.isBlank()) {
            drawingName = DrawingService.randomNameGenerator();
        }

        DrawingObject.Status visibility = DrawingObject.Status.PUBLIC;
        if (status) {
            visibility = DrawingObject.Status.PRIVATE;
        }
        drawingObjectService.createNewDrawing(user.getId(), drawingName, visibility);
        return "redirect:/app/dashboard";
    }
}
