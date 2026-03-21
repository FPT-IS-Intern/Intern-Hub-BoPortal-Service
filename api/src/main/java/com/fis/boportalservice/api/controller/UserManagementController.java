package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.AssignUserRoleRequest;
import com.fis.boportalservice.api.dto.request.UserFilterRequest;
import com.fis.boportalservice.api.dto.request.UserProfileUpdateRequest;
import com.fis.boportalservice.api.dto.request.UserRejectRequest;
import com.fis.boportalservice.api.dto.request.UserSuspendRequest;
import com.fis.boportalservice.api.dto.response.AuthzRoleResponse;
import com.fis.boportalservice.api.dto.response.UserDetailResponse;
import com.fis.boportalservice.api.dto.response.UserHistoryResponse;
import com.fis.boportalservice.api.dto.response.UserListItemResponse;
import com.fis.boportalservice.api.dto.response.UserMetaResponse;
import com.fis.boportalservice.api.dto.response.UserPageResponse;
import com.fis.boportalservice.api.dto.response.UserRoleResponse;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.service.UserManagementServicePort;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bo-portal/users")
@Tag(name = "6. Admin - User Management")
public class UserManagementController {

  private final UserManagementServicePort userManagementServicePort;

  @PostMapping("/search")
  public ResponseApi<UserPageResponse<UserListItemResponse>> searchUsers(
      @RequestBody(required = false) UserFilterRequest request,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    UserFilterRequest filterRequest = request != null ? request : new UserFilterRequest(null, null, null, null, null);
    var result = userManagementServicePort.filterUsers(
        new UserManagementServicePort.UserFilterCriteria(
            filterRequest.keyword(),
            filterRequest.sysStatuses(),
            filterRequest.roles(),
            filterRequest.positions(),
            filterRequest.departments()
        ),
        Math.max(page - 1, 0),
        size
    );

    UserPageResponse<UserListItemResponse> response = new UserPageResponse<>(
        result.items().stream()
            .map(item -> new UserListItemResponse(
                item.no(),
                item.userId(),
                item.avatarUrl(),
                item.fullName(),
                item.sysStatus(),
                item.email(),
                item.role(),
                item.position(),
                item.department(),
                item.deleted()
            ))
            .toList(),
        result.totalItems(),
        result.totalPages()
    );
    return ResponseApi.success(response);
  }

  @GetMapping("/meta")
  public ResponseApi<UserMetaResponse> getMetaOptions() {
    var meta = userManagementServicePort.getMetaOptions();
    return ResponseApi.success(new UserMetaResponse(meta.roles(), meta.positions(), meta.departments()));
  }

  @GetMapping("/{userId}")
  public ResponseApi<UserDetailResponse> getUserById(@PathVariable Long userId) {
    log.info("Request to get user detail: userId={}", userId);
    return ResponseApi.success(toDetailResponse(userManagementServicePort.getUserById(userId)));
  }

  @PutMapping("/{userId}/lock")
  public ResponseApi<UserDetailResponse> lockUser(@PathVariable Long userId) {
    log.info("Request to lock user: userId={}", userId);
    return ResponseApi.success(toDetailResponse(userManagementServicePort.lockUser(userId)));
  }

  @PutMapping("/{userId}/unlock")
  public ResponseApi<UserDetailResponse> unlockUser(@PathVariable Long userId) {
    log.info("Request to unlock user: userId={}", userId);
    return ResponseApi.success(toDetailResponse(userManagementServicePort.unlockUser(userId)));
  }

  @PutMapping("/{userId}/approve")
  public ResponseApi<UserDetailResponse> approveUser(@PathVariable Long userId) {
    return ResponseApi.success(toDetailResponse(userManagementServicePort.approveUser(userId)));
  }

  @PutMapping("/{userId}/reject")
  public ResponseApi<UserDetailResponse> rejectUser(@PathVariable Long userId, @RequestBody(required = false) UserRejectRequest request) {
    return ResponseApi.success(toDetailResponse(userManagementServicePort.rejectUser(userId, request != null ? request.reason() : null)));
  }

  @PutMapping("/{userId}/suspend")
  public ResponseApi<UserDetailResponse> suspendUser(@PathVariable Long userId, @RequestBody(required = false) UserSuspendRequest request) {
    return ResponseApi.success(toDetailResponse(userManagementServicePort.suspendUser(userId, request != null ? request.reason() : null)));
  }

  @PutMapping("/{userId}/reactivate")
  public ResponseApi<UserDetailResponse> reactivateUser(@PathVariable Long userId) {
    return ResponseApi.success(toDetailResponse(userManagementServicePort.reactivateUser(userId)));
  }

  @PostMapping("/{userId}/reset-password")
  public ResponseApi<UserDetailResponse> resetPassword(@PathVariable Long userId) {
    return ResponseApi.success(toDetailResponse(userManagementServicePort.resetPassword(userId)));
  }

  @PatchMapping("/{userId}/profile")
  public ResponseApi<UserDetailResponse> updateProfile(@PathVariable Long userId, @RequestBody UserProfileUpdateRequest request) {
    return ResponseApi.success(toDetailResponse(userManagementServicePort.updateProfile(
        userId,
        new UserManagementServicePort.UserProfileUpdateCommand(
            request.fullName(),
            request.phoneNumber(),
            request.positionCode(),
            request.department()
        )
    )));
  }

  @GetMapping("/{userId}/role")
  public ResponseApi<UserRoleResponse> getUserRoles(@PathVariable Long userId) {
    return ResponseApi.success(toRoleResponse(userManagementServicePort.getUserRoles(userId)));
  }

  @PutMapping("/{userId}/role")
  public ResponseApi<UserRoleResponse> assignRole(@PathVariable Long userId, @RequestBody AssignUserRoleRequest request) {
    return ResponseApi.success(toRoleResponse(userManagementServicePort.assignRole(userId, request.roleId())));
  }

  @GetMapping("/{userId}/activity-history")
  public ResponseApi<List<UserHistoryResponse>> getActivityHistory(@PathVariable Long userId) {
    return ResponseApi.success(userManagementServicePort.getActivityHistory(userId).stream()
        .map(item -> new UserHistoryResponse(item.id(), item.title(), item.description(), item.createdAt(), item.actor()))
        .toList());
  }

  @GetMapping("/{userId}/login-history")
  public ResponseApi<List<UserHistoryResponse>> getLoginHistory(@PathVariable Long userId) {
    return ResponseApi.success(userManagementServicePort.getLoginHistory(userId).stream()
        .map(item -> new UserHistoryResponse(item.id(), item.title(), item.description(), item.createdAt(), item.actor()))
        .toList());
  }

  private UserDetailResponse toDetailResponse(UserManagementServicePort.UserDetail user) {
    if (user == null) {
      return null;
    }
    return new UserDetailResponse(
        user.userId(),
        user.email(),
        user.fullName(),
        user.phoneNumber(),
        user.avatarUrl(),
        user.positionCode(),
        user.role(),
        user.status(),
        user.loginStatus(),
        user.department(),
        user.activated(),
        user.deleted()
    );
  }

  private UserRoleResponse toRoleResponse(UserManagementServicePort.UserRoleResult result) {
    if (result == null) {
      return null;
    }
    return new UserRoleResponse(
        result.userId(),
        result.roles().stream()
            .map(role -> new AuthzRoleResponse(role.id(), role.code(), role.name(), role.description(), null))
            .toList()
    );
  }
}
