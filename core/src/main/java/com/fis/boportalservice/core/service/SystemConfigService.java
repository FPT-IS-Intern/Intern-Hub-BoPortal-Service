package com.fis.boportalservice.core.service;

import com.fis.boportalservice.core.domain.model.SystemConfig;

public interface SystemConfigService {
    SystemConfig getSystemConfig();

    SystemConfig updateSystemConfig(SystemConfig config);
}
