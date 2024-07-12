package com.example.blog5.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ElementCollection
    private List<String> tags;

    private LocalDateTime createdAt;

    private boolean published;

    private boolean isPublic;

    private String previewImage;

    @ManyToOne
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    @ElementCollection
    private List<String> images;

    @ElementCollection
    private List<String> urls;

    @ManyToOne
    @JoinColumn(name = "series_id")
    private Series series;

    // 좋아요 수를 추적하는 필드 추가
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Like> likes;

    // 좋아요 수를 반환하는 메서드
    public int getLikesCount() {
        return likes != null ? likes.size() : 0;
    }
}
