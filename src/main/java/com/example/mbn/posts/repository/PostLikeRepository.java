package com.example.mbn.posts.repository;

import com.example.mbn.posts.entity.Post;
import com.example.mbn.posts.entity.PostLike;
import com.example.mbn.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndUser(Post post, User user);

}
