package com.example.mbn.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        // 🔓 예외: GET /posts 및 하위 경로는 토큰 없이 허용
        if ("GET".equalsIgnoreCase(method) && uri.startsWith("/posts")) {
            return true;
        }
        if ("GET".equalsIgnoreCase(method) && uri.startsWith("/comments")) {
            return true;
        }

        // 🔐 나머지는 토큰 필요
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String token = header.substring(7);
        if (!jwtProvider.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        Long userId = jwtProvider.getUserIdFromToken(token);
        request.setAttribute("userId", userId);
        return true;
    }

}
