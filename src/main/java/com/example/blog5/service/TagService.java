package com.example.blog5.service;


import com.example.blog5.model.Tag;
import com.example.blog5.model.User;
import com.example.blog5.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public List<Tag> getTagsByUser(User user) {
        return tagRepository.findByUser(user);
    }
}
