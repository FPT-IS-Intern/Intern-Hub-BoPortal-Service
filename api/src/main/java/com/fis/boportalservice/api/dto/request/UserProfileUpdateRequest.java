package com.fis.boportalservice.api.dto.request;

public record UserProfileUpdateRequest(
    String fullName,
    String phoneNumber,
    String positionCode,
    String department
) {
}
