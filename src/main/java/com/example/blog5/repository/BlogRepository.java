package com.example.blog5.repository;

import com.example.blog5.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long> {
}
