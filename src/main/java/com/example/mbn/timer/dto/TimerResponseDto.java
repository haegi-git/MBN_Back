package com.example.mbn.timer.dto;

import com.example.mbn.timer.entity.Timer;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TimerResponseDto {
    private Long id;
    private String name;
    private int remainingSeconds;
    private boolean isRunning;
    private LocalDateTime lastUpdatedAt;

    public TimerResponseDto(Timer timer) {
        this.id = timer.getId();
        this.name = timer.getName();
        this.remainingSeconds = timer.getRemainingSeconds();
        this.isRunning = timer.isRunning();
        this.lastUpdatedAt = timer.getLastUpdatedAt();
    }
}
