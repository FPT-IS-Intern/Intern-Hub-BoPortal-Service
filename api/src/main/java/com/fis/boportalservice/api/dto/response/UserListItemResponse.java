package com.fis.boportalservice.api.dto.response;

public record UserListItemResponse(
    Integer no,
    String userId,
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

