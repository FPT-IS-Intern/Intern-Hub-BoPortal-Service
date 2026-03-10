package com.fis.boportalservice.api.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class AllowedIpRangeRequest {
  private String name;
  private String ipPrefix;
  private Boolean isActive;
  private String description;
  private UUID branchId;
}
