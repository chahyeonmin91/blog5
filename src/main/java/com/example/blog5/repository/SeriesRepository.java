package com.example.blog5.repository;


import com.example.blog5.model.Blog;
import com.example.blog5.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeriesRepository extends JpaRepository<Series, Long> {
    List<Series> findByBlog(Blog blog);
}
