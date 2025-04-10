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
    private String title;
    private String content;

    private String tag;
    private String platform;

    private List<String> imageUrls;

    private int likeCount = 0;
    private int viewCount = 0;
    private int reportCount = 0;

    private LocalDateTime createdAt;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.tag = post.getTag();
        this.platform = post.getPlatform();
        this.likeCount = post.getLikeCount();
        this.viewCount = post.getViewCount();
        this.reportCount = post.getReportCount();
        this.createdAt = post.getCreatedAt();
        this.imageUrls = post.getImages().stream()
                .map(PostImage::getUrl)
                .toList();
    }

    public PostResponseDto(Post post, List<PostImage> images) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.tag = post.getTag();
        this.platform = post.getPlatform();
        this.likeCount = post.getLikeCount();
        this.viewCount = post.getViewCount();
        this.reportCount = post.getReportCount();
        this.createdAt = post.getCreatedAt();
        this.imageUrls = images.stream()
                .map(PostImage::getUrl)
                .toList();
    }
}
