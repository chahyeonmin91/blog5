package com.example.blog5.controller;

import com.example.blog5.model.Post;
import com.example.blog5.model.User;
import com.example.blog5.service.LikeService;
import com.example.blog5.service.PostService;
import com.example.blog5.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private PostService postService;

    @PostMapping("/{postId}")
    public String likePost(@PathVariable Long postId) {
        User user = getCurrentUser();
        Post post = postService.getPostById(postId);
        likeService.likePost(user, post);
        return "redirect:/posts/" + postId;
    }

    @DeleteMapping("/{postId}")
    public String unlikePost(@PathVariable Long postId) {
        User user = getCurrentUser();
        Post post = postService.getPostById(postId);
        likeService.unlikePost(user, post);
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/user")
    public String getLikedPosts(Model model) {
        User user = getCurrentUser();
        model.addAttribute("posts", likeService.getLikedPosts(user));
        return "likedPosts";
    }

    private User getCurrentUser() {
        return UserContext.getUser();
    }
}