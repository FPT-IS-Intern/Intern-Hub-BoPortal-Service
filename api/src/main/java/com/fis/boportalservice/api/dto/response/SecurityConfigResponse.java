package com.fis.boportalservice.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
