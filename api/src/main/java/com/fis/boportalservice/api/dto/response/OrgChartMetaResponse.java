package com.fis.boportalservice.api.dto.response;

public record OrgChartMetaResponse(
    long total,
    int page,
    int limit,
    int totalPages
) {
}
