package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.response.SystemConfigPublicResponse;
import com.fis.boportalservice.api.mapper.SystemConfigApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system-config")
@RequiredArgsConstructor
public class SystemConfigController {

    private final SystemConfigService systemConfigService;
    private final SystemConfigApiMapper apiMapper;

    @GetMapping
    public ResponseApi<SystemConfigPublicResponse> getPublicSystemConfig() {
        return ResponseApi.success(apiMapper.toPublicResponse(systemConfigService.getSystemConfig()));
    }
}
