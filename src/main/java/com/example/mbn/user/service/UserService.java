package com.example.mbn.user.service;

import com.example.mbn.oauth.dto.KakaoUserInfoDto;
import com.example.mbn.user.dto.OAuthUserInfoDto;
import com.example.mbn.user.dto.UserRequestDto;
import com.example.mbn.user.dto.UserResponseDto;
import com.example.mbn.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User registerOrLogin(OAuthUserInfoDto userInfo);

    UserResponseDto getUserById(Long userId);
    void updateUserInfo(Long userId, UserRequestDto dto);

    void updateProfile(Long userId, String nickname, MultipartFile profileImage);
}
