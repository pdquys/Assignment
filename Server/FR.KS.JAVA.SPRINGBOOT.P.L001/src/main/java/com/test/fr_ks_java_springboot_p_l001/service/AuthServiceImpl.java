package com.test.fr_ks_java_springboot_p_l001.service;

import com.test.fr_ks_java_springboot_p_l001.dto.user.*;
import com.test.fr_ks_java_springboot_p_l001.entity.Role;
import com.test.fr_ks_java_springboot_p_l001.entity.RoleEnum;
import com.test.fr_ks_java_springboot_p_l001.entity.User;
import com.test.fr_ks_java_springboot_p_l001.repository.RoleRepository;
import com.test.fr_ks_java_springboot_p_l001.repository.UserRepository;
import com.test.fr_ks_java_springboot_p_l001.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtProvider;

    @Override
    public void register(RegisterRequest req) {

        if (userRepository.existsByEmailIgnoreCase(req.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // default ROLE_USER
        Role roleUser = roleRepository.findByName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new IllegalArgumentException("ROLE_USER not found"));

        User u = new User();
        u.setEmail(req.email().toLowerCase().trim());
        u.setPassword(passwordEncoder.encode(req.password()));
        u.setFullName(req.fullName().trim());
        u.setActive(true);
        u.setRoles(Set.of(roleUser));

        userRepository.save(u);
    }

    @Override
    public AuthResponse login(LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        String accessToken = jwtProvider.generateAccessToken(auth.getName());
        String refreshToken = jwtProvider.generateRefreshToken(auth.getName());

        // Lấy user từ DB
        User user = userRepository.findByEmailIgnoreCaseAndActiveTrue(auth.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        // Map User -> UserResponse
        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getActive(),
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        // Lấy danh sách role name
        Set<String> roles = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet());

        return new AuthResponse(
                accessToken,
                refreshToken,
                userResponse,
                roles
        );

    }

    @Override
    public AuthResponse refresh(RefreshRequest req) {
        String refreshToken = req.refreshToken();

        // 1. Validate token
        if (!jwtProvider.validate(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        // 2. Check token type
        if (!"refresh".equals(jwtProvider.getType(refreshToken))) {
            throw new IllegalArgumentException("Token is not refresh token");
        }

        // 3. Get subject (email)
        String email = jwtProvider.getSubject(refreshToken);

        // 4. Check user still exists & active
        User user = userRepository.findByEmailIgnoreCaseAndActiveTrue(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found or inactive"));

        // 5. Generate NEW tokens (rotate refresh token)
        String newAccessToken = jwtProvider.generateAccessToken(email);
        String newRefreshToken = jwtProvider.generateRefreshToken(email);

        // 6. Map User → UserResponse
        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getActive(),
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        // 7. Extract role names
        Set<String> roles = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet());

        // 8. Return AuthResponse
        return new AuthResponse(
                newAccessToken,
                newRefreshToken,
                userResponse,
                roles
        );
    }
}
