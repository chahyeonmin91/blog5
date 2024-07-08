package com.example.blog5.repository;


import com.example.blog5.model.Like;
import com.example.blog5.model.Post;
import com.example.blog5.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByUser(User user);
    List<Like> findByPost(Post post);
    Like findByUserAndPost(User user, Post post);
}
