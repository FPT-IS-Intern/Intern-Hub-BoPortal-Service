package com.fis.boportalservice.core.service;

import java.util.List;

public interface UserManagementServicePort {

  UserPageResult filterUsers(UserFilterCriteria criteria, int page, int size);

  UserDetail getUserById(Long userId);

  UserDetail lockUser(Long userId);

  UserDetail unlockUser(Long userId);

  record UserFilterCriteria(
      String keyword,
      List<String> sysStatuses,
      List<String> roles,
      List<String> positions
  ) {
  }

  record UserListItem(
      Integer no,
      Long userId,
      String avatarUrl,
      String fullName,
      String sysStatus,
      String email,
      String role,
      String position
  ) {
  }

  record UserDetail(
      Long userId,
      String email,
      String fullName,
      String phoneNumber,
      String positionCode,
      String role,
      String status
  ) {
  }

  record UserPageResult(
      List<UserListItem> items,
      long totalItems,
      int totalPages
  ) {
  }
}
