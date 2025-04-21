package com.example.mbn.comment.service;

import com.example.mbn.comment.dto.CommentRequestDto;
import com.example.mbn.comment.dto.CommentUpdateRequestDto;
import com.example.mbn.comment.entity.Comment;
import com.example.mbn.comment.entity.CommentHistory;
import com.example.mbn.comment.repository.CommentHistoryRepository;
import com.example.mbn.comment.repository.CommentRepository;
import com.example.mbn.posts.entity.Post;
import com.example.mbn.posts.repository.PostRepository;
import com.example.mbn.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentHistoryRepository commentHistoryRepository;

    @Transactional
    @Override
    public Comment createComment(CommentRequestDto requestDto, User user) {

        Post post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .user(user)
                .post(post)
                .likeCount(0)
                .reportCount(0)
                .isEdited(false)
                .isHidden(false)
                .build();

        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return commentRepository.findAllByPostOrderByCreatedAtAsc(post);
    }

    @Transactional
    @Override
    public Comment updateComment(Long commentId, CommentUpdateRequestDto dto, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // 작성자 확인
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        // 히스토리 먼저 저장 (수정 전 내용)
        CommentHistory history = CommentHistory.builder()
                .comment(comment)
                .oldContent(comment.getContent()) // ✅ 수정 전 내용
                .modifiedAt(LocalDateTime.now())
                .user(user)
                .build();
        commentHistoryRepository.save(history);

        // 댓글 실제 수정
        comment.updateContent(dto.getContent()); // ✅ 수정은 나중에

        return comment;
    }

    @Transactional
    @Override
    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        comment.markAsDeleted(); // 👈 내부에서 isDeleted = true;
    }

}
