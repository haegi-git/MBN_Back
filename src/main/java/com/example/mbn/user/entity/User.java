package com.example.mbn.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String socialId;

    @Column(nullable = false)
    private String provider; // kakao, google 등

    @Column(unique = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    private String profileImageUrl;
}
