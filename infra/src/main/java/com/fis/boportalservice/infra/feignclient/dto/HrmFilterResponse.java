package com.fis.boportalservice.infra.feignclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
