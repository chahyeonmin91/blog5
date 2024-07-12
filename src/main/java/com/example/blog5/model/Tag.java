package com.example.blog5.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int count;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Getter for count
    public int getCount() {
        return count;
    }
}
