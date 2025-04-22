package com.example.mbn.timer.service;

import com.example.mbn.timer.dto.TimerRequestDto;
import com.example.mbn.timer.dto.TimerResponseDto;
import com.example.mbn.timer.dto.TimerStatusUpdateRequestDto;
import com.example.mbn.user.entity.User;

import java.util.List;

public interface TimerService {
    TimerResponseDto createTimer(TimerRequestDto dto, User user);
    List<TimerResponseDto> getTimersByUser(User user);
    TimerResponseDto updateTimer(Long timerId, TimerRequestDto dto, User user);
    void deleteTimer(Long timerId, User user);

    TimerResponseDto updateTimerStatus(Long id, User user, TimerStatusUpdateRequestDto dto);

}
