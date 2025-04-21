package com.example.mbn.comment.repository;

import com.example.mbn.comment.entity.CommentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentHistoryRepository extends JpaRepository<CommentHistory, Long> {
}
