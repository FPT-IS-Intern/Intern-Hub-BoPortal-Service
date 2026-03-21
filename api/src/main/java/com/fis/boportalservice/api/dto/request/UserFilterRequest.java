package com.fis.boportalservice.api.dto.request;

import java.util.List;

public record UserFilterRequest(
    String keyword,
    List<String> sysStatuses,
    List<String> roles,
    List<String> positions,
    List<String> departments
) {
}
