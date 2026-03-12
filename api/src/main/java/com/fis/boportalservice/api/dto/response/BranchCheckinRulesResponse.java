package com.fis.boportalservice.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class BranchCheckinRulesResponse {
  private UUID id;
  private String name;
  private String description;
  private Boolean isActive;
  private List<BoPortalAllowedIpRangeResponse> allowedIpRanges;
  private List<AttendanceLocationResponse> attendanceLocations;
}
