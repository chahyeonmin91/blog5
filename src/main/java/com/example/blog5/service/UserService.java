package com.example.blog5.service;

import com.example.blog5.model.Blog;
import com.example.blog5.model.User;
import com.example.blog5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateProfileImage(User user, MultipartFile file) throws IOException {
        String filename = user.getUsername() + "_" + file.getOriginalFilename();
        Path path = Paths.get("uploads/" + filename);
        Files.write(path, file.getBytes());

        user.setProfileImage("/uploads/" + filename);
        return userRepository.save(user);
    }

    public User deleteProfileImage(User user) throws IOException {
        if (user.getProfileImage() != null) {
            Path path = Paths.get("uploads/" + user.getProfileImage());
            Files.deleteIfExists(path);
            user.setProfileImage(null);
        }
        return userRepository.save(user);
    }

    public User updateEmail(User user, String email) {
        user.setEmail(email);
        return userRepository.save(user);
    }

    public User updateEmailNotifications(User user, Map<String, Boolean> notifications) {
        user.setEmailNotifications(notifications);
        return userRepository.save(user);
    }

    public boolean deleteUserWithPasswordCheck(Long userId, String password) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    public boolean updateUser(Long userId, String name, String email, String profileImage, String blogTitle, Map<String, Boolean> emailNotification) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setUsername(name);
            user.setEmail(email);
            user.setProfileImage(profileImage);

            Blog blog = user.getBlog();
            if (blog != null) {
                blog.setTitle(blogTitle);
            }

            user.setEmailNotifications(emailNotification);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
