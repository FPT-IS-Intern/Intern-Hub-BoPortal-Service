package com.fis.boportalservice.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityConfig {
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
