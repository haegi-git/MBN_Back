package com.example.mbn.posts.service;

import com.example.mbn.posts.dto.PostRequestDto;
import com.example.mbn.posts.entity.Post;
import com.example.mbn.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class  PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public Post createPost(PostRequestDto requestDto){
        Post post = requestDto.toEntity();
        return postRepository.save(post);
    }

}
