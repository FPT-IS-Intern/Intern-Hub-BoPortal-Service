package com.fis.boportalservice.api.dto.response;

import java.util.List;

public record OrgChartUserDetailResponse(
    String id,
    String name,
    String title,
    OrgChartDepartmentResponse department,
    String avatar,
    String email,
    String phone,
    String status,
    String joinedDate,
    String location,
    OrgChartUserLiteResponse manager,
    List<OrgChartUserLiteResponse> subordinates,
    List<String> projects,
    boolean hasChildren,
    long subordinateCount
) {
}
