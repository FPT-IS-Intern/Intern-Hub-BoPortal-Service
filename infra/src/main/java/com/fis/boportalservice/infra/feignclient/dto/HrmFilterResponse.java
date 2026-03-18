package com.fis.boportalservice.infra.feignclient.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HrmFilterResponse {
  Integer no;
  Long userId;

  String avatarUrl;
  String fullName;
  String sysStatus;
  String email;
  String role;
  String position;
}
