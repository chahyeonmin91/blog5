package com.example.blog5.repository;


import com.example.blog5.model.Tag;
import com.example.blog5.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByUser(User user);
}
