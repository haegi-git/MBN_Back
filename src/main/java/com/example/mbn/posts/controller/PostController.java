package com.example.mbn.posts.controller;

import com.example.mbn.posts.dto.PostRequestDto;
import com.example.mbn.posts.dto.PostResponseDto;
import com.example.mbn.posts.dto.PostUpdateRequestDto;
import com.example.mbn.posts.entity.Post;
import com.example.mbn.posts.service.PostService;
import com.example.mbn.user.entity.User;
import com.example.mbn.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            @RequestBody @Valid PostRequestDto requestDto,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));

        Post savedPost = postService.createPost(requestDto, user);
        return ResponseEntity.ok(new PostResponseDto(savedPost));
    }

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadImages(@RequestParam("files") List<MultipartFile> files) {
        try {
            List<String> imageUrls = postService.uploadImages(files);

            return ResponseEntity.ok(imageUrls);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(
            @RequestParam(required = false) String category,
            Pageable pageable
    ) {
        Page<Post> posts = (category == null || category.isBlank())
                ? postService.getAllPosts(pageable)
                : postService.getAllPosts(category, pageable);

        Page<PostResponseDto> response = posts.map(post -> new PostResponseDto(post, post.getImages()));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<PostResponseDto>> getRecentPosts(
            @RequestParam(defaultValue = "10") int limit) {
        List<Post> posts = postService.getRecentPosts(limit);
        List<PostResponseDto> result = posts.stream()
                .map(PostResponseDto::new)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        PostResponseDto responseDto = postService.getPostById(id);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePost(
            @PathVariable Long id,
            @RequestPart("dto") PostUpdateRequestDto dto,
            @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages,
            HttpServletRequest request
    ) throws IOException {
        Long userId = (Long) request.getAttribute("userId");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));

        postService.updatePost(id, user, dto, newImages);
        return ResponseEntity.ok("게시글 수정 완료");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        postService.deletePost(id, user);
        return ResponseEntity.ok("게시글 삭제 완료");
    }


    @PostMapping("/{id}/like")
    public ResponseEntity<String> toggleLike(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        postService.toggleLike(id, user);
        return ResponseEntity.ok("좋아요 토글 완료");
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostResponseDto>> searchPosts(@RequestParam String keyword) {
        List<PostResponseDto> results = postService.searchPosts(keyword);
        return ResponseEntity.ok(results);
    }
}
