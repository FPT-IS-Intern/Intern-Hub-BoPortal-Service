package com.fis.boportalservice.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemConfig {
  private String appName;
  private String logoUrl;
  private String defaultLanguage;
  private LocalTime workStartTime;
  private LocalTime workEndTime;
  private LocalTime autoCheckoutTime;
  private Integer attendanceOffsetMinutes;
  private UUID updatedBy;
}
