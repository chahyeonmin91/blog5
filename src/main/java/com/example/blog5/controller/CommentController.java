package com.example.blog5.controller;


import com.example.blog5.model.Comment;
import com.example.blog5.model.Post;
import com.example.blog5.model.User;
import com.example.blog5.service.CommentService;
import com.example.blog5.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @PostMapping("/add")
    public String addComment(@RequestParam Long postId, @RequestParam String content, @RequestParam(required = false) Long parentId) {
        Post post = postService.getPostById(postId);
        User user = getCurrentUser();
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        if (parentId != null) {
            Comment parent = commentService.getCommentById(parentId);
            comment.setParent(parent);
        }

        commentService.saveComment(comment);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable Long commentId, @RequestParam Long postId) {
        commentService.deleteComment(commentId);
        return "redirect:/posts/" + postId;
    }

    private User getCurrentUser() {
        // 현재 로그인한 사용자 정보를 반환
        return new User(); // 예시로 빈 사용자 반환
    }
}
