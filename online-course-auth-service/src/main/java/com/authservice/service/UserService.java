package com.authservice.service;

import java.util.Set;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.authservice.config.JwtTokenProvider;
import com.authservice.entity.CustomUserDetails;
import com.authservice.entity.Role;
import com.authservice.entity.User;
import com.authservice.exception.RoleNotFoundException;
import com.authservice.model.enums.ERole;
import com.authservice.model.http.request.LoginRequest;
import com.authservice.model.http.request.RegisterRequest;
import com.authservice.model.http.response.LoginResponse;
import com.authservice.model.http.response.RegisterResponse;
import com.authservice.repository.RoleRepository;
import com.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User nnot found"));
    }

    public RegisterResponse register(RegisterRequest request) {
        Role role = roleRepository.findByRole(ERole.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(role));
        userRepository.save(user);

        RegisterResponse response = new RegisterResponse();
        response.setMessage("User registered successfully!!!");
        return response;
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(userDetails.getUserId(), userDetails.getAuthorities());

        refreshTokenService.createRefreshToken(userDetails.getUserId());
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;
    }

}
