package com.example.mbn.posts.service;

import com.example.mbn.posts.dto.PostRequestDto;
import com.example.mbn.posts.dto.PostResponseDto;
import com.example.mbn.posts.dto.PostUpdateRequestDto;
import com.example.mbn.posts.entity.Post;
import com.example.mbn.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    Post createPost(PostRequestDto requestDto, User user);

    List<String> uploadImages(List<MultipartFile> files) throws IOException; // 다중 이미지 업로드

    Page<Post> getAllPosts(Pageable pageable);
    Page<Post> getAllPosts(String category, Pageable pageable);

    List<Post> getRecentPosts(int limit);

    PostResponseDto getPostById(Long id); // 반환 타입과 매개변수 모두 맞춤

    void updatePost(Long postId, User user, PostUpdateRequestDto dto, List<MultipartFile> newImages) throws IOException ;

    void deletePost(Long postId, User user);

    void toggleLike(Long postId, User user);

    List<PostResponseDto> searchPosts(String keyword);

}
