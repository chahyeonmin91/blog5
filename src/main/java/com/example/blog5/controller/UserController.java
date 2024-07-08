package com.example.blog5.controller;


import com.example.blog5.model.User;
import com.example.blog5.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/profile")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showProfile(Model model) {
        User user = getCurrentUser();
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/image")
    public String updateProfileImage(@RequestParam("image") MultipartFile file) throws IOException {
        User user = getCurrentUser();
        userService.updateProfileImage(user, file);
        return "redirect:/profile";
    }

    @DeleteMapping("/image")
    public String deleteProfileImage() throws IOException {
        User user = getCurrentUser();
        userService.deleteProfileImage(user);
        return "redirect:/profile";
    }

    @PostMapping("/email")
    public String updateEmail(@RequestParam("email") String email) {
        User user = getCurrentUser();
        userService.updateEmail(user, email);
        return "redirect:/profile";
    }

    @PostMapping("/notifications")
    public String updateEmailNotifications(@RequestParam Map<String, Boolean> notifications) {
        User user = getCurrentUser();
        userService.updateEmailNotifications(user, notifications);
        return "redirect:/profile";
    }

    @PostMapping("/delete")
    public String deleteUser() {
        User user = getCurrentUser();
        userService.deleteUser(user);
        return "redirect:/logout";
    }

    private User getCurrentUser() {
        // 현재 로그인한 사용자 정보를 반환
        return new User(); // 예시로 빈 사용자 반환
    }
}
