package com.fis.boportalservice.infra.feignclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthzUpdateRolePermissionRequest {
  private List<ResourcePermission> resources;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ResourcePermission {
    private Long id;
    private List<Integer> permissions;
  }
}
