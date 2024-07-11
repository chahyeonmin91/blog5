package com.example.blog5.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Blog blog;

    @Column
    private String profileImage;

    @ElementCollection
    @CollectionTable(name = "email_notifications", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "enabled")
    @MapKeyColumn(name = "type")
    private Map<String, Boolean> emailNotifications;

    // 새로운 필드 추가
    @Column
    private String introduction;

    // 새로운 메서드 추가
    public String getIntroduction() {
        return introduction;
    }
}
