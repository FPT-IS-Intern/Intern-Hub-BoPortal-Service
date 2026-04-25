package com.fis.boportalservice.api.dto.request;

import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public record SidebarMenuRequest(
    String role,
    List<String> roles,
    List<String> roleCodes
) {
  public List<String> resolveRoles() {
    Set<String> normalized = new LinkedHashSet<>();
    if (roles != null) {
      roles.stream().filter(StringUtils::hasText).map(String::trim).forEach(normalized::add);
    }
    if (roleCodes != null) {
      roleCodes.stream().filter(StringUtils::hasText).map(String::trim).forEach(normalized::add);
    }
    if (StringUtils.hasText(role)) {
      normalized.add(role.trim());
    }
    if (normalized.isEmpty()) {
      return Collections.emptyList();
    }
    return List.copyOf(normalized);
  }
}
