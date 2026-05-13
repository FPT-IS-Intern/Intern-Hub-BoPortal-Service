package com.fis.boportalservice.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response trả về cho frontend (public).
 * Chỉ bao gồm các field UI-related, không expose giờ làm việc nội bộ.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfigPublicResponse {
  private String appName;
  private String logoUrl;
  private String defaultLanguage;
}
