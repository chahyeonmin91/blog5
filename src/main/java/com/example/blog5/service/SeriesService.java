package com.example.blog5.service;


import com.example.blog5.model.Blog;
import com.example.blog5.model.Series;
import com.example.blog5.model.User;
import com.example.blog5.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}