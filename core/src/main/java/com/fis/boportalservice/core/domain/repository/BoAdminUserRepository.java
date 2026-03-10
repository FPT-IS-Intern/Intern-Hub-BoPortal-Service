package com.fis.boportalservice.core.domain.repository;

import com.fis.boportalservice.core.domain.model.BoAdminUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BoAdminUserRepository {
  Optional<BoAdminUser> findByUsername(String username);

  Optional<BoAdminUser> findById(UUID id);

  List<String> findRoleCodes(UUID userId);

  List<String> findPermissionCodes(UUID userId);

  BoAdminUser save(BoAdminUser user);
}
