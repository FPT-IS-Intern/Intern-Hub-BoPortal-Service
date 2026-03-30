package com.fis.boportalservice.api.dto.response;

import java.util.List;

public record OrgChartPageResponse<T>(
    List<T> data,
    OrgChartMetaResponse meta
) {
}
