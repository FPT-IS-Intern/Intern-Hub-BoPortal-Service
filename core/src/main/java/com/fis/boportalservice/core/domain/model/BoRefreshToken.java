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
public class BoRefreshToken {
  private UUID id;
  private UUID userId;
  private String tokenHash;
  private String tokenJti;
  private String deviceId;
  private LocalDateTime expiresAt;
  private LocalDateTime revokedAt;
  private LocalDateTime createdAt;
}

