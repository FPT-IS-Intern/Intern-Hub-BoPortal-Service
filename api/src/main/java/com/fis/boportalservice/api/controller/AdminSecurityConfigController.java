package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.SecurityConfigRequest;
import com.fis.boportalservice.api.dto.response.SecurityConfigResponse;
import com.fis.boportalservice.api.mapper.SecurityConfigApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.domain.model.SecurityConfig;
import com.fis.boportalservice.core.service.SecurityConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "5. Admin - Security Configurations")
@RestController
@RequestMapping("/bo-portal/security-config")
@RequiredArgsConstructor
public class AdminSecurityConfigController {

  private final SecurityConfigService securityConfigService;
  private final SecurityConfigApiMapper apiMapper;

  @GetMapping
  public ResponseApi<SecurityConfigResponse> getSecurityConfig() {
    log.info("Request to get security configuration");
    return ResponseApi.success(apiMapper.toResponse(securityConfigService.getSecurityConfig()));
  }

  @PutMapping
  public ResponseApi<SecurityConfigResponse> updateSecurityConfig(@RequestBody SecurityConfigRequest request) {
    log.info("Request to update security configuration: {}", request);
    SecurityConfig domain = apiMapper.toDomain(request);
    SecurityConfig updated = securityConfigService.updateSecurityConfig(domain);
    log.info("Security configuration updated successfully");
    return ResponseApi.success(apiMapper.toResponse(updated));
  }
}
