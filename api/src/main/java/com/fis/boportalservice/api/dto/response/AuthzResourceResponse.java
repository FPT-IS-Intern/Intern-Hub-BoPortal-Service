package com.fis.boportalservice.api.dto.response;

public record AuthzResourceResponse(
    String id,
    String name,
    String code,
    String description,
    String categoryId
) {
}
