package com.example.demo.controller;

import com.example.demo.DTO.UserDrawingDTO;
import com.example.demo.model.DrawingObject;
import com.example.demo.model.Drawing_versionObject;
import com.example.demo.model.User;
import com.example.demo.services.DrawingObjectService;
import com.example.demo.services.DrawingService;
import com.example.demo.services.GalleryService;
import com.example.demo.services.UserService;
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

    @Autowired
    private DrawingObjectService drawingObjectService;

    @GetMapping("/list")
    public String listAllUserDrawings(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return "redirect:/home";
        }

        List<DrawingObject> otherUsersDrawings = drawingObjectService.fetchAllPublicDrawings();
        List<UserDrawingDTO> userDrawingDTOlist = drawingObjectService.parseDTODrawingList(otherUsersDrawings);
        model.addAttribute("drawingsList", userDrawingDTOlist);
        model.addAttribute("amountOfDraws", userDrawingDTOlist);
        model.addAttribute("ownerUsername", userDrawingDTOlist);
        return "galleries/list";
    }

    @GetMapping("/canvaview/{id}")
    public String getDrawingController(HttpSession session, @PathVariable("id") int id, Model model) {
        String username = (String) session.getAttribute("username");
        User user = userService.getUserByUsername(username);
        DrawingObject fetchedDrawing = drawingObjectService.getDrawingByID(id);
        if (fetchedDrawing == null) return "redirect:/home";
        Drawing_versionObject version = drawingObjectService.fetchLatestVersion(id);
        boolean authorized = fetchedDrawing.getUser_id() == user.getId() || drawingObjectService.checkForPermissionReadByUser(user.getId(), fetchedDrawing.getDrawing_id()) || fetchedDrawing.getStatus().name().equals("PUBLIC");
        if (!authorized) {
            System.out.println("User : " + user.getUsername() + " WAS NOT authorized for Drawing ID : " + fetchedDrawing.getDrawing_id());
            return "redirect:/home";
        }

        String jsonDataDrawing = "";

        if (version != null && version.getDrawing_data() != null) {
            jsonDataDrawing = version.getDrawing_data();
        }

        model.addAttribute("json_data_view", jsonDataDrawing);
        model.addAttribute("drawingID", id);
        return "galleries/canvaview";
    }


}
