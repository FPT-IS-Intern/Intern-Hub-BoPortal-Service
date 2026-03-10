package com.fis.boportalservice.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoAuthSessionResponse {
  private String accessToken;
  private String refreshToken;
  private String tokenType;
  private long expiresIn;
  private long refreshExpiresIn;
  private BoAdminProfileResponse user;
}
