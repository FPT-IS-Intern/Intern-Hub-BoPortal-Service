package com.fis.boportalservice.core.service.impl;

import com.fis.boportalservice.core.domain.model.SystemConfig;
import com.fis.boportalservice.core.domain.repository.SystemConfigRepository;
import com.fis.boportalservice.core.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigRepository systemConfigRepository;

    @Override
    public SystemConfig getSystemConfig() {
        return systemConfigRepository.find()
                .orElseThrow(() -> {
                    log.error("System configuration not found");
                    return new RuntimeException("System config not found");
                });
    }

    @Override
    public SystemConfig updateSystemConfig(SystemConfig config) {
        log.info("Updating system configuration in repository");
        return systemConfigRepository.save(config);
    }
}
