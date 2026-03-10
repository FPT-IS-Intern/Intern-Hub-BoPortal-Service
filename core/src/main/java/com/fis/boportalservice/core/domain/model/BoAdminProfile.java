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
public class BoAdminProfile {
  private UUID id;
  private String username;
  private String displayName;
  private List<String> roles;
  private List<String> permissions;
}

