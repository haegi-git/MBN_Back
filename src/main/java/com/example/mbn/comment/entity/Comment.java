package com.example.mbn.comment.entity;

import com.example.mbn.posts.entity.Post;
import com.example.mbn.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Getter
@Table(name="comments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private boolean isEdited = false;
    private LocalDateTime editedAt;

    private boolean isHidden = false; // 👈 이거 추가

    private boolean isDeleted = false;

    private LocalDateTime createdAt;

    private int likeCount;
    private int reportCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;


    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // 필요하면 댓글 내용 업데이트 메서드도 따로 추가 가능
    public void updateContent(String newContent) {
        this.content = newContent;
        this.isEdited = true;
        this.editedAt = LocalDateTime.now();
    }

    public void markAsDeleted() {
        this.isDeleted = true;
    }
}
