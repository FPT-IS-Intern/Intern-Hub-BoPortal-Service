package com.fis.boportalservice.core.domain.repository;

import com.fis.boportalservice.core.domain.model.SystemConfig;

import java.util.Optional;

public interface SystemConfigRepository {
    Optional<SystemConfig> find();

    SystemConfig save(SystemConfig config);
}
