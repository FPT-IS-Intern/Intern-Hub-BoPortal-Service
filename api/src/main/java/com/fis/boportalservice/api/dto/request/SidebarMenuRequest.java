package com.fis.boportalservice.api.dto.request;

import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

public record SidebarMenuRequest(
    String role,
    List<String> roles
) {
  public List<String> resolveRoles() {
    if (roles != null && !roles.isEmpty()) {
      return roles;
    }
    if (StringUtils.hasText(role)) {
      return List.of(role);
    }
    return Collections.emptyList();
  }
}
