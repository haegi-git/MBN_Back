package com.example.mbn.oauth.service;

import com.example.mbn.oauth.dto.KakaoUserInfoDto;

public interface KakaoOAuthService {
    String getKakaoLoginUrl();
    String getAccessToken(String code);

    KakaoUserInfoDto getUserInfo(String accessToken);

}
