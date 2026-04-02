package com.fis.boportalservice.api.dto.response;

public record UserDetailResponse(
    String userId,
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
    String mentorId,
    Boolean activated,
    Boolean deleted
) {
}

