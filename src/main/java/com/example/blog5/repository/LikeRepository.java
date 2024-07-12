package com.example.blog5.repository;


import com.example.blog5.model.Like;
import com.example.blog5.model.Post;
import com.example.blog5.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByUser(User user);
    List<Like> findByPost(Post post);
    Like findByUserAndPost(User user, Post post);

    // 특정 게시물의 좋아요 수를 계산하는 메서드
    long countByPost(Post post);

    // 인기 게시물을 좋아요 수 기준으로 가져오는 메서드
    @Query("SELECT p FROM Post p WHERE p.published = true ORDER BY (SELECT COUNT(l) FROM Like l WHERE l.post = p) DESC")
    List<Post> findPopularPosts();
}
