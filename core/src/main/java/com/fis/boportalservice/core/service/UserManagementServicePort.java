package com.fis.boportalservice.core.service;

import java.util.List;

public interface UserManagementServicePort {

  UserPageResult filterUsers(UserFilterCriteria criteria, int page, int size);

  UserDetail getUserById(Long userId);

  UserMetaOptions getMetaOptions();

  UserDetail lockUser(Long userId);

  UserDetail unlockUser(Long userId);

  UserDetail approveUser(Long userId);

  UserDetail rejectUser(Long userId, String reason);

  UserDetail suspendUser(Long userId, String reason);

  UserDetail reactivateUser(Long userId);

  UserDetail resetPassword(Long userId);

  UserDetail updateProfile(Long userId, UserProfileUpdateCommand command);

  UserRoleResult getUserRoles(Long userId);

  UserRoleResult assignRole(Long userId, String roleId);

  List<UserHistoryItem> getActivityHistory(Long userId);

  List<UserHistoryItem> getLoginHistory(Long userId);

  record UserFilterCriteria(
      String keyword,
      List<String> sysStatuses,
      List<String> roles,
      List<String> positions,
      List<String> departments
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
      String position,
      String department,
      Boolean deleted
  ) {
  }

  record UserDetail(
      Long userId,
      String email,
      String fullName,
      String phoneNumber,
      String avatarUrl,
      String positionCode,
      String role,
      String status,
      String loginStatus,
      String department,
      String mentorName,
      Long mentorId,
      Boolean activated,
      Boolean deleted
  ) {
  }

  record UserPageResult(
      List<UserListItem> items,
      long totalItems,
      int totalPages
  ) {
  }

  record UserMetaOptions(
      List<String> roles,
      List<String> positions,
      List<String> departments
  ) {
  }

  record UserProfileUpdateCommand(
      String fullName,
      String phoneNumber,
      String positionCode,
      String department
  ) {
  }

  record UserRole(
      String id,
      String code,
      String name,
      String description
  ) {
  }

  record UserRoleResult(
      Long userId,
      List<UserRole> roles
  ) {
  }

  record UserHistoryItem(
      Long id,
      String title,
      String description,
      String createdAt,
      String actor
  ) {
  }
}
