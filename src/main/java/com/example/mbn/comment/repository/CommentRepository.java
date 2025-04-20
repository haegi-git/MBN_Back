package com.example.mbn.comment.repository;

import com.example.mbn.comment.entity.Comment;
import com.example.mbn.posts.entity.Post;
import com.example.mbn.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글에 달린 댓글 전체 조회
    List<Comment> findAllByPostOrderByCreatedAtAsc(Post post);

    // 특정 유저가 작성한 댓글 목록 (관리자 페이지 등에서 유용)
    List<Comment> findAllByUser(User user);

    // 신고 수 10 이상인 댓글만 조회
    List<Comment> findAllByIsHiddenTrue();
}