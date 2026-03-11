package com.fis.boportalservice.api.dto.response;

import java.util.List;

public record AuthzRolePermissionResponse(
    Resource resource,
    List<String> permissions
) {

  public record Resource(
      String id
  ) {}
}
