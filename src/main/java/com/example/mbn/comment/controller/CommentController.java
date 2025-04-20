package com.example.mbn.comment.controller;

import com.example.mbn.comment.dto.CommentRequestDto;
import com.example.mbn.comment.dto.CommentResponseDto;
import com.example.mbn.comment.entity.Comment;
import com.example.mbn.comment.service.CommentService;
import com.example.mbn.user.entity.User;
import com.example.mbn.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createComment(
            @RequestBody CommentRequestDto requestDto,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        Comment comment = commentService.createComment(requestDto, user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "댓글 작성 완료");
        response.put("id", comment.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(@RequestParam Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        List<CommentResponseDto> response = comments.stream()
                .map(CommentResponseDto::new)
                .toList();
        return ResponseEntity.ok(response);
    }
}
