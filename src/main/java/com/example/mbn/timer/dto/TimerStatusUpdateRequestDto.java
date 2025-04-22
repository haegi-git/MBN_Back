package com.example.mbn.timer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TimerStatusUpdateRequestDto {
    private boolean isRunning;
    private int remainingSeconds;
}