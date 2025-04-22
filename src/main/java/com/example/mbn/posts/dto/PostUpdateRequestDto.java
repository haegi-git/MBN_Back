package com.example.mbn.posts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostUpdateRequestDto {
    private String title;
    private String content;
    private String category;
    private List<String> existingImageUrls; // ✅ 남겨둘 기존 이미지 URL들
}
