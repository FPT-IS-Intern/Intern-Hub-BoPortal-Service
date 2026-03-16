package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.SecurityConfigRequest;
import com.fis.boportalservice.api.dto.request.SystemConfigRequest;
import com.fis.boportalservice.api.dto.response.SecurityConfigResponse;
import com.fis.boportalservice.api.dto.response.SystemConfigInternalResponse;
import com.fis.boportalservice.api.dto.response.SystemConfigurationResponse;
import com.fis.boportalservice.api.mapper.SecurityConfigApiMapper;
import com.fis.boportalservice.api.mapper.SystemConfigApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.domain.model.SecurityConfig;
import com.fis.boportalservice.core.domain.model.SystemConfig;
import com.fis.boportalservice.core.service.SecurityConfigService;
import com.fis.boportalservice.core.service.SystemConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "5. Admin - System Configurations")
@RestController
@RequestMapping("/bo-portal/system-configurations")
@RequiredArgsConstructor
public class AdminSystemConfigurationController {

  private final SystemConfigService systemConfigService;
  private final SystemConfigApiMapper systemConfigApiMapper;
  private final SecurityConfigService securityConfigService;
  private final SecurityConfigApiMapper securityConfigApiMapper;

  @GetMapping
  public ResponseApi<SystemConfigurationResponse> getSystemConfiguration() {
    log.info("Request to get system and security configuration");
    SystemConfigInternalResponse systemConfig = systemConfigApiMapper
            .toInternalResponse(systemConfigService.getSystemConfig());
    SecurityConfigResponse securityConfig = securityConfigApiMapper
            .toResponse(securityConfigService.getSecurityConfig());
    return ResponseApi.success(SystemConfigurationResponse.builder()
            .systemConfig(systemConfig)
            .securityConfig(securityConfig)
            .build());
  }

  @PutMapping("/system")
  public ResponseApi<SystemConfigInternalResponse> updateSystemConfig(@RequestBody SystemConfigRequest request) {
    log.info("Request to update system configuration: {}", request);
    SystemConfig domain = systemConfigApiMapper.toDomain(request);
    SystemConfig updated = systemConfigService.updateSystemConfig(domain);
    log.info("System configuration updated successfully");
    return ResponseApi.success(systemConfigApiMapper.toInternalResponse(updated));
  }

  @PutMapping("/security")
  public ResponseApi<SecurityConfigResponse> updateSecurityConfig(@RequestBody SecurityConfigRequest request) {
    log.info("Request to update security configuration: {}", request);
    SecurityConfig domain = securityConfigApiMapper.toDomain(request);
    SecurityConfig updated = securityConfigService.updateSecurityConfig(domain);
    log.info("Security configuration updated successfully");
    return ResponseApi.success(securityConfigApiMapper.toResponse(updated));
  }
}
