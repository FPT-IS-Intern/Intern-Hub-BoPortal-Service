package com.fis.boportalservice.api.dto.request;

import java.util.List;

public record OrgChartBulkManagerUpdateRequest(
    List<Long> userIds,
    Long managerId
) {
}
