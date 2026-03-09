package com.fis.boportalservice.core.service.impl;

import com.fis.boportalservice.core.domain.model.BoAdminProfile;
import com.fis.boportalservice.core.domain.model.BoAdminUser;
import com.fis.boportalservice.core.domain.model.BoAuthSession;
import com.fis.boportalservice.core.domain.model.BoRefreshToken;
import com.fis.boportalservice.core.domain.model.BoTokenClaims;
import com.fis.boportalservice.core.domain.repository.BoAdminUserRepository;
import com.fis.boportalservice.core.domain.repository.BoRefreshTokenRepository;
import com.fis.boportalservice.core.domain.repository.BoTokenProvider;
import com.fis.boportalservice.core.exception.ClientSideException;
import com.fis.boportalservice.core.exception.ErrorCode;
import com.fis.boportalservice.core.service.BoAuthService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoAuthServiceImpl implements BoAuthService {

    private static final String ACTIVE_STATUS = "ACTIVE";
    private static final int MAX_FAILED_ATTEMPT = 5;

    private final BoAdminUserRepository boAdminUserRepository;
    private final BoRefreshTokenRepository boRefreshTokenRepository;
    private final BoTokenProvider boTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public BoAuthSession login(String username, String password, String deviceId) {
        BoAdminUser user = boAdminUserRepository.findByUsername(username)
                .orElseThrow(() -> new ClientSideException(ErrorCode.BAD_REQUEST, "Invalid credential"));

        if (!ACTIVE_STATUS.equalsIgnoreCase(user.getStatus())) {
            throw new ClientSideException(ErrorCode.BAD_REQUEST, "Account is inactive");
        }

        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now())) {
            throw new ClientSideException(ErrorCode.BAD_REQUEST, "Account is temporarily locked");
        }

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            int failedAttempt = user.getFailedAttempt() == null ? 0 : user.getFailedAttempt();
            user.setFailedAttempt(failedAttempt + 1);
            if (user.getFailedAttempt() >= MAX_FAILED_ATTEMPT) {
                user.setLockedUntil(LocalDateTime.now().plusMinutes(15));
            }
            boAdminUserRepository.save(user);
            throw new ClientSideException(ErrorCode.BAD_REQUEST, "Invalid credential");
        }

        user.setFailedAttempt(0);
        user.setLockedUntil(null);
        user.setLastLoginAt(LocalDateTime.now());
        boAdminUserRepository.save(user);

        return issueSession(user, deviceId);
    }

    @Override
    public BoAuthSession refresh(String refreshToken, String deviceId) {
        BoTokenClaims claims = boTokenProvider.parseRefreshToken(refreshToken);
        BoRefreshToken storedToken = boRefreshTokenRepository.findByTokenHash(hashToken(refreshToken))
                .orElseThrow(() -> new ClientSideException(ErrorCode.BAD_REQUEST, "Refresh token not found"));

        if (storedToken.getRevokedAt() != null || storedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ClientSideException(ErrorCode.BAD_REQUEST, "Refresh token expired or revoked");
        }

        if (StringUtils.hasText(deviceId) && StringUtils.hasText(storedToken.getDeviceId())
                && !deviceId.equals(storedToken.getDeviceId())) {
            throw new ClientSideException(ErrorCode.BAD_REQUEST, "Token device mismatch");
        }

        if (!claims.getUserId().equals(storedToken.getUserId())) {
            throw new ClientSideException(ErrorCode.BAD_REQUEST, "Refresh token user mismatch");
        }

        BoAdminUser user = boAdminUserRepository.findById(claims.getUserId())
                .orElseThrow(() -> new ClientSideException(ErrorCode.BAD_REQUEST, "User not found"));

        boRefreshTokenRepository.revokeById(storedToken.getId(), LocalDateTime.now());
        return issueSession(user, deviceId);
    }

    @Override
    public void logout(String refreshToken) {
        if (!StringUtils.hasText(refreshToken)) {
            return;
        }
        boRefreshTokenRepository.findByTokenHash(hashToken(refreshToken))
                .ifPresent(token -> boRefreshTokenRepository.revokeById(token.getId(), LocalDateTime.now()));
    }

    @Override
    public BoAdminProfile me(String accessToken) {
        if (!StringUtils.hasText(accessToken)) {
            throw new ClientSideException(ErrorCode.BAD_REQUEST, "Missing access token");
        }
        BoTokenClaims claims = boTokenProvider.parseAccessToken(accessToken);
        return BoAdminProfile.builder()
                .id(claims.getUserId())
                .username(claims.getUsername())
                .roles(claims.getRoles())
                .permissions(claims.getPermissions())
                .build();
    }

    private BoAuthSession issueSession(BoAdminUser user, String deviceId) {
        List<String> roles = resolveRoles(user);
        List<String> permissions = resolvePermissions(user);

        BoAdminProfile profile = BoAdminProfile.builder()
                .id(user.getId())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .roles(roles)
                .permissions(permissions)
                .build();

        BoTokenClaims tokenClaims = BoTokenClaims.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .roles(profile.getRoles())
                .permissions(profile.getPermissions())
                .jti(UUID.randomUUID().toString())
                .build();

        String accessToken = boTokenProvider.generateAccessToken(tokenClaims);
        String refreshToken = boTokenProvider.generateRefreshToken(tokenClaims);

        boRefreshTokenRepository.save(BoRefreshToken.builder()
                .userId(user.getId())
                .tokenJti(tokenClaims.getJti())
                .tokenHash(hashToken(refreshToken))
                .deviceId(deviceId)
                .expiresAt(LocalDateTime.now().plusSeconds(boTokenProvider.getRefreshTokenExpiresInSeconds()))
                .build());

        return BoAuthSession.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(boTokenProvider.getAccessTokenExpiresInSeconds())
                .refreshExpiresIn(boTokenProvider.getRefreshTokenExpiresInSeconds())
                .user(profile)
                .build();
    }

    private List<String> resolveRoles(BoAdminUser user) {
        List<String> roles = boAdminUserRepository.findRoleCodes(user.getId());
        if (roles != null && !roles.isEmpty()) {
            return roles;
        }
        return splitCodes(user.getRoleCodes());
    }

    private List<String> resolvePermissions(BoAdminUser user) {
        List<String> permissions = boAdminUserRepository.findPermissionCodes(user.getId());
        if (permissions != null && !permissions.isEmpty()) {
            return permissions;
        }
        return splitCodes(user.getPermissionCodes());
    }

    private List<String> splitCodes(String raw) {
        if (!StringUtils.hasText(raw)) {
            return Collections.emptyList();
        }
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
    }

    private String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception ex) {
            log.error("Failed to hash refresh token", ex);
            throw new ClientSideException(ErrorCode.SYSTEM_ERROR, "Cannot process token");
        }
    }
}
