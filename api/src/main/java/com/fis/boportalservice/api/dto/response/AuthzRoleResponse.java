package com.fis.boportalservice.api.dto.response;

public record AuthzRoleResponse(
    String id,
    String name,
    String description,
    String status
) {
}
