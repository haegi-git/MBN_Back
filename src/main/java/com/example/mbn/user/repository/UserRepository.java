package com.example.mbn.user.repository;

import com.example.mbn.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByNickname(String nickname);

    Optional<User> findByProviderAndSocialId(String provider, String socialId);

    Optional<User> findBySocialIdAndProvider(String socialId, String provider);

}
