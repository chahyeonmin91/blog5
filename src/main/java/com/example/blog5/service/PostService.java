package com.example.blog5.service;

import com.example.blog5.model.Blog;
import com.example.blog5.model.Post;
import com.example.blog5.model.User;
import com.example.blog5.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getDraftPosts(Blog blog) {
        return postRepository.findByBlogAndPublished(blog, false);
    }

    public List<Post> getPublishedPosts(Blog blog) {
        return postRepository.findByBlogAndPublishedOrderByCreatedAtDesc(blog, true);
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public List<Post> getPostsByUser(User user) {
        return postRepository.findByBlogUser(user);
    }

    public List<Post> getPreviousAndNextPosts(Long postId, Blog blog) {
        Post post = getPostById(postId);
        List<Post> posts = postRepository.findByBlogAndPublishedOrderByCreatedAtAsc(blog, true);
        int index = posts.indexOf(post);
        return List.of(
                index > 0 ? posts.get(index - 1) : null,
                index < posts.size() - 1 ? posts.get(index + 1) : null
        );
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 최신 글 가져오기
    public List<Post> getLatestPosts() {
        return postRepository.findAllByPublishedTrueOrderByCreatedAtDesc();
    }

    // 인기 글 가져오기
    public List<Post> getPopularPosts() {
        return postRepository.findAllByPublishedTrueOrderByLikesCountDesc();
    }
}
