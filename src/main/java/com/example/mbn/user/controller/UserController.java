package com.example.mbn.user.controller;

import com.example.mbn.user.dto.UserRequestDto;
import com.example.mbn.user.dto.UserResponseDto;
import com.example.mbn.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        System.out.println(userId+"dgdfgdgdfgdfgㅇㄹㄴㅇㄹ" +request);
        UserResponseDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<String> updateProfile(
            @RequestParam("nickname") String nickname,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId"); // ✅ 여기!
        userService.updateProfile(userId, nickname, profileImage);
        return ResponseEntity.ok("프로필 수정 완료");
    }

}
