package com.example.blog5.service;

import com.example.blog5.model.Blog;
import com.example.blog5.model.Series;
import com.example.blog5.model.User;
import com.example.blog5.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SeriesService {

    @Autowired
    private SeriesRepository seriesRepository;

    public List<Series> getSeriesByBlog(Blog blog) {
        return seriesRepository.findByBlog(blog);
    }

    public Series getSeriesById(Long seriesId) {
        return seriesRepository.findById(seriesId).orElse(null);
    }

    public List<Series> getSeriesByUser(User user) {
        return seriesRepository.findByBlog(user.getBlog());
    }

    // 수정된 메서드
    public List<Map<String, Object>> getSeriesWithPostsByUser(User user) {
        return seriesRepository.findByBlog(user.getBlog()).stream()
                .map(series -> Map.of(
                        "seriesId", series.getId().toString(),
                        "title", series.getTitle(),
                        "posts", series.getPosts().stream()
                                .map(post -> Map.of(
                                        "postId", post.getId().toString(),
                                        "title", post.getTitle()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
}
