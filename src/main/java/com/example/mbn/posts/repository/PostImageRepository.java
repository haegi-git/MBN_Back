package com.example.mbn.posts.repository;

import com.example.mbn.posts.entity.Post;
import com.example.mbn.posts.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findAllByPost(Post post);


}
