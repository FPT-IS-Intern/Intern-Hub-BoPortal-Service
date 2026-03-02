package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.SystemConfigRequest;
import com.fis.boportalservice.api.dto.response.SystemConfigInternalResponse;
import com.fis.boportalservice.api.mapper.SystemConfigApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.domain.model.SystemConfig;
import com.fis.boportalservice.core.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "5. Admin - System Configurations")
@RestController
@RequestMapping("/bo-portal/system-config")
@RequiredArgsConstructor
public class AdminSystemConfigController {

    private final SystemConfigService systemConfigService;
    private final SystemConfigApiMapper apiMapper;

    @GetMapping
    public ResponseApi<SystemConfigInternalResponse> getSystemConfig() {
        log.info("Request to get system configuration");
        return ResponseApi.success(apiMapper.toInternalResponse(systemConfigService.getSystemConfig()));
    }

    @PutMapping
    public ResponseApi<SystemConfigInternalResponse> updateSystemConfig(@RequestBody SystemConfigRequest request) {
        log.info("Request to update system configuration: {}", request);
        SystemConfig domain = apiMapper.toDomain(request);
        SystemConfig updated = systemConfigService.updateSystemConfig(domain);
        log.info("System configuration updated successfully");
        return ResponseApi.success(apiMapper.toInternalResponse(updated));
    }
}
