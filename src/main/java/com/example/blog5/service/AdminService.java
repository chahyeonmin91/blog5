package com.example.blog5.service;

import com.example.blog5.model.Post;
import com.example.blog5.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private PostRepository postRepository;

    // 모든 글 목록 조회 로직
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 글 삭제 로직
    public boolean deletePost(Long postId) {
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
            return true;
        }
        return false;
    }
}
