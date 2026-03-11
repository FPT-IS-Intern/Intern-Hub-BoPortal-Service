package com.fis.boportalservice.infra.feignclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthzCreateResourceRequest {
  private String name;
  private String code;
  private String categoryId;
  private String description;
}
