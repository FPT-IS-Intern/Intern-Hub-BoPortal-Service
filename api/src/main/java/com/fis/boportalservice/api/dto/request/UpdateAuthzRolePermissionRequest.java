package com.fis.boportalservice.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateAuthzRolePermissionRequest(

    @NotEmpty(message = "Resources list must not be empty")
    @Valid
    List<ResourcePermission> resources
) {

  public record ResourcePermission(

      @NotNull(message = "Resource ID is required")
      Long id,

      @NotNull(message = "Permissions are required")
      @Size(min = 5, max = 5, message = "Permissions must have exactly 5 elements")
      List<Integer> permissions
  ) {
  }
}
