package com.example.mbn.comment.service;

import com.example.mbn.comment.dto.CommentRequestDto;
import com.example.mbn.comment.entity.Comment;
import com.example.mbn.comment.repository.CommentRepository;
import com.example.mbn.posts.entity.Post;
import com.example.mbn.posts.repository.PostRepository;
import com.example.mbn.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

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
}
