package com.fis.boportalservice.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAuthzResourceRequest(

    @NotBlank(message = "Name is required")
    String name,

    @NotBlank(message = "Code is required")
    String code,

    @NotNull(message = "Category ID is required")
    Long categoryId,

    String description
) {
}
