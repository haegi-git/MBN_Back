package com.example.mbn.timer.controller;

import com.example.mbn.timer.dto.TimerRequestDto;
import com.example.mbn.timer.dto.TimerResponseDto;
import com.example.mbn.timer.dto.TimerStatusUpdateRequestDto;
import com.example.mbn.timer.service.TimerService;
import com.example.mbn.user.entity.User;
import com.example.mbn.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/timers")
@CrossOrigin(origins = "http://localhost:5173")
public class TimerController {

    private final TimerService timerService;
    private final UserRepository userRepository;

    // 타이머 생성
    @PostMapping
    public ResponseEntity<TimerResponseDto> createTimer(
            @RequestBody @Valid TimerRequestDto dto,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        return ResponseEntity.ok(timerService.createTimer(dto, user));
    }

    // 유저의 모든 타이머 조회
    @GetMapping
    public ResponseEntity<List<TimerResponseDto>> getTimers(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        return ResponseEntity.ok(timerService.getTimersByUser(user));
    }

    // 타이머 수정 (시간 변경, 중지, 재시작 등)
    @PutMapping("/{id}")
    public ResponseEntity<TimerResponseDto> updateTimer(
            @PathVariable Long id,
            @RequestBody @Valid TimerRequestDto dto,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        return ResponseEntity.ok(timerService.updateTimer(id, dto, user));
    }

    // 타이머 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTimer(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        timerService.deleteTimer(id, user);

        // ✅ JSON 형태로 메시지 응답
        Map<String, String> response = Collections.singletonMap("message", "타이머 삭제 완료");
        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<TimerResponseDto> updateTimerStatus(
            @PathVariable Long id,
            @RequestBody TimerStatusUpdateRequestDto dto,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        TimerResponseDto updated = timerService.updateTimerStatus(id, user, dto);
        return ResponseEntity.ok(updated);
    }

}
