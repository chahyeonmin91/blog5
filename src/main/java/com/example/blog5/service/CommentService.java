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

    // 댓글 저장 로직
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    // 특정 게시물의 댓글 조회 로직
    public List<Comment> getCommentsByPost(Post post) {
        return commentRepository.findByPostAndParentIsNull(post);
    }

    // 댓글 삭제 로직
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    // 특정 댓글 조회 로직
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }
}
