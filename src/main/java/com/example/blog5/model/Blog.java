package com.example.blog5.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    private List<Post> posts;

    @Column
    private String introduction; // 추가된 필드

    // 시리즈 목록을 반환하는 메서드 추가
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    private List<Series> series;

    public List<Series> getSeries() {
        return series;
    }
}
