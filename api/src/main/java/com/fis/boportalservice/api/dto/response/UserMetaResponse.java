package com.fis.boportalservice.api.dto.response;

import java.util.List;

public record UserMetaResponse(
    List<String> roles,
    List<String> positions,
    List<String> departments
) {
}
