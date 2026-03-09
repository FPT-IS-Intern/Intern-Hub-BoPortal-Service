package com.fis.boportalservice.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateAuthzRoleRequest(

        @NotBlank(message = "Name is required")
        String name,

        String description
) {
}
