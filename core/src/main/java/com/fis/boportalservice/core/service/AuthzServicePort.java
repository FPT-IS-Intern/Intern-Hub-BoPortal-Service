package com.fis.boportalservice.core.service;

import com.fis.boportalservice.core.domain.model.AuthzResource;
import com.fis.boportalservice.core.domain.model.AuthzRole;
import com.fis.boportalservice.core.domain.model.AuthzRolePermission;

import java.util.List;

public interface AuthzServicePort {

  AuthzResource createResource(String name, String code, Long categoryId, String description);

  AuthzRole createRole(String name, String description);

  void updateRolePermissions(Long roleId, List<ResourcePermission> resources);

  List<AuthzRole> getRoles();

  List<AuthzRolePermission> getRolePermissions(Long roleId);

  List<AuthzResource> getAllResources();

  record ResourcePermission(Long id, List<Integer> permissions) {
  }
}
