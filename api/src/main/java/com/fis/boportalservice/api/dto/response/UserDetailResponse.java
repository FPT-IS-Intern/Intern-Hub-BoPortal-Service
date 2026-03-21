package com.fis.boportalservice.api.dto.response;

public record UserDetailResponse(
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
    Boolean activated,
    Boolean deleted
) {
}
