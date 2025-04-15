package com.example.mbn.posts.service;

import com.example.mbn.posts.dto.PostRequestDto;
import com.example.mbn.posts.dto.PostResponseDto;
import com.example.mbn.posts.entity.Post;
import com.example.mbn.posts.entity.PostImage;
import com.example.mbn.posts.repository.PostImageRepository;
import com.example.mbn.posts.repository.PostRepository;
import com.example.mbn.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class  PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;


    @Override
    @Transactional
    public Post createPost(PostRequestDto requestDto, User user) {
        Post post = requestDto.toEntity();
        post.setUser(user); // ✅ 작성자 설정

        if (requestDto.getImageUrls() != null && !requestDto.getImageUrls().isEmpty()) {
            List<PostImage> imageEntities = requestDto.getImageUrls().stream()
                    .map(url -> new PostImage(url, post))
                    .toList();
            post.getImages().addAll(imageEntities);
        }

        return postRepository.save(post);
    }

    
    // 다중 이미지 업로드
    @Override
    public List<String> uploadImages(List<MultipartFile> files) throws IOException {
        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String extension = Optional.ofNullable(file.getOriginalFilename())
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(file.getOriginalFilename().lastIndexOf(".")))
                    .orElse("");

            String fileName = UUID.randomUUID() + extension;

            // 절대 경로로 보정
            String absolutePath = new File(uploadDir).getAbsolutePath() + "/";
            File saveFile = new File(absolutePath + fileName);
            saveFile.getParentFile().mkdirs();
            file.transferTo(saveFile);

            urls.add("/uploads/" + fileName); // 이건 static 기준이라 그대로 둬도 됨
        }

        return urls;
    }


    @Override
    public List<Post>getAllPosts(){
        return postRepository.findAll();
    }


    @Override
    public PostResponseDto getPostById(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글"));
        List<PostImage> images = postImageRepository.findAllByPost(post);

        return new PostResponseDto(post, images);  // 💡 리턴 추가!
    }
}
