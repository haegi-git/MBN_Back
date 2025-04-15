package com.example.mbn.config;

import com.example.mbn.jwt.JwtAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtAuthInterceptor jwtAuthInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:src/main/resources/static/uploads/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor)
                .addPathPatterns("/user","/posts")
                .excludePathPatterns(
                        "/uploads/**",       // 🔓 이미지 경로 허용
                        "/oauth/**",         // 🔓 로그인 콜백 허용
                        "/error",            // 🔓 에러 페이지
                        "/favicon.ico"       // 🔓 파비콘
                );; // ✅ 보호할 경로 설정
    }
}
