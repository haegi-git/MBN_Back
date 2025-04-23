package com.example.mbn.user.service;

import com.example.mbn.file.service.FileUploadService;
import com.example.mbn.oauth.dto.KakaoUserInfoDto;
import com.example.mbn.user.dto.OAuthUserInfoDto;
import com.example.mbn.user.dto.UserRequestDto;
import com.example.mbn.user.dto.UserResponseDto;
import com.example.mbn.user.entity.User;
import com.example.mbn.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;

    @Override
    public User registerOrLogin(OAuthUserInfoDto userInfo) {
        return userRepository.findByProviderAndSocialId(userInfo.getProvider(), userInfo.getSocialId())
                .orElseGet(() -> {
                    String nickname = generateUniqueNickname(); // ❗ 무조건 랜덤

                    User newUser = User.builder()
                            .email(userInfo.getEmail())
                            .nickname(nickname)
                            .profileImageUrl(userInfo.getProfileImageUrl())
                            .provider(userInfo.getProvider())
                            .socialId(userInfo.getSocialId())
                            .rule("user")
                            .build();

                    return userRepository.save(newUser);
                });
    }


    private String generateUniqueNickname() {
        String nickname;
        do {
            nickname = "user_" + (int)(Math.random() * 1000000);
        } while (userRepository.existsByNickname(nickname));
        return nickname;
    }

    @Override
    public UserResponseDto getUserById(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 유저"));
                return UserResponseDto.from(user);
    }

    @Override
    public void updateUserInfo(Long userId, UserRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        if (dto.getNickname() != null) {
            if (userRepository.existsByNickname(dto.getNickname())) {
                throw new IllegalStateException("이미 사용 중인 닉네임입니다.");
            }
            user.setNickname(dto.getNickname());
        }

        if (dto.getProfileImageUrl() != null) {
            user.setProfileImageUrl(dto.getProfileImageUrl());
        }

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateProfile(Long userId, String nickname, MultipartFile profileImage) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저가 없습니다."));

        // 닉네임 업데이트
        user.setNickname(nickname);

        // 이미지 업로드 및 경로 저장
        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = fileUploadService.upload(profileImage, "profile"); // 예: "profile/123.jpg"
            user.setProfileImageUrl(imageUrl);
        }
    }
}
