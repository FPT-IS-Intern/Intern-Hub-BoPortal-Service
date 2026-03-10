package com.fis.boportalservice.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoAdminUser {
  private UUID id;
  private String username;
  private String passwordHash;
  private String displayName;
  private String status;
  private Integer failedAttempt;
  private LocalDateTime lockedUntil;
  private LocalDateTime lastLoginAt;
  private String roleCodes;
  private String permissionCodes;
}

