package com.example.blog5.service;

import com.example.blog5.model.Blog;
import com.example.blog5.model.Post;
import com.example.blog5.model.User;
import com.example.blog5.repository.PostRepository;
import com.example.blog5.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getDraftPosts(Blog blog) {
        return postRepository.findByBlogAndPublished(blog, false);
    }

    public List<Post> getPublishedPosts(Blog blog) {
        return postRepository.findByBlogAndPublishedOrderByCreatedAtDesc(blog, true);
    }

    public boolean deletePost(Long postId) {
        if (postRepository.existsById(postId)) {
            postRepository.deleteById(postId);
            return true;
        }
        return false;
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

    public List<Post> getPosts(String sort) {
        List<Post> posts = postRepository.findAll();
        if ("popular".equalsIgnoreCase(sort)) {
            posts = posts.stream()
                    .sorted(Comparator.comparingInt(Post::getLikesCount).reversed())
                    .collect(Collectors.toList());
        } else {
            posts = posts.stream()
                    .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                    .collect(Collectors.toList());
        }
        return posts;
    }

    public Blog getBlogByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user != null ? user.getBlog() : null;
    }
}
