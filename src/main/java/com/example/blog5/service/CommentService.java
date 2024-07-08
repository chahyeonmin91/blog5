package com.example.blog5.service;


import com.example.blog5.model.Comment;
import com.example.blog5.model.Post;
import com.example.blog5.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPost(Post post) {
        return commentRepository.findByPostAndParentIsNull(post);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }
}
