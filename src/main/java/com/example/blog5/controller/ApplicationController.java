package com.example.blog5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ApplicationController {

    @GetMapping("/")
    public String root() {
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/userreg")
    public String registerUser(Model model) {
        // 회원가입 로직 구현
        // 성공하면 "회원 가입이 성공했습니다", 실패하면 "회원 가입에 실패했습니다" 메시지를 모델에 추가
        boolean registrationSuccessful = true; // 예시로 성공 여부 결정
        if (registrationSuccessful) {
            model.addAttribute("message", "회원 가입이 성공했습니다");
        } else {
            model.addAttribute("message", "회원 가입에 실패했습니다");
        }
        return "userreg";
    }



    // 추가적인 매핑이 필요한 경우 여기에 작성
}
