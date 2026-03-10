package com.fis.boportalservice.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoTokenClaims {
  private UUID userId;
  private String username;
  private List<String> roles;
  private List<String> permissions;
  private String jti;
}

