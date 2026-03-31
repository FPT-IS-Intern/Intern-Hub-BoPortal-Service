package com.fis.boportalservice.api.dto.response;

import java.util.List;

public record OrgChartBulkManagerUpdateResponse(
    List<String> updatedUserIds,
    String managerId,
    int updatedCount
) {
}
