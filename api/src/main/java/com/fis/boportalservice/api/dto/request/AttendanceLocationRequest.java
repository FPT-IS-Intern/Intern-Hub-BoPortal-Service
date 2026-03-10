package com.fis.boportalservice.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceLocationRequest {
  private String name;
  private Double latitude;
  private Double longitude;
  private Integer radiusMeters;
  private Boolean isActive;
  private UUID branchId;
}
