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

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthzServiceAdapter implements AuthzServicePort {

  private final AuthServiceClient authServiceClient;

  @Override
  public AuthzResource createResource(String name, String code, String categoryId, String description) {
    log.info("event=AUTH_SERVICE_RESOURCE_CREATE_REQUEST name={} code={} categoryId={}", name, code, categoryId);
    var request = new AuthzCreateResourceRequest(name, code, categoryId, description);
    var dto = extractPayload(authServiceClient.createResource(request));
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
    log.info("event=AUTH_SERVICE_ROLE_CREATE_REQUEST name={}", name);
    var request = new AuthzCreateRoleRequest(name, description);
    var dto = extractPayload(authServiceClient.createRole(request));
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
  public void updateRolePermissions(String roleId, List<ResourcePermission> resources) {
    log.info("event=AUTH_SERVICE_ROLE_PERMISSION_UPDATE_REQUEST roleId={}", roleId);
    var requestResources = resources.stream()
        .map(r -> new AuthzUpdateRolePermissionRequest.ResourcePermission(r.id(), r.permissions()))
        .toList();
    authServiceClient.updateRolePermissions(roleId, new AuthzUpdateRolePermissionRequest(requestResources));
  }

  @Override
  public List<AuthzRole> getRoles() {
    log.info("event=AUTH_SERVICE_ROLE_LIST_REQUEST");
    List<AuthzRoleDto> dtos = extractListPayload(authServiceClient.getRoles());
    return dtos.stream()
        .map(this::toRole)
        .toList();
  }

  @Override
  public List<AuthzRolePermission> getRolePermissions(String roleId) {
    log.info("event=AUTH_SERVICE_ROLE_PERMISSION_LIST_REQUEST roleId={}", roleId);
    List<AuthzRolePermissionDto> dtos = extractListPayload(authServiceClient.getRolePermissions(roleId));
    return dtos.stream()
        .map(this::toRolePermission)
        .toList();
  }

  @Override
  public List<AuthzResource> getAllResources() {
    log.info("event=AUTH_SERVICE_RESOURCE_LIST_REQUEST");
    List<AuthzResourceDto> dtos = extractListPayload(authServiceClient.getAllResources());
    return dtos.stream()
        .map(this::toResource)
        .toList();
  }

  private AuthzRole toRole(AuthzRoleDto dto) {
    return AuthzRole.builder()
        .id(dto.getId())
        .name(dto.getName())
        .description(dto.getDescription())
        .status(dto.getStatus())
        .build();
  }

  private AuthzRolePermission toRolePermission(AuthzRolePermissionDto dto) {
    return AuthzRolePermission.builder()
        .resourceId(dto.getResource() != null ? dto.getResource().getId() : null)
        .permissions(dto.getPermissions())
        .build();
  }

  private AuthzResource toResource(AuthzResourceDto dto) {
    return AuthzResource.builder()
        .id(dto.getId())
        .name(dto.getName())
        .code(dto.getCode())
        .description(dto.getDescription())
        .categoryId(dto.getCategoryId())
        .build();
  }

  private <T> T extractPayload(ResponseFeignClient<T> response) {
    if (response == null) {
      return null;
    }
    return response.getData() != null ? response.getData() : response.getResult();
  }

  private <T> List<T> extractListPayload(ResponseFeignClient<List<T>> response) {
    List<T> payload = extractPayload(response);
    return payload != null ? payload : Collections.emptyList();
  }
}

