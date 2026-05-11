package com.fis.boportalservice.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class SystemConfigWorkingTimeResponse {
  private LocalTime workStartTime;
  private LocalTime workEndTime;
  private LocalTime autoCheckoutTime;
  private Integer attendanceOffsetMinutes;
}
