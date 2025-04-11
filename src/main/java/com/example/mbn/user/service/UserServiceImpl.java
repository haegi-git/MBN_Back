package com.example.mbn.user.service;

import com.example.mbn.oauth.dto.KakaoUserInfoDto;
import com.example.mbn.user.entity.User;
import com.example.mbn.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User registerOrLogin(KakaoUserInfoDto userInfo) {
        return userRepository.findByKakaoId(userInfo.getId())
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .kakaoId(userInfo.getId())
                            .email(userInfo.getEmail())
                            .nickname(userInfo.getNickname())
                            .profileImageUrl(userInfo.getProfileImageUrl())
                            .build();
                    return userRepository.save(newUser);
                });
    }
}
