package com.fis.boportalservice.infra.service;

import com.fis.boportalservice.core.domain.model.AuthzResource;
import com.fis.boportalservice.core.domain.model.AuthzRole;
import com.fis.boportalservice.core.domain.model.AuthzRolePermission;
import com.fis.boportalservice.core.service.AuthzServicePort;
import com.fis.boportalservice.infra.feignclient.AuthServiceClient;
import com.fis.boportalservice.infra.feignclient.ResponseFeignClient;
import com.fis.boportalservice.infra.feignclient.dto.AuthzCreateResourceRequest;
import com.fis.boportalservice.infra.feignclient.dto.AuthzCreateRoleRequest;
import com.fis.boportalservice.infra.feignclient.dto.AuthzResourceDto;
import com.fis.boportalservice.infra.feignclient.dto.AuthzRoleDto;
import com.fis.boportalservice.infra.feignclient.dto.AuthzRolePermissionDto;
import com.fis.boportalservice.infra.feignclient.dto.AuthzUpdateRolePermissionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthzServiceAdapter implements AuthzServicePort {

  private final AuthServiceClient authServiceClient;

  @Override
  public AuthzResource createResource(String name, String code, Long categoryId, String description) {
    log.info("Calling auth-service to create resource: name={}, code={}, categoryId={}", name, code, categoryId);
    var request = new AuthzCreateResourceRequest(name, code, categoryId, description);
    ResponseFeignClient<com.fis.boportalservice.infra.feignclient.dto.AuthzResourceDto> response =
        authServiceClient.createResource(request);
    var dto = response.getData() != null ? response.getData() : response.getResult();
    if (dto == null) {
      log.warn("No data returned from auth-service createResource");
      return null;
    }
    return AuthzResource.builder()
        .id(dto.getId())
        .name(dto.getName())
        .code(dto.getCode())
        .description(dto.getDescription())
        .categoryId(dto.getCategoryId())
        .build();
  }

  @Override
  public AuthzRole createRole(String name, String description) {
    log.info("Calling auth-service to create role: name={}", name);
    var request = new AuthzCreateRoleRequest(name, description);
    ResponseFeignClient<AuthzRoleDto> response = authServiceClient.createRole(request);
    var dto = response.getData() != null ? response.getData() : response.getResult();
    if (dto == null) {
      log.warn("No data returned from auth-service createRole");
      return null;
    }
    return AuthzRole.builder()
        .id(dto.getId())
        .name(dto.getName())
        .description(dto.getDescription())
        .status(dto.getStatus())
        .build();
  }

  @Override
  public void updateRolePermissions(Long roleId, List<ResourcePermission> resources) {
    log.info("Calling auth-service to update role permissions: roleId={}", roleId);
    var requestResources = resources.stream()
        .map(r -> new AuthzUpdateRolePermissionRequest.ResourcePermission(r.id(), r.permissions()))
        .collect(Collectors.toList());
    authServiceClient.updateRolePermissions(roleId, new AuthzUpdateRolePermissionRequest(requestResources));
  }

  @Override
  public List<AuthzRole> getRoles() {
    log.info("Calling auth-service to get roles");
    ResponseFeignClient<List<AuthzRoleDto>> response = authServiceClient.getRoles();
    List<AuthzRoleDto> dtos = Optional.ofNullable(response.getData())
        .orElseGet(() -> Optional.ofNullable(response.getResult()).orElse(Collections.emptyList()));
    return dtos.stream()
        .map(dto -> AuthzRole.builder()
            .id(dto.getId())
            .name(dto.getName())
            .description(dto.getDescription())
            .status(dto.getStatus())
            .build())
        .collect(Collectors.toList());
  }

  @Override
  public List<AuthzRolePermission> getRolePermissions(Long roleId) {
    log.info("Calling auth-service to get role permissions: roleId={}", roleId);
    ResponseFeignClient<List<AuthzRolePermissionDto>> response = authServiceClient.getRolePermissions(roleId);
    List<AuthzRolePermissionDto> dtos = Optional.ofNullable(response.getData())
        .orElseGet(() -> Optional.ofNullable(response.getResult()).orElse(Collections.emptyList()));
    return dtos.stream()
        .map(dto -> AuthzRolePermission.builder()
            .resourceId(dto.getResource() != null ? dto.getResource().getId() : null)
            .permissions(dto.getPermissions())
            .build())
        .collect(Collectors.toList());
  }

  @Override
  public List<AuthzResource> getAllResources() {
    log.info("Calling auth-service to get all resources");
    ResponseFeignClient<List<AuthzResourceDto>> response = authServiceClient.getAllResources();
    List<AuthzResourceDto> dtos = Optional.ofNullable(response.getData())
        .orElseGet(() -> Optional.ofNullable(response.getResult()).orElse(Collections.emptyList()));
    return dtos.stream()
        .map(dto -> AuthzResource.builder()
            .id(dto.getId())
            .name(dto.getName())
            .code(dto.getCode())
            .description(dto.getDescription())
            .categoryId(dto.getCategoryId())
            .build())
        .collect(Collectors.toList());
  }
}
