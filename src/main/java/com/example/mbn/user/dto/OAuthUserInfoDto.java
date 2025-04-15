package com.example.mbn.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthUserInfoDto {
    private String socialId; // 카카오나 구글의 유저 ID
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String provider; // "kakao", "google" 등
}

