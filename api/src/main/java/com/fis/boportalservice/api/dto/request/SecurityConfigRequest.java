package com.fis.boportalservice.api.dto.request;



import lombok.Data;

import java.util.UUID;

@Data
public class SecurityConfigRequest {
  private Integer minPasswordLength;
  private Integer maxPasswordLength;
  private Integer minUppercaseChars;
  private Integer minSpecialChars;
  private Integer minNumericChars;
  private Integer passwordExpiryDays;
  private Boolean allowWhitespace;
  private Integer autoLogoutMinutes;
  private Integer maxLoginAttempts;
  private UUID updatedBy;
}
