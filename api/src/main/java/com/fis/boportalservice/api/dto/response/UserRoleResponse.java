package com.fis.boportalservice.api.dto.response;

import java.util.List;

public record UserRoleResponse(
    Long userId,
    List<AuthzRoleResponse> roles
) {
}
