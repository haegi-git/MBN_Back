package com.example.mbn.timer.entity;

import com.example.mbn.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "timers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Timer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;              // 타이머 이름 (예: "카이녹스", "루페온")
    private int remainingSeconds;     // 남은 시간 (초 단위 저장)

    private boolean isRunning;        // 현재 실행 중인지 여부

    private LocalDateTime lastUpdatedAt; // 마지막으로 상태가 변경된 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void update(int remainingSeconds, boolean isRunning) {
        this.remainingSeconds = remainingSeconds;
        this.isRunning = isRunning;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void update(String name, int remainingSeconds, boolean isRunning, LocalDateTime updatedAt) {
        this.name = name;
        this.remainingSeconds = remainingSeconds;
        this.isRunning = isRunning;
        this.lastUpdatedAt = updatedAt;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void setRemainingSeconds(int remainingSeconds) {
        this.remainingSeconds = remainingSeconds;
    }
}
