package com.example.mbn.comment.service;

import com.example.mbn.comment.dto.CommentRequestDto;
import com.example.mbn.comment.dto.CommentUpdateRequestDto;
import com.example.mbn.comment.entity.Comment;
import com.example.mbn.user.entity.User;

import java.util.List;

public interface CommentService {
    Comment createComment(CommentRequestDto requestDto, User user);

    List<Comment> getCommentsByPostId(Long postId);

    Comment updateComment(Long commentId, CommentUpdateRequestDto dto, User user);

    void deleteComment(Long commentId, User user);
}
