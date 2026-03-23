package com.fis.boportalservice.core.service.impl;

import com.fis.boportalservice.core.domain.model.SystemConfig;
import com.fis.boportalservice.core.domain.repository.SystemConfigRepository;
import com.fis.boportalservice.core.exception.ClientSideException;
import com.fis.boportalservice.core.exception.ErrorCode;
import com.fis.boportalservice.core.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements SystemConfigService {

  private final SystemConfigRepository systemConfigRepository;

  @Override
  public SystemConfig getSystemConfig() {
    return systemConfigRepository.find()
        .orElseThrow(() -> {
          log.error("System configuration not found");
          return new ClientSideException(ErrorCode.NOT_FOUND, "System config not found");
        });
  }

  @Override
  public SystemConfig updateSystemConfig(SystemConfig config) {
    log.info("event=SYSTEM_CONFIGURATION_PERSIST_UPDATE");
    return systemConfigRepository.save(config);
  }
}
