package com.fis.boportalservice.infra.feignclient.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HrmUserResponse {
  private Long userId;
  private String email;
  private String fullName;
  private String phoneNumber;
  private String avatarUrl;
  private String positionCode;
  private String roleId;
  private String idNumber;
  private String address;
  private LocalDate dateOfBirth;
  private String department;
  @JsonAlias({"sysStatus", "status"})
  private String sysStatus;
}
