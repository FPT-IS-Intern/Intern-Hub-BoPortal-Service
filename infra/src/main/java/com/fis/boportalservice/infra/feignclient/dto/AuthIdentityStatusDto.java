package com.fis.boportalservice.infra.feignclient.dto;

import lombok.Data;

@Data
public class AuthIdentityStatusDto {
  private Long userId;
  private String sysStatus;
}
