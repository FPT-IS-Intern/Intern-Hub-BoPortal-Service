package com.fis.boportalservice.api.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.List;

public record AuthzRolePermissionResponse(
    Resource resource,
    List<String> permissions
) {

  public record Resource(
      @JsonSerialize(using = ToStringSerializer.class)
      Long id
  ) {}
}
