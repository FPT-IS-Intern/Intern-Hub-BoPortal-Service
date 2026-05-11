package com.fis.boportalservice.api.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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
