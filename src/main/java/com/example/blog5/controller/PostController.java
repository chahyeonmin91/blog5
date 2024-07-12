package com.example.blog5.controller;

import com.example.blog5.model.Blog;
import com.example.blog5.model.Comment;
import com.example.blog5.model.Post;
import com.example.blog5.model.Series;
import com.example.blog5.model.User;
import com.example.blog5.service.*;
import com.example.blog5.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private SeriesService seriesService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/create")
    public String showCreatePostForm(Model model) {
        model.addAttribute("post", new Post());
        Blog blog = blogService.getBlogByUser(getCurrentUser());
        List<Series> seriesList = seriesService.getSeriesByBlog(blog);
        model.addAttribute("seriesList", seriesList);
        return "postForm";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute Post post, @RequestParam("publish") boolean publish,
                             @RequestParam("imageFiles") List<MultipartFile> imageFiles,
                             @RequestParam("previewImage") MultipartFile previewImageFile,
                             @RequestParam("urls") List<String> urls,
                             @RequestParam("seriesId") Long seriesId,
                             @RequestParam("isPublic") boolean isPublic) throws IOException {
        // 이미지와 URL 처리
        List<String> imagePaths = saveImages(imageFiles);
        post.setImages(imagePaths);
        post.setUrls(urls);

        // 미리보기 이미지 처리
        if (!previewImageFile.isEmpty()) {
            String previewImagePath = saveImage(previewImageFile);
            post.setPreviewImage(previewImagePath);
        }

        // 기본 설정
        post.setCreatedAt(LocalDateTime.now());
        post.setPublished(publish);
        post.setPublic(isPublic);

        Blog blog = blogService.getBlogByUser(getCurrentUser());
        post.setBlog(blog);

        if (seriesId != null) {
            Series series = seriesService.getSeriesById(seriesId);
            post.setSeries(series);
        }

        postService.savePost(post);
        return "redirect:/posts/drafts";
    }

    @GetMapping("/publish/{postId}")
    public String showPublishPostForm(@PathVariable Long postId, Model model) {
        Post post = postService.getPostById(postId);
        model.addAttribute("post", post);
        Blog blog = blogService.getBlogByUser(getCurrentUser());
        List<Series> seriesList = seriesService.getSeriesByBlog(blog);
        model.addAttribute("seriesList", seriesList);
        return "publishPost";
    }

    @PostMapping("/publish/{postId}")
    public String publishPost(@PathVariable Long postId, @ModelAttribute Post post,
                              @RequestParam("previewImage") MultipartFile previewImageFile,
                              @RequestParam("isPublic") boolean isPublic,
                              @RequestParam("seriesId") Long seriesId) throws IOException {
        Post existingPost = postService.getPostById(postId);

        // 업데이트
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setTags(post.getTags());
        existingPost.setPublished(true);
        existingPost.setPublic(isPublic);

        if (!previewImageFile.isEmpty()) {
            String previewImagePath = saveImage(previewImageFile);
            existingPost.setPreviewImage(previewImagePath);
        }

        if (seriesId != null) {
            Series series = seriesService.getSeriesById(seriesId);
            existingPost.setSeries(series);
        }

        postService.savePost(existingPost);
        return "redirect:/posts/drafts";
    }

    @GetMapping("/{postId}")
    public String viewPost(@PathVariable Long postId, Model model) {
        User currentUser = getCurrentUser();
        Post post = postService.getPostById(postId);
        List<Post> previousAndNextPosts = postService.getPreviousAndNextPosts(postId, post.getBlog());
        boolean isOwner = post.getBlog().getUser().equals(currentUser);

        model.addAttribute("post", post);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("previousPost", previousAndNextPosts.get(0));
        model.addAttribute("nextPost", previousAndNextPosts.get(1));

        if (!isOwner) {
            model.addAttribute("canLike", true);
            model.addAttribute("canFollow", !followService.isFollowing(currentUser, post.getBlog().getUser()));
        } else {
            model.addAttribute("statistics", getStatistics(post));
        }

        return "viewPost";
    }

    // 최신 글/인기 글 목록 조회 기능 추가
    @GetMapping
    public ResponseEntity<?> getPosts(@RequestParam(defaultValue = "latest") String sort) {
        List<Post> posts;
        if ("popular".equals(sort)) {
            posts = postService.getPopularPosts();
        } else {
            posts = postService.getLatestPosts();
        }

        List<Map<String, Object>> response = posts.stream().map(post -> Map.of(
                "postId", post.getId(),
                "title", post.getTitle(),
                "excerpt", post.getContent().substring(0, Math.min(post.getContent().length(), 100)),
                "author", Map.of(
                        "username", post.getBlog().getUser().getUsername(),
                        "profileImage", post.getBlog().getUser().getProfileImage()
                ),
                "likes", post.getLikesCount()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // 좋아요 기능 추가
    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId) {
        User currentUser = getCurrentUser();
        Post post = postService.getPostById(postId);

        if (post == null) {
            return ResponseEntity.status(400).body(Map.of("error", "잘못된 요청입니다."));
        }

        likeService.likePost(currentUser, post);
        return ResponseEntity.ok(Map.of("message", "좋아요가 추가되었습니다."));
    }

    // 댓글 작성 기능 추가
    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long postId, @RequestBody Map<String, String> request) {
        String content = request.get("content");
        Post post = postService.getPostById(postId);
        User user = getCurrentUser();

        if (post == null || content == null || content.isEmpty()) {
            return ResponseEntity.status(400).body(Map.of("error", "잘못된 요청입니다."));
        }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        Comment savedComment = commentService.saveComment(comment);
        return ResponseEntity.ok(Map.of(
                "message", "댓글 작성이 완료되었습니다.",
                "commentId", savedComment.getId()
        ));
    }

    // 댓글 삭제 기능 추가
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        Post post = postService.getPostById(postId);
        if (post == null || commentService.getCommentById(commentId) == null) {
            return ResponseEntity.status(400).body(Map.of("error", "잘못된 요청입니다."));
        }

        commentService.deleteComment(commentId);
        return ResponseEntity.ok(Map.of("message", "댓글 삭제가 완료되었습니다."));
    }

    @PostMapping("/follow/{userId}")
    public String followUser(@PathVariable Long userId) {
        User currentUser = getCurrentUser();
        User userToFollow = userService.getUserById(userId);
        followService.followUser(currentUser, userToFollow);
        return "redirect:/posts/user/" + userToFollow.getUsername();
    }

    @GetMapping("/edit/{postId}")
    public String editPost(@PathVariable Long postId, Model model) {
        Post post = postService.getPostById(postId);
        model.addAttribute("post", post);
        return "editPost";
    }

    @PostMapping("/edit/{postId}")
    public String updatePost(@PathVariable Long postId, @ModelAttribute Post post) {
        Post existingPost = postService.getPostById(postId);
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        postService.savePost(existingPost);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/delete/{postId}")
    public String deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return "redirect:/";
    }

    private List<String> saveImages(List<MultipartFile> imageFiles) throws IOException {
        // 이미지 저장 로직
        // 예시로 이미지 경로를 반환
        return imageFiles.stream().map(file -> "/path/to/" + file.getOriginalFilename()).collect(Collectors.toList());
    }

    private String saveImage(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        Path path = Paths.get("uploads/" + filename);
        Files.write(path, file.getBytes());
        return "/uploads/" + filename;
    }

    private User getCurrentUser() {
        return UserContext.getUser();
    }

    private Map<String, Object> getStatistics(Post post) {
        // 통계 정보를 반환하는 로직
        return Map.of("views", 100, "likes", post.getLikesCount());
    }
}
