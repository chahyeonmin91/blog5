package com.example.blog5.controller;

import com.example.blog5.model.Post;
import com.example.blog5.service.AdminService;
import com.example.blog5.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private PostService postService;

    @Autowired
    private AdminService adminService;

    // 모든 글 목록 조회
    @GetMapping("/posts")
    public ResponseEntity<List<Map<String, Object>>> getAllPosts() {
        List<Post> posts = adminService.getAllPosts();
        List<Map<String, Object>> response = posts.stream().map(post -> Map.of(
                "postId", post.getId(),
                "title", post.getTitle(),
                "author", Map.of(
                        "username", post.getBlog().getUser().getUsername(),
                        "profileImage", post.getBlog().getUser().getProfileImage()
                ),
                "isPublished", post.isPublished(),
                "createdAt", post.getCreatedAt()
        )).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // 글 삭제 (관리자 권한)
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        boolean isDeleted = adminService.deletePost(postId);
        if (isDeleted) {
            return ResponseEntity.ok(Map.of("message", "글 삭제가 완료되었습니다."));
        } else {
            return ResponseEntity.status(400).body(Map.of("error", "잘못된 요청입니다."));
        }
    }

    @GetMapping("/posts")
    public String getAllPostsView(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "admin";
    }

    @PostMapping("/delete/{postId}")
    public String deletePostView(@PathVariable Long postId) {
        postService.deletePost(postId);
        return "redirect:/admin/posts";
    }
}
