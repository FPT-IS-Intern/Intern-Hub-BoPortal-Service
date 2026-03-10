package com.fis.boportalservice.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllowedIpRange {
  private UUID id;
  private String name;
  private String ipPrefix;
  private Boolean isActive;
  private String description;
  private UUID branchId;
}
