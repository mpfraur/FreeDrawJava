package com.example.demo.controller;

import com.example.demo.DTO.SharedDrawingDTO;
import com.example.demo.DTO.SharedUserDTO;
import com.example.demo.model.DrawingObject;
import com.example.demo.model.Drawing_versionObject;
import com.example.demo.model.PermissionsObject;
import com.example.demo.model.User;
import com.example.demo.repository.DrawingsRepositoryImpl;
import com.example.demo.services.DrawingObjectService;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/settings")
public class DrawingSettingsController {

    @Autowired
    UserService userService;

    @Autowired
    DrawingObjectService drawingObjectService;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(DrawingSettingsController.class);

    // -------------- Settings per Drawing
    @GetMapping("/{drawingID}")
    public String openDrawingSettings(@PathVariable int drawingID, HttpSession session, Model model) {
        User user = userService.getUserByUsername((String) session.getAttribute("username"));
        if (user == null) return "redirect:/login";
        DrawingObject drawing = drawingObjectService.getDrawingByID(drawingID);
        if (drawing.getUser_id() != user.getId()) return "redirect:/error404";
        model.addAttribute("drawing", drawing);

        List<SharedUserDTO> sharedUserList = drawingObjectService.getPermissionByDrawingID(drawingID);
        List<Drawing_versionObject> versions = drawingObjectService.getAllDrawingVersions(drawingID);

        model.addAttribute("sharedUsers", sharedUserList);
        model.addAttribute("versions", versions);
        return "settings/settings";
    }

    // ------- create new Permission
    @PostMapping("/addpermission")
    public String createNewPermission(HttpSession session, @RequestParam int drawingId, @RequestParam String username, @RequestParam boolean permission, Model model) {
        DrawingsRepositoryImpl.PermissionType permType;
        User user = userService.getUserByUsername(username);
        User owner = userService.getUserByUsername((String) session.getAttribute("username"));
        DrawingObject drawing = drawingObjectService.getDrawingByID(drawingId);
        if (owner == null) {
            return "redirect:/login";
        }
        if (user == null) {
            model.addAttribute("error", "Username " + username + " Does not exist!");
            return "redirect:/settings/" + drawingId;
        }
        if (drawing == null || drawing.getUser_id() != owner.getId()) return "redirect:/error404";
        if (permission) {
            permType = DrawingsRepositoryImpl.PermissionType.WRITE;
        } else {
            permType = DrawingsRepositoryImpl.PermissionType.READ;
        }
        drawingObjectService.createNewPermission(username, drawingId, owner.getId(), permType);
        return "redirect:/settings/" + drawingId;
    }


    // ----------- Revoke Permission
    @PostMapping("/revoke")
    public String revokePermissionController(@RequestParam int drawingId, @RequestParam int userId, HttpSession session) {
        User owner = userService.getUserByUsername((String) session.getAttribute("username"));
        DrawingObject drawing = drawingObjectService.getDrawingByID(drawingId);
        if (owner == null) {
            return "redirect:/login";
        }
        if (drawing == null || drawing.getUser_id() != owner.getId()) return "redirect:/error404";
        drawingObjectService.revokeUserPermission(userId, drawingId);
        logger.info("User : {} Got his permissions Revoked.", userId);
        return "redirect:/settings/" + drawingId;
    }

    // ----------- Creates a new Drawing copy off a version!
    @PostMapping("/copy")
    public String createCopyDrawingController(HttpSession session, @RequestParam int drawingId, @RequestParam(defaultValue = "false") boolean isPrivate, @RequestParam int versionID, @RequestParam String jsonData) {
        User user = userService.getUserByUsername((String) session.getAttribute("username"));
        DrawingObject drawing = drawingObjectService.getDrawingByID(drawingId);
        Drawing_versionObject version = drawingObjectService.getVersionByID(versionID, drawingId);
        if (version == null || user == null || drawing == null) return "redirect:/dashboard";
        DrawingObject.Status status = DrawingObject.Status.PUBLIC;
        if (isPrivate) {
            status = DrawingObject.Status.PRIVATE;
        }
        logger.info("isPrivate received: {}", isPrivate);
        logger.info("status assigned: {}", status);
        DrawingObject copyOfDrawing = drawingObjectService.createNewDrawingFromPrevDrawing(versionID, drawingId, user.getId(), status);

        if (copyOfDrawing != null) {
            drawingObjectService.createNewVersion(copyOfDrawing.getDrawing_id(), jsonData);
        } else {
            logger.error("Error copy of Drawing version is null");
        }

        logger.info("User : {} Has Copied {}", user.getUsername(), drawing.getName());


        return "redirect:/app/dashboard";
    }

    // ----------- HARD delete Drawing


}
