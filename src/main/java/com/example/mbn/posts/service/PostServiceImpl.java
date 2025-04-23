package com.example.mbn.posts.service;

import com.example.mbn.posts.dto.PostRequestDto;
import com.example.mbn.posts.dto.PostResponseDto;
import com.example.mbn.posts.dto.PostUpdateRequestDto;
import com.example.mbn.posts.entity.Post;
import com.example.mbn.posts.entity.PostImage;
import com.example.mbn.posts.entity.PostLike;
import com.example.mbn.posts.repository.PostImageRepository;
import com.example.mbn.posts.repository.PostLikeRepository;
import com.example.mbn.posts.repository.PostRepository;
import com.example.mbn.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final PostLikeRepository postLikeRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private String extractFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }


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
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Page<Post> getAllPosts(String category, Pageable pageable) {
        return postRepository.findByCategory(category, pageable);
    }

    @Override
    public List<Post> getRecentPosts(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postRepository.findAll(pageable).getContent();
    }

    @Transactional
    @Override
    public PostResponseDto getPostById(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글"));
        post.increaseViewCount();
        List<PostImage> images = postImageRepository.findAllByPost(post);

        return new PostResponseDto(post, images);  // 💡 리턴 추가!
    }

    @Transactional
    @Override
    public void updatePost(Long postId, User user, PostUpdateRequestDto dto, List<MultipartFile> newImages) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        // 1. 기존 이미지들 가져오기
        List<PostImage> existingImages = postImageRepository.findAllByPost(post);
        List<String> urlsToKeep = dto.getExistingImageUrls();

        // 2. 삭제 대상 이미지 추출 및 삭제
        List<PostImage> imagesToDelete = existingImages.stream()
                .filter(img -> !urlsToKeep.contains(img.getUrl()))
                .toList();
        for (PostImage image : imagesToDelete) {
            String filePath = new File(uploadDir).getAbsolutePath() + "/" + extractFileName(image.getUrl());
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            postImageRepository.delete(image); // DB에서도 삭제
        }

        // 3. 새 이미지 업로드
        List<String> newImageUrls = new ArrayList<>();
        if (newImages != null && !newImages.isEmpty()) {
            newImageUrls = uploadImages(newImages);
        }

        // 4. PostImage 엔티티로 변환해서 Post에 추가
        List<PostImage> allImages = new ArrayList<>();

        // 기존에 유지할 이미지들 다시 등록
        if (urlsToKeep != null) {
            for (String url : urlsToKeep) {
                allImages.add(new PostImage(url, post));
            }
        }

        // 새로 업로드된 이미지들 등록
        for (String url : newImageUrls) {
            allImages.add(new PostImage(url, post));
        }

        // 5. 기존 이미지 리스트 덮어쓰기
        post.getImages().clear();
        post.getImages().addAll(allImages);

        // 6. 게시글 내용 수정
        post.update(dto.getTitle(), dto.getContent(), dto.getCategory());
    }

    @Transactional
    @Override
    public void deletePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 작성자 검증
        if (!post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        // 이미지 먼저 삭제 (실제 파일 + DB)
        List<PostImage> images = postImageRepository.findAllByPost(post);
        for (PostImage image : images) {
            String filePath = new File(uploadDir).getAbsolutePath() + "/" + extractFileName(image.getUrl());
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            postImageRepository.delete(image); // DB 삭제
        }

        // 게시글 삭제
        postRepository.delete(post);
    }

    @Transactional
    @Override
    public void toggleLike(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Optional<PostLike> existingLike = postLikeRepository.findByPostAndUser(post, user);

        if (existingLike.isPresent()) {
            // 좋아요 취소
            postLikeRepository.delete(existingLike.get());
            post.decreaseLike(); // likeCount--
        } else {
            // 좋아요 추가
            PostLike like = PostLike.builder()
                    .post(post)
                    .user(user)
                    .build();
            postLikeRepository.save(like);
            post.increaseLike(); // likeCount++
        }
    }

    public List<PostResponseDto> searchPosts(String keyword){
        List<Post> posts = postRepository.searchByKeyword(keyword);
        return posts.stream().map(post -> new PostResponseDto(post, post.getImages())).toList();
    }
}
