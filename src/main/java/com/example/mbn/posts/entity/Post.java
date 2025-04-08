package com.example.mbn.posts.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    private String tag; // 말머리
    private String platform; // PS, Steam 등

    private int likeCount = 0;
    private int viewCount = 0;
    private int reportCount = 0;

//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user; // 작성자
}
