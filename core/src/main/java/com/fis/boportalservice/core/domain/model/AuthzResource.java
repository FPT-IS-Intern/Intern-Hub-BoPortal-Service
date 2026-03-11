package com.fis.boportalservice.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthzResource {
  private String id;
  private String name;
  private String code;
  private String description;
  private String categoryId;
}
