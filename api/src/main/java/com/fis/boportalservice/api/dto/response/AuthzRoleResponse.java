package com.fis.boportalservice.api.dto.response;

public record AuthzRoleResponse(
    String id,
    String code,
    String name,
    String description,
    String status
) {
}
