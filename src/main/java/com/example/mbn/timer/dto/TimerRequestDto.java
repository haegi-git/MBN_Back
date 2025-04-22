package com.example.mbn.timer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TimerRequestDto {
    @NotBlank
    @Size(min = 1, max = 15)
    private String name;

    private int remainingSeconds;

    private boolean isRunning;
}
