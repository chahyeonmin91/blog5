package com.example.blog5.controller;

import com.example.blog5.model.User;
import com.example.blog5.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/@{username}")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private PostService postService;

    @Autowired
    private SeriesService seriesService;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService; // 추가된 부분

    @GetMapping
    public String showUserBlog(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("tags", tagService.getTagsByUser(user));
        model.addAttribute("posts", postService.getPostsByUser(user));
        model.addAttribute("series", seriesService.getSeriesByUser(user));
        return "userBlog";
    }
}
