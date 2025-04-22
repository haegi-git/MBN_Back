package com.example.mbn.timer.service;

import com.example.mbn.timer.dto.TimerRequestDto;
import com.example.mbn.timer.dto.TimerResponseDto;
import com.example.mbn.timer.dto.TimerStatusUpdateRequestDto;
import com.example.mbn.timer.entity.Timer;
import com.example.mbn.timer.repository.TimerRepository;
import com.example.mbn.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimerServiceImpl implements TimerService {
    private final TimerRepository timerRepository;

    @Transactional
    @Override
    public TimerResponseDto createTimer(TimerRequestDto dto, User user) {
        Timer timer = Timer.builder()
                .name(dto.getName())
                .remainingSeconds(dto.getRemainingSeconds())
                .isRunning(dto.isRunning())
                .lastUpdatedAt(LocalDateTime.now())
                .user(user)
                .build();

        Timer saved = timerRepository.save(timer);
        return new TimerResponseDto(saved);
    }

    @Override
    public List<TimerResponseDto> getTimersByUser(User user) {
        return timerRepository.findAllByUser(user).stream()
                .map(TimerResponseDto::new)
                .toList();
    }

    @Transactional
    @Override
    public TimerResponseDto updateTimer(Long timerId, TimerRequestDto dto, User user) {
        Timer timer = timerRepository.findById(timerId)
                .orElseThrow(() -> new IllegalArgumentException("타이머를 찾을 수 없습니다."));

        if (!timer.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        timer.update(dto.getName(), dto.getRemainingSeconds(), dto.isRunning(), LocalDateTime.now());
        return new TimerResponseDto(timer);
    }

    @Transactional
    @Override
    public void deleteTimer(Long timerId, User user) {
        Timer timer = timerRepository.findById(timerId)
                .orElseThrow(() -> new IllegalArgumentException("타이머를 찾을 수 없습니다."));

        if (!timer.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        timerRepository.delete(timer);
    }

    @Transactional
    @Override
    public TimerResponseDto updateTimerStatus(Long id, User user, TimerStatusUpdateRequestDto dto) {
        Timer timer = timerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("타이머를 찾을 수 없습니다."));

        if (!timer.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("해당 타이머에 대한 권한이 없습니다.");
        }

        timer.setRunning(dto.isRunning());
        timer.setRemainingSeconds(dto.getRemainingSeconds());

        return new TimerResponseDto(timer); // ✅ 수정 후 상태를 DTO로 변환하여 반환
    }

}
