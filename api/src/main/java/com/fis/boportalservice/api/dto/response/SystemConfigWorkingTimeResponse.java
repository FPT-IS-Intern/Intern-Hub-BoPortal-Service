package com.fis.boportalservice.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfigWorkingTimeResponse {
  private LocalTime workStartTime;
  private LocalTime workEndTime;
  private LocalTime autoCheckoutTime;
  private Integer attendanceOffsetMinutes;
}
