package com.example.blog5.service;


import com.example.blog5.model.Follow;
import com.example.blog5.model.User;
import com.example.blog5.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    public void followUser(User follower, User followed) {
        if (followRepository.findByFollowerAndFollowed(follower, followed) == null) {
            Follow follow = new Follow();
            follow.setFollower(follower);
            follow.setFollowed(followed);
            followRepository.save(follow);
        }
    }

    public void unfollowUser(User follower, User followed) {
        Follow follow = followRepository.findByFollowerAndFollowed(follower, followed);
        if (follow != null) {
            followRepository.delete(follow);
        }
    }

    public List<User> getFollowedUsers(User follower) {
        return followRepository.findByFollower(follower).stream().map(Follow::getFollowed).collect(Collectors.toList());
    }

    public boolean isFollowing(User follower, User followed) {
        return followRepository.findByFollowerAndFollowed(follower, followed) != null;
    }
}