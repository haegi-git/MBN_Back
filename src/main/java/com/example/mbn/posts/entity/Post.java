package com.example.mbn.posts.entity;

import com.example.mbn.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> images = new ArrayList<>();


    private int likeCount = 0;
    private int viewCount = 0;
    private int reportCount = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 글 작성 시간

    // 이 메서드는 'Post' 객체를 생성할 때 작성 시간을 자동으로 설정해줌
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 작성자

    public void setUser(User user) {
        this.user = user;
    }

    public void update(String title, String content, String platform, String tag) {
        this.title = title;
        this.content = content;
        this.platform = platform;
        this.tag = tag;
    }
    public void increaseViewCount() {
        this.viewCount++;
    }

    public void increaseLike() {
        this.likeCount++;
    }

    public void decreaseLike() {
        if (this.likeCount > 0) this.likeCount--;
    }

}
