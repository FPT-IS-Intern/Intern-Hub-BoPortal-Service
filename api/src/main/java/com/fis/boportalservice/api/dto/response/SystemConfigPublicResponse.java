package com.fis.boportalservice.api.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 * Response trả về cho frontend (public).
 * Chỉ bao gồm các field UI-related, không expose giờ làm việc nội bộ.
 */
@Data
@Builder
public class SystemConfigPublicResponse {
  private String appName;
  private String logoUrl;
  private String defaultLanguage;
}
