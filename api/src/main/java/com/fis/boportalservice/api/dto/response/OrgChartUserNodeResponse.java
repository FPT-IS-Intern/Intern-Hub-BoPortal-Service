package com.fis.boportalservice.api.dto.response;

import java.util.List;

public record OrgChartUserNodeResponse(
    String id,
    String name,
    String title,
    String department,
    String avatar,
    String email,
    String phone,
    String status,
    String joinedDate,
    String location,
    String managerId,
    boolean hasChildren,
    long subordinateCount,
    List<OrgChartUserNodeResponse> children
) {
}
