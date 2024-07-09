package com.example.blog5.repository;


import com.example.blog5.model.Comment;
import com.example.blog5.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
    List<Comment> findByPostAndParentIsNull(Post post);
    List<Comment> findByParent(Comment parent);
}