package com.fis.boportalservice.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BoPortalAllowedIpRangeResponse {
  private UUID id;
  private String name;
  private String ipPrefix;
  private Boolean isActive;
  private String description;
  private UUID branchId;
}
