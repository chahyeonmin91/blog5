package com.example.blog5.repository;


import com.example.blog5.model.Blog;
import com.example.blog5.model.Post;
import com.example.blog5.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByBlogAndPublishedOrderByCreatedAtAsc(Blog blog, boolean published);
    List<Post> findByBlogUser(User user);
    List<Post> findByBlogAndPublishedOrderByCreatedAtDesc(Blog blog, boolean published);
    List<Post> findByBlogAndPublished(Blog blog, boolean published);
    List<Post> findAll();  // 추가된 메서드
}