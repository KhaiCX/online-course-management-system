package com.authservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.authservice.model.http.request.LoginRequest;
import com.authservice.model.http.request.RefreshTokenRequest;
import com.authservice.model.http.request.RegisterRequest;
import com.authservice.model.http.response.LoginResponse;
import com.authservice.model.http.response.RegisterResponse;
import com.authservice.service.RefreshTokenService;
import com.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> login(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok().body(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok().body(userService.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok().body(refreshTokenService.validateRefreshToken(request));
    }

}
