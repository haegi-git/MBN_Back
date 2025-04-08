package com.example.mbn.posts.dto;

import com.example.mbn.posts.entity.Post;
import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;

    private String tag;
    private String platform;

    private int likeCount = 0;
    private int viewCount = 0;
    private int reportCount = 0;

    public PostResponseDto(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.tag = post.getTag();
        this.platform = post.getPlatform();
        this.likeCount = post.getLikeCount();
        this.viewCount = post.getViewCount();
        this.reportCount = post.getReportCount();
    }
}
