package com.example.blog5.controller;

import com.example.blog5.dto.UserUpdateRequest;
import com.example.blog5.dto.PasswordRequest;
import com.example.blog5.model.User;
import com.example.blog5.repository.UserRepository;
import com.example.blog5.service.UserService;
import com.example.blog5.service.FollowService;
import com.example.blog5.util.UserContext;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<User> showProfile() {
        User user = getCurrentUser();
        return ResponseEntity.ok(user);
    }

    @PostMapping("/image")
    public ResponseEntity<User> updateProfileImage(@RequestParam("image") MultipartFile file) throws IOException {
        User user = getCurrentUser();
        userService.updateProfileImage(user, file);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/image")
    public ResponseEntity<User> deleteProfileImage() throws IOException {
        User user = getCurrentUser();
        userService.deleteProfileImage(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/email")
    public ResponseEntity<User> updateEmail(@RequestParam("email") String email) {
        User user = getCurrentUser();
        userService.updateEmail(user, email);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/notifications")
    public ResponseEntity<User> updateEmailNotifications(@RequestParam Map<String, Boolean> notifications) {
        User user = getCurrentUser();
        userService.updateEmailNotifications(user, notifications);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, @RequestBody PasswordRequest request) {
        boolean isDeleted = userService.deleteUserWithPasswordCheck(userId, request.getPassword());
        if (isDeleted) {
            return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 완료되었습니다."));
        } else {
            return ResponseEntity.status(400).body(Map.of("error", "비밀번호가 잘못되었습니다."));
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest request) {
        boolean isUpdated = userService.updateUser(userId, request.getName(), request.getEmail(), request.getProfileImage(), request.getBlogTitle(), request.getEmailNotification());

        if (isUpdated) {
            return ResponseEntity.ok(Map.of("message", "회원 정보가 수정되었습니다."));
        } else {
            return ResponseEntity.status(400).body(Map.of("error", "잘못된 요청입니다."));
        }
    }

    // 사용자 팔로우
    @PostMapping("/{username}/follow")
    public ResponseEntity<?> followUser(@PathVariable String username) {
        User currentUser = getCurrentUser();
        User userToFollow = userService.findByUsername(username);

        if (userToFollow == null || currentUser.equals(userToFollow)) {
            return ResponseEntity.status(400).body(Map.of("error", "잘못된 요청입니다."));
        }

        followService.followUser(currentUser, userToFollow);
        return ResponseEntity.ok(Map.of("message", "팔로우가 완료되었습니다."));
    }

    // 팔로우 취소
    @PostMapping("/{username}/unfollow")
    public ResponseEntity<?> unfollowUser(@PathVariable String username) {
        User currentUser = getCurrentUser();
        User userToUnfollow = userService.findByUsername(username);

        if (userToUnfollow == null || currentUser.equals(userToUnfollow)) {
            return ResponseEntity.status(400).body(Map.of("error", "잘못된 요청입니다."));
        }

        followService.unfollowUser(currentUser, userToUnfollow);
        return ResponseEntity.ok(Map.of("message", "언팔로우가 완료되었습니다."));
    }

    // 팔로우 목록 조회
    @GetMapping("/followers")
    public ResponseEntity<List<Map<String, Object>>> getFollowers() {
        User currentUser = getCurrentUser();
        List<User> followers = followService.getFollowedUsers(currentUser);

        List<Map<String, Object>> response = followers.stream().map(follower -> Map.of(
                "userId", (Object) follower.getUserid(),
                "username", (Object) follower.getUsername(),
                "profileImage", (Object) follower.getProfileImage()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    private User getCurrentUser() {
        return UserContext.getUser();
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

    @GetMapping("/user-info")
    public String getUserInfo() {
        User user = UserContext.getUser();
        if (user != null) {
            return "User ID: " + user;
        } else {
            return "No user authenticated";
        }
    }
}
