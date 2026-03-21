package com.fis.boportalservice.api.dto.response;

import java.util.List;

public record UserRoleResponse(
    String userId,
    List<AuthzRoleResponse> roles
) {
}

