package com.example.blog5.service;


import com.example.blog5.model.Like;
import com.example.blog5.model.Post;
import com.example.blog5.model.User;
import com.example.blog5.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    public void likePost(User user, Post post) {
        if (likeRepository.findByUserAndPost(user, post) == null) {
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like);
        }
    }

    public void unlikePost(User user, Post post) {
        Like like = likeRepository.findByUserAndPost(user, post);
        if (like != null) {
            likeRepository.delete(like);
        }
    }

    public List<Post> getLikedPosts(User user) {
        return likeRepository.findByUser(user).stream().map(Like::getPost).collect(Collectors.toList());
    }

    public long getLikesCount(Post post) {
        return likeRepository.findByPost(post).size();
    }
}