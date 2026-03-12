package com.fis.boportalservice.core.domain.repository;

import com.fis.boportalservice.core.domain.model.SecurityConfig;

import java.util.Optional;

public interface SecurityConfigRepository {
  Optional<SecurityConfig> find();

  SecurityConfig save(SecurityConfig config);
}
