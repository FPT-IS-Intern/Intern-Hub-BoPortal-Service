package com.fis.boportalservice.core.service.impl;

import com.fis.boportalservice.core.domain.model.SystemConfig;
import com.fis.boportalservice.core.domain.repository.SystemConfigRepository;
import com.fis.boportalservice.core.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigRepository systemConfigRepository;

    @Override
    public SystemConfig getSystemConfig() {
        return systemConfigRepository.find()
                .orElseThrow(() -> new RuntimeException("System config not found"));
    }

    @Override
    public SystemConfig updateSystemConfig(SystemConfig config) {
        return systemConfigRepository.save(config);
    }
}
