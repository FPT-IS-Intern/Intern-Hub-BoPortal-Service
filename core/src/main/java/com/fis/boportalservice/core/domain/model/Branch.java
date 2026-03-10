package com.fis.boportalservice.core.domain.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Branch {
  private UUID id;
  private String name;
  private String description;
  private Boolean isActive;
}
