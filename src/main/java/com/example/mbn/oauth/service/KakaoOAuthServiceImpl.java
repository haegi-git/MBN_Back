package com.example.mbn.oauth.service;

import com.example.mbn.oauth.config.KakaoOAuthConfig;
import com.example.mbn.oauth.dto.KakaoUserInfoDto;
import com.example.mbn.user.dto.OAuthUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoOAuthServiceImpl implements KakaoOAuthService{

    private final KakaoOAuthConfig kakaoOAuthConfig;

    @Override
    public String getKakaoLoginUrl(){
        System.out.println("KAKAO CLIENT_ID: " + kakaoOAuthConfig.getClientId());
        System.out.println("KAKAO REDIRECT_URI: " + kakaoOAuthConfig.getRedirectUri());

        return "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + kakaoOAuthConfig.getClientId()
                + "&redirect_uri=" + kakaoOAuthConfig.getRedirectUri()
                + "&response_type=code";
    }

    @Override
    public String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOAuthConfig.getClientId());
        params.add("redirect_uri", kakaoOAuthConfig.getRedirectUri());
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token",
                request,
                Map.class
        );

        Map<String, Object> body = response.getBody();
        return (String) body.get("access_token"); // ✅ access_token만 리턴!
    }

    @Override
    public OAuthUserInfoDto getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<?> request = new HttpEntity<>(headers);

        // 요청 보내기
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                Map.class
        );

        Map<String, Object> body = response.getBody();

        // 파싱
        Long id = ((Number) body.get("id")).longValue();
        Map<String, Object> kakaoAccount = (Map<String, Object>) body.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) profile.get("nickname");
        String profileImageUrl = (String) profile.get("profile_image_url");

        return OAuthUserInfoDto.builder()
                .socialId(String.valueOf(id))
                .email(email)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .provider("kakao")
                .build();
    }

}
