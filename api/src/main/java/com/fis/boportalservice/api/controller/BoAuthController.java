package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.BoLoginRequest;
import com.fis.boportalservice.api.dto.request.BoLogoutRequest;
import com.fis.boportalservice.api.dto.request.BoRefreshTokenRequest;
import com.fis.boportalservice.api.dto.response.BoAdminProfileResponse;
import com.fis.boportalservice.api.dto.response.BoAuthSessionResponse;
import com.fis.boportalservice.api.mapper.BoAuthApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.service.BoAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "1. BO Authentication APIs")
@RestController
@RequestMapping("/bo-portal/auth")
@RequiredArgsConstructor
public class BoAuthController {

    private final BoAuthService boAuthService;
    private final BoAuthApiMapper boAuthApiMapper;

    @PostMapping("/login")
    public ResponseApi<BoAuthSessionResponse> login(@Valid @RequestBody BoLoginRequest request) {
        log.info("BO login request for username={}", request.getUsername());
        return ResponseApi.success(boAuthApiMapper.toSessionResponse(
                boAuthService.login(request.getUsername(), request.getPassword(), request.getDeviceId())));
    }

    @PostMapping("/refresh")
    public ResponseApi<BoAuthSessionResponse> refresh(
            @Valid @RequestBody BoRefreshTokenRequest request) {
        log.info("BO refresh token request");
        return ResponseApi.success(boAuthApiMapper.toSessionResponse(
                boAuthService.refresh(request.getRefreshToken(), request.getDeviceId())));
    }

    @PostMapping("/logout")
    public ResponseApi<Void> logout(@Valid @RequestBody BoLogoutRequest request) {
        log.info("BO logout request");
        boAuthService.logout(request.getRefreshToken());
        return ResponseApi.success(null);
    }

    @GetMapping("/me")
    public ResponseApi<BoAdminProfileResponse> me(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        String token = extractBearerToken(authorizationHeader);
        log.info("BO me request");
        return ResponseApi.success(boAuthApiMapper.toProfileResponse(boAuthService.me(token)));
    }

    private String extractBearerToken(String authorizationHeader) {
        if (!StringUtils.hasText(authorizationHeader)
                || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }
        return authorizationHeader.substring("Bearer ".length());
    }
}
