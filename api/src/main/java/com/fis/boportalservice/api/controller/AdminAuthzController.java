package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.CreateAuthzResourceRequest;
import com.fis.boportalservice.api.dto.request.CreateAuthzRoleRequest;
import com.fis.boportalservice.api.dto.request.UpdateAuthzRolePermissionRequest;
import com.fis.boportalservice.api.dto.response.AuthzResourceResponse;
import com.fis.boportalservice.api.dto.response.AuthzRoleResponse;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.domain.model.AuthzResource;
import com.fis.boportalservice.core.domain.model.AuthzRole;
import com.fis.boportalservice.core.service.AuthzServicePort;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "5. Admin - Authorization (Auth Service)")
@RestController
@RequestMapping("/bo-portal/authz")
@RequiredArgsConstructor
public class AdminAuthzController {

    private final AuthzServicePort authzServicePort;

    @PostMapping("/resources")
    public ResponseApi<AuthzResourceResponse> createResource(
            @Valid @RequestBody CreateAuthzResourceRequest request) {
        log.info("Request to create authz resource: name={}, code={}", request.name(), request.code());
        AuthzResource resource = authzServicePort.createResource(
                request.name(),
                request.code(),
                request.categoryId(),
                request.description()
        );
        return ResponseApi.success(toResourceResponse(resource));
    }

    @PostMapping("/roles")
    public ResponseApi<AuthzRoleResponse> createRole(
            @Valid @RequestBody CreateAuthzRoleRequest request) {
        log.info("Request to create authz role: name={}", request.name());
        AuthzRole role = authzServicePort.createRole(request.name(), request.description());
        return ResponseApi.success(new AuthzRoleResponse(role.getId(), role.getName(), role.getDescription(), role.getStatus()));
    }

    @PutMapping("/roles/{roleId}/permissions")
    public ResponseApi<Void> updateRolePermissions(
            @PathVariable Long roleId,
            @Valid @RequestBody UpdateAuthzRolePermissionRequest request) {
        log.info("Request to update permissions for roleId={}", roleId);
        List<AuthzServicePort.ResourcePermission> permissions = request.resources().stream()
                .map(r -> new AuthzServicePort.ResourcePermission(r.id(), r.permissions()))
                .toList();
        authzServicePort.updateRolePermissions(roleId, permissions);
        return ResponseApi.success();
    }

    @GetMapping("/roles")
    public ResponseApi<List<AuthzRoleResponse>> getRoles() {
        log.info("Request to get all authz roles");
        List<AuthzRoleResponse> roles = authzServicePort.getRoles().stream()
                .map(r -> new AuthzRoleResponse(r.getId(), r.getName(), r.getDescription(), r.getStatus()))
                .toList();
        return ResponseApi.success(roles);
    }

    private AuthzResourceResponse toResourceResponse(AuthzResource resource) {
        if (resource == null) return null;
        return new AuthzResourceResponse(
                resource.getId(),
                resource.getName(),
                resource.getCode(),
                resource.getDescription(),
                resource.getCategoryId()
        );
    }
}
