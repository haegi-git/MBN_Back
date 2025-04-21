package com.example.mbn.comment.dto;

import com.example.mbn.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String nickname;
    private String profileImageUrl;
    private boolean isEdited;
    private boolean isHidden;
    private boolean isDeleted;
    private LocalDateTime createdAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.nickname = comment.getUser().getNickname();
        this.profileImageUrl = comment.getUser().getProfileImageUrl();
        this.isEdited = comment.isEdited();
        this.isHidden = comment.isHidden();
        this.isDeleted = comment.isDeleted();
        this.createdAt = comment.getCreatedAt();
    }
}
