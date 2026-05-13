package com.fis.boportalservice.api.dto.request;

import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class SystemConfigRequest {
  private String appName;
  private String logoUrl;
  private String defaultLanguage;
  private LocalTime workStartTime;
  private LocalTime workEndTime;
  private LocalTime autoCheckoutTime;
  private Integer attendanceOffsetMinutes;
  private UUID updatedBy;
}
