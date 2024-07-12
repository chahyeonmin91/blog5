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
    List<Post> findAll();

    // 최신 글 가져오기
    List<Post> findAllByPublishedTrueOrderByCreatedAtDesc();

    // 인기 글 가져오기
    List<Post> findAllByPublishedTrueOrderByLikesCountDesc();

    // 임시 글 가져오기
    List<Post> findByBlogUserAndPublishedFalse(User user);
}