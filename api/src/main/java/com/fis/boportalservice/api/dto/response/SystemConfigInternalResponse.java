package com.fis.boportalservice.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

/**
 * Response trả về cho các service nội bộ qua FeignClient.
 * Bao gồm đầy đủ các field cấu hình, kể cả giờ làm việc.
 */
@Data
@Builder
public class SystemConfigInternalResponse {
  private String appName;
  private String logoUrl;
  private String defaultLanguage;
  private LocalTime workStartTime;
  private LocalTime workEndTime;
  private LocalTime autoCheckoutTime;
}
