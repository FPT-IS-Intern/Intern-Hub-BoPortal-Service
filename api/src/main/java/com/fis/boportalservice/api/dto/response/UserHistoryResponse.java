package com.fis.boportalservice.api.dto.response;

public record UserHistoryResponse(
    Long id,
    String title,
    String description,
    String createdAt,
    String actor
) {
}
