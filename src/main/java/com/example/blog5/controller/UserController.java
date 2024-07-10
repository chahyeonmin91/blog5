package com.example.blog5.controller;

import com.example.blog5.model.User;
import com.example.blog5.service.UserService;
import com.example.blog5.util.UserContext;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/profile")
@RestController
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
    @GetMapping("/login")
    public String showLoginForm() {
        log.info("s안아");
        return "login"; // 로그인 폼 HTML 페이지 반환
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username, @RequestParam String password, HttpSession session) {
        // 로그인 검증 로직
        // 성공 시 세션에 사용자 정보 저장 등
        return "redirect:/main"; // 로그인 성공 후 리다이렉트 할 페이지
    }

//    @GetMapping("/user-info")
//        public String getUserInfo() {
//            String userId = UserContext.getUser();
//            if (userId != null) {
//                return "User ID: " + userId;
//            } else {
//                return "No user authenticated";
//            }
//        }

    }