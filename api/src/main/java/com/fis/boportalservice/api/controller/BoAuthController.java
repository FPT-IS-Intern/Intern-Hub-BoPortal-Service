package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.BoLoginRequest;
import com.fis.boportalservice.api.dto.request.BoLogoutRequest;
import com.fis.boportalservice.api.dto.request.BoRefreshTokenRequest;
import com.fis.boportalservice.api.dto.response.BoAdminProfileResponse;
import com.fis.boportalservice.api.dto.response.BoAuthSessionResponse;
import com.fis.boportalservice.api.mapper.BoAuthApiMapper;
import com.fis.boportalservice.api.security.LoginPasswordResolver;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.domain.usecase.BoAuthUsecase;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "1. BO Authentication APIs")
@RestController
@RequestMapping("/bo-portal/auth")
@RequiredArgsConstructor
public class BoAuthController {

  private final BoAuthUsecase boAuthUsecase;
  private final BoAuthApiMapper boAuthApiMapper;
  private final LoginPasswordResolver loginPasswordResolver;

  @PostMapping("/login")
  public ResponseApi<BoAuthSessionResponse> login(@Valid @RequestBody BoLoginRequest request) {
    log.info("event=BO_LOGIN_REQUEST");
    String resolvedUsername = loginPasswordResolver.resolveUsername(request);
    String resolvedPassword = loginPasswordResolver.resolvePassword(request);
    return ResponseApi.success(boAuthApiMapper.toSessionResponse(
        boAuthUsecase.login(resolvedUsername, resolvedPassword, request.deviceId())));
  }

  @GetMapping("/public-key")
  public ResponseApi<String> publicKey() {
    return ResponseApi.success(loginPasswordResolver.getPublicKey());
  }

  @PostMapping("/refresh")
  public ResponseApi<BoAuthSessionResponse> refresh(
      @Valid @RequestBody BoRefreshTokenRequest request) {
    log.info("event=BO_REFRESH_TOKEN_REQUEST");
    return ResponseApi.success(boAuthApiMapper.toSessionResponse(
        boAuthUsecase.refresh(request.getRefreshToken(), request.getDeviceId())));
  }

  @PostMapping("/logout")
  public ResponseApi<Void> logout(@Valid @RequestBody BoLogoutRequest request) {
    log.info("event=BO_LOGOUT_REQUEST");
    boAuthUsecase.logout(request.getRefreshToken());
    return ResponseApi.success(null);
  }

  @GetMapping("/me")
  public ResponseApi<BoAdminProfileResponse> me(
      @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
    String token = extractBearerToken(authorizationHeader);
    log.info("event=BO_ME_REQUEST");
    return ResponseApi.success(boAuthApiMapper.toProfileResponse(boAuthUsecase.me(token)));
  }

  private String extractBearerToken(String authorizationHeader) {
    if (!StringUtils.hasText(authorizationHeader)
        || !authorizationHeader.startsWith("Bearer ")) {
      return null;
    }
    return authorizationHeader.substring("Bearer ".length());
  }
}

