package com.fis.boportalservice.infra.feignclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthzRoleDto {
  private String id;
  private String name;
  private String description;
  private String status;
}
