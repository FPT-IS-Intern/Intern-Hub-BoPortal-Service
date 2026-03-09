package com.fis.boportalservice.api.dto.response;

public record AuthzResourceResponse(
    Long id,
    String name,
    String code,
    String description,
    Long categoryId
) {
}
