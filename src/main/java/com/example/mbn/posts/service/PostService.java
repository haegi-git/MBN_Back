package com.example.mbn.posts.service;

import com.example.mbn.posts.dto.PostRequestDto;
import com.example.mbn.posts.entity.Post;

public interface PostService {
    Post createPost(PostRequestDto requestDto);
}
