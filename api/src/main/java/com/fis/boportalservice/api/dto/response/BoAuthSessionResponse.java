package com.fis.boportalservice.api.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
  @JsonSerialize(using = ToStringSerializer.class)
  private long expiresIn;
  @JsonSerialize(using = ToStringSerializer.class)
  private long refreshExpiresIn;
  private BoAdminProfileResponse user;
}
