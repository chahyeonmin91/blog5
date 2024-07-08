package com.example.blog5.service;


import com.example.blog5.model.Blog;
import com.example.blog5.model.User;
import com.example.blog5.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    public Blog getBlogByUser(User user) {
        return user.getBlog();
    }
}
