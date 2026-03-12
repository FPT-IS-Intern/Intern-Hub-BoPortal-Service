package com.fis.boportalservice.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SecurityConfigResponse {
  private Integer minPasswordLength;
  private Integer maxPasswordLength;
  private Integer minUppercaseChars;
  private Integer minSpecialChars;
  private Integer minNumericChars;
  private Integer passwordExpiryDays;
  private Boolean allowWhitespace;
  private Integer autoLogoutMinutes;
  private Integer maxLoginAttempts;
}
