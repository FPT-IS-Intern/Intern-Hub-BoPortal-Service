package com.fis.boportalservice.api.dto.response;

public record UserListItemResponse(
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
