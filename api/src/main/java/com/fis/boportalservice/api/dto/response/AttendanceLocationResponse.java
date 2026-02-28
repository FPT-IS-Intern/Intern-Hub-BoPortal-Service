package com.fis.boportalservice.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceLocationResponse {
    private String name;
    private Double latitude;
    private Double longitude;
    private Integer radiusMeters;
}
