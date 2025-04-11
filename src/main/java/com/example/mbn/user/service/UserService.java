package com.example.mbn.user.service;

import com.example.mbn.oauth.dto.KakaoUserInfoDto;
import com.example.mbn.user.entity.User;

public interface UserService {
    User registerOrLogin(KakaoUserInfoDto userInfo);
}
