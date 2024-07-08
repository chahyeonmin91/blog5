package com.example.blog5.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    private User followed;

}
