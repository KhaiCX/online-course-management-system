package com.authservice.service;

import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.authservice.entity.RefreshToken;
import com.authservice.entity.User;
import com.authservice.repository.RefreshTokenRepository;
import com.authservice.repository.UserRepository;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private static final Integer refreshTokenDurationMs = 30 * 24 * 60 * 60 * 1000;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public void createRefreshToken(UUID userId) {
        User user = userRepository.findByUserId(userId).orElseThrow();
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user).orElse(new RefreshToken());
        refreshToken.setUser(userRepository.findByUserId(userId).orElseThrow());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshTokenRepository.save(refreshToken);
    }
}
