package com.fis.boportalservice.core.service.impl;

import com.fis.boportalservice.core.domain.model.SecurityConfig;
import com.fis.boportalservice.core.domain.repository.SecurityConfigRepository;
import com.fis.boportalservice.core.exception.ClientSideException;
import com.fis.boportalservice.core.exception.ErrorCode;
import com.fis.boportalservice.core.service.SecurityConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SecurityConfigServiceImpl implements SecurityConfigService {

  private final SecurityConfigRepository securityConfigRepository;

  @Override
  public SecurityConfig getSecurityConfig() {
    return securityConfigRepository.find()
        .orElseThrow(() -> {
          log.error("Security configuration not found");
          return new ClientSideException(ErrorCode.NOT_FOUND, "Security config not found");
        });
  }

  @Override
  public SecurityConfig updateSecurityConfig(SecurityConfig config) {
    log.info("event=SECURITY_CONFIGURATION_PERSIST_UPDATE");
    return securityConfigRepository.save(config);
  }
}
