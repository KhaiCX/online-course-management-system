package com.authservice.service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.authservice.config.JwtTokenProvider;
import com.authservice.entity.CustomUserDetails;
import com.authservice.entity.RefreshToken;
import com.authservice.entity.User;
import com.authservice.exception.NotFoundException;
import com.authservice.exception.TokenRefreshException;
import com.authservice.model.http.request.RefreshTokenRequest;
import com.authservice.model.http.response.LoginResponse;
import com.authservice.repository.RefreshTokenRepository;
import com.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private static final Long refreshTokenDurationMs = Duration.ofDays(30).toMillis();

    public void createRefreshToken(UUID userId, String refreshTokenParam) {
        User user = userRepository.findByUserId(userId).orElseThrow();
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user).orElse(new RefreshToken());
        refreshToken.setUser(userRepository.findByUserId(userId).orElseThrow());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(refreshTokenParam);
        refreshTokenRepository.save(refreshToken);
    }

    public LoginResponse validateRefreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new NotFoundException("Refresh Token Not Found"));

        String rToken = refreshToken.getToken();
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenRefreshException(rToken);
        }

        CustomUserDetails userDetails = new CustomUserDetails(refreshToken.getUser());
        String accessToken = jwtTokenProvider.generateToken(userDetails.getUserId(), userDetails.getAuthorities());
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }
}
