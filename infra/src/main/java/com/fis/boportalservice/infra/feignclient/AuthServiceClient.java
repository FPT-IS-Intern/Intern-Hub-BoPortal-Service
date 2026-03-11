package com.fis.boportalservice.infra.feignclient;

import com.fis.boportalservice.infra.configuration.FeignClientCommonConfiguration;
import com.fis.boportalservice.infra.feignclient.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
    name = "auth-service",
    url = "${feign.client.config.auth-service.url:http://auth-service:8080}",
    configuration = FeignClientCommonConfiguration.class
)
public interface AuthServiceClient {

  @PostMapping("/auth/internal/authz/resources")
  ResponseFeignClient<AuthzResourceDto> createResource(@RequestBody AuthzCreateResourceRequest request);

  @PostMapping("/auth/internal/authz/roles")
  ResponseFeignClient<AuthzRoleDto> createRole(@RequestBody AuthzCreateRoleRequest request);

  @PutMapping("/auth/internal/authz/roles/{roleId}/permissions")
  ResponseFeignClient<Void> updateRolePermissions(
      @PathVariable("roleId") String roleId,
      @RequestBody AuthzUpdateRolePermissionRequest request
  );

  @GetMapping("/auth/internal/authz/roles")
  ResponseFeignClient<List<AuthzRoleDto>> getRoles();

  @GetMapping("/auth/internal/authz/roles/{roleId}/permissions")
  ResponseFeignClient<List<AuthzRolePermissionDto>> getRolePermissions(@PathVariable("roleId") String roleId);

  @GetMapping("/auth/internal/authz/resources")
  ResponseFeignClient<List<AuthzResourceDto>> getAllResources();
}
