package com.example.blog5.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CookieAPITestController {
    @GetMapping("/api/sendCookie")
    public String createCookie(HttpServletRequest request) {
        // request로부터 auth라는 이름의 쿠키를 구한다.
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("auth")) {
                    return cookie.getValue();
                }
            }
        }
        return "no cookie";
    }
}
