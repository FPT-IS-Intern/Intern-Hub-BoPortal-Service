package com.fis.boportalservice.api.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public record AuthzResourceResponse(
    @JsonSerialize(using = ToStringSerializer.class) Long id,
    String name,
    String code,
    String description,
    @JsonSerialize(using = ToStringSerializer.class) Long categoryId
) {
}
