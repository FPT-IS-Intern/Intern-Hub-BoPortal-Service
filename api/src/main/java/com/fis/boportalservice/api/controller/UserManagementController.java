package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.UserFilterRequest;
import com.fis.boportalservice.api.dto.response.UserDetailResponse;
import com.fis.boportalservice.api.dto.response.UserListItemResponse;
import com.fis.boportalservice.api.dto.response.UserPageResponse;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.service.UserManagementServicePort;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    UserFilterRequest filterRequest = request != null ? request : new UserFilterRequest(null, null, null, null);
    var result = userManagementServicePort.filterUsers(
        new UserManagementServicePort.UserFilterCriteria(
            filterRequest.keyword(),
            filterRequest.sysStatuses(),
            filterRequest.roles(),
            filterRequest.positions()
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
                item.position()
            ))
            .toList(),
        result.totalItems(),
        result.totalPages()
    );
    return ResponseApi.success(response);
  }

  @GetMapping("/{userId}")
  public ResponseApi<UserDetailResponse> getUserById(@PathVariable Long userId) {
    log.info("Request to get user detail: userId={}", userId);
    var user = userManagementServicePort.getUserById(userId);
    return ResponseApi.success(toDetailResponse(user));
  }

  @PutMapping("/{userId}/lock")
  public ResponseApi<UserDetailResponse> lockUser(@PathVariable Long userId) {
    log.info("Request to lock user: userId={}", userId);
    var user = userManagementServicePort.lockUser(userId);
    return ResponseApi.success(toDetailResponse(user));
  }

  @PutMapping("/{userId}/unlock")
  public ResponseApi<UserDetailResponse> unlockUser(@PathVariable Long userId) {
    log.info("Request to unlock user: userId={}", userId);
    var user = userManagementServicePort.unlockUser(userId);
    return ResponseApi.success(toDetailResponse(user));
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
        user.positionCode(),
        user.role(),
        user.status()
    );
  }
}
