package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.SystemConfigRequest;
import com.fis.boportalservice.api.dto.response.SystemConfigInternalResponse;
import com.fis.boportalservice.api.mapper.SystemConfigApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.domain.model.SystemConfig;
import com.fis.boportalservice.core.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/system-config")
@RequiredArgsConstructor
public class AdminSystemConfigController {

    private final SystemConfigService systemConfigService;
    private final SystemConfigApiMapper apiMapper;

    @GetMapping
    public ResponseApi<SystemConfigInternalResponse> getSystemConfig() {
        return ResponseApi.success(apiMapper.toInternalResponse(systemConfigService.getSystemConfig()));
    }

    @PutMapping
    public ResponseApi<SystemConfigInternalResponse> updateSystemConfig(@RequestBody SystemConfigRequest request) {
        SystemConfig domain = apiMapper.toDomain(request);
        SystemConfig updated = systemConfigService.updateSystemConfig(domain);
        return ResponseApi.success(apiMapper.toInternalResponse(updated));
    }
}
