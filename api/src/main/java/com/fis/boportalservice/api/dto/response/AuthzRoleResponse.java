package com.fis.boportalservice.api.dto.response;

public record AuthzRoleResponse(
    Long id,
    String name,
    String description,
    String status
) {
}
