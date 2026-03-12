package com.fis.boportalservice.core.service.impl;

import com.fis.boportalservice.core.domain.model.SecurityConfig;
import com.fis.boportalservice.core.domain.repository.SecurityConfigRepository;
import com.fis.boportalservice.core.service.SecurityConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityConfigServiceImpl implements SecurityConfigService {

  private final SecurityConfigRepository securityConfigRepository;

  @Override
  public SecurityConfig getSecurityConfig() {
    return securityConfigRepository.find()
        .orElseThrow(() -> {
          log.error("Security configuration not found");
          return new RuntimeException("Security config not found");
        });
  }

  @Override
  public SecurityConfig updateSecurityConfig(SecurityConfig config) {
    log.info("Updating security configuration in repository");
    return securityConfigRepository.save(config);
  }
}
