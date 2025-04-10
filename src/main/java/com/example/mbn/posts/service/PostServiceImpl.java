package com.example.mbn.posts.service;

import com.example.mbn.posts.dto.PostRequestDto;
import com.example.mbn.posts.dto.PostResponseDto;
import com.example.mbn.posts.entity.Post;
import com.example.mbn.posts.entity.PostImage;
import com.example.mbn.posts.repository.PostImageRepository;
import com.example.mbn.posts.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final String uploadDir = new File("src/main/resources/static/uploads/").getAbsolutePath() + "/";


    @Override
    @Transactional
    public Post createPost(PostRequestDto requestDto) {
        Post post = requestDto.toEntity();

        // 이미지 URL이 null이 아니고, 하나라도 있으면 → PostImage로 변환
        if (requestDto.getImageUrls() != null && !requestDto.getImageUrls().isEmpty()) {
            List<PostImage> imageEntities = requestDto.getImageUrls().stream()
                    .map(url -> new PostImage(url, post))
                    .toList();

            post.getImages().addAll(imageEntities); // Post와 연결!
        }

        return postRepository.save(post); // Post와 연관된 이미지들까지 저장됨
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
            File saveFile = new File(uploadDir + fileName);
            saveFile.getParentFile().mkdirs();
            file.transferTo(saveFile);

            urls.add("/uploads/" + fileName);
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
