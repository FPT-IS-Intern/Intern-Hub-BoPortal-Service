package com.fis.boportalservice.api.dto.response;

import java.util.List;

public record UserPageResponse<T>(
    List<T> items,
    long totalItems,
    int totalPages
) {
}
