package com.example.mbn.oauth.controller;

import com.example.mbn.jwt.JwtProvider;
import com.example.mbn.oauth.config.KakaoOAuthConfig;
import com.example.mbn.oauth.dto.KakaoUserInfoDto;
import com.example.mbn.oauth.service.KakaoOAuthService;
import com.example.mbn.user.dto.OAuthUserInfoDto;
import com.example.mbn.user.entity.User;
import com.example.mbn.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    @Value("${frontend.domain}")
    private String frontendDomain;

    private final KakaoOAuthService kakaoOAuthService;
    private final KakaoOAuthConfig kakaoOAuthConfig;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @GetMapping("/kakao")
    public ResponseEntity<String> getKakaoLoginUrl() {
        String loginUrl = kakaoOAuthService.getKakaoLoginUrl();
        System.out.println("KAKAO CLIENT_ID: " + kakaoOAuthConfig.getClientId());
        System.out.println("KAKAO REDIRECT_URI: " + kakaoOAuthConfig.getRedirectUri());
        return ResponseEntity.ok(loginUrl);
    }

    @GetMapping("/kakao/callback")
    public void kakaoCallback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        String accessToken = kakaoOAuthService.getAccessToken(code);
        OAuthUserInfoDto oauthUserInfo = kakaoOAuthService.getUserInfo(accessToken);
        User user = userService.registerOrLogin(oauthUserInfo);

        String jwtToken = jwtProvider.createToken(user.getId());

        // ✅ 리액트 쪽으로 리디렉션 + JWT 쿼리 파라미터로 전달
        String redirectUrl = frontendDomain+"oauth/callback/success?token=" + jwtToken;
        System.out.println(redirectUrl+"gdgdfgdfgdfgㅇㅀㅇㅀㅋㅋㅋ여기임");
        response.sendRedirect(redirectUrl);
    }
}
