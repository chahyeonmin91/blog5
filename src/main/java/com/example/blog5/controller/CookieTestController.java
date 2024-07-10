package com.example.blog5.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CookieTestController {
    @GetMapping("/createCookie")
    public String createCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("auth", "carami");
        cookie.setPath("/");

        response.addCookie(cookie);
        return "createCookie";
    }
}
