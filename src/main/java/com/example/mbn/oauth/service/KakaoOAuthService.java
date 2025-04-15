package com.example.mbn.oauth.service;

import com.example.mbn.oauth.dto.KakaoUserInfoDto;
import com.example.mbn.user.dto.OAuthUserInfoDto;

public interface KakaoOAuthService {
    String getKakaoLoginUrl();
    String getAccessToken(String code);

    OAuthUserInfoDto getUserInfo(String accessToken);

}
