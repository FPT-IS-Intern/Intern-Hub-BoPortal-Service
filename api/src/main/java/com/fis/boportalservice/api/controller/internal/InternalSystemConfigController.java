package com.fis.boportalservice.api.controller.internal;

import com.fis.boportalservice.api.dto.response.SystemConfigInternalResponse;
import com.fis.boportalservice.api.dto.response.SystemConfigPublicResponse;
import com.fis.boportalservice.api.mapper.SystemConfigApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/internal/system-config")
@RequiredArgsConstructor
public class InternalSystemConfigController {

    private final SystemConfigService systemConfigService;
    private final SystemConfigApiMapper apiMapper;

    @GetMapping
    public ResponseApi<SystemConfigInternalResponse> getSystemConfig() {
        log.info("Internal request to get full system configuration");
        return ResponseApi.success(apiMapper.toInternalResponse(systemConfigService.getSystemConfig()));
    }

    @GetMapping("/public")
    public ResponseApi<SystemConfigPublicResponse> getPublicSystemConfig() {
        log.info("Internal request to get public system configuration");
        return ResponseApi.success(apiMapper.toPublicResponse(systemConfigService.getSystemConfig()));
    }
}
