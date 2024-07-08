package com.example.blog5.repository;


import com.example.blog5.model.Follow;
import com.example.blog5.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(User follower);
    List<Follow> findByFollowed(User followed);
    Follow findByFollowerAndFollowed(User follower, User followed);
}
