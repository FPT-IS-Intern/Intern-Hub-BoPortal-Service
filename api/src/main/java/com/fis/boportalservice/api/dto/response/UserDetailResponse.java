package com.fis.boportalservice.api.dto.response;

public record UserDetailResponse(
    Long userId,
    String email,
    String fullName,
    String phoneNumber,
    String positionCode,
    String role,
    String status
) {
}
