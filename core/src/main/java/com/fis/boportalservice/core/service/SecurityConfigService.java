package com.fis.boportalservice.core.service;

import com.fis.boportalservice.core.domain.model.SecurityConfig;

public interface SecurityConfigService {
  SecurityConfig getSecurityConfig();

  SecurityConfig updateSecurityConfig(SecurityConfig config);
}
