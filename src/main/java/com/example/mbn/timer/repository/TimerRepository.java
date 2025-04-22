package com.example.mbn.timer.repository;

import com.example.mbn.timer.entity.Timer;
import com.example.mbn.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimerRepository extends JpaRepository<Timer, Long> {
    List<Timer> findAllByUser(User user);
}
