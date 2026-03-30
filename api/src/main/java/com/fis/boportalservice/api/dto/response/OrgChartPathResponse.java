package com.fis.boportalservice.api.dto.response;

import java.util.List;

public record OrgChartPathResponse(
    List<OrgChartUserLiteResponse> data
) {
}
