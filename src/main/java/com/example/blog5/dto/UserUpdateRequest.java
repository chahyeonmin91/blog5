package com.example.blog5.dto;

import lombok.Data;

import java.util.Map;

@Data
public class UserUpdateRequest {
    private String name;
    private String email;
    private String profileImage;
    private String blogTitle;
    private Map<String, Boolean> emailNotification;
}
