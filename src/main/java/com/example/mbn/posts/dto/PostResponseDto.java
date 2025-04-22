package com.example.mbn.posts.dto;

import com.example.mbn.posts.entity.Post;
import com.example.mbn.posts.entity.PostImage;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private Long authorId;

    private String title;
    private String content;

    private String nickname;
    private String profileImageUrl;

    private String category;

    private List<String> imageUrls;

    private int likeCount = 0;
    private int viewCount = 0;
    private int reportCount = 0;

    private LocalDateTime createdAt;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.authorId = post.getUser().getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.likeCount = post.getLikeCount();
        this.viewCount = post.getViewCount();
        this.reportCount = post.getReportCount();
        this.createdAt = post.getCreatedAt();
        this.imageUrls = post.getImages().stream()
                .map(PostImage::getUrl)
                .toList();

        this.nickname = post.getUser().getNickname(); // ✅ 추가
        this.profileImageUrl = post.getUser().getProfileImageUrl(); // ✅ 추가
    }

    public PostResponseDto(Post post, List<PostImage> images) {
        this.id = post.getId();
        this.authorId = post.getUser().getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.nickname = post.getUser().getNickname(); // ✅ 추가
        this.profileImageUrl = post.getUser().getProfileImageUrl(); // ✅ 추가
        this.category = post.getCategory();
        this.likeCount = post.getLikeCount();
        this.viewCount = post.getViewCount();
        this.reportCount = post.getReportCount();
        this.createdAt = post.getCreatedAt();
        this.imageUrls = images.stream()
                .map(PostImage::getUrl)
                .toList();
    }
}
