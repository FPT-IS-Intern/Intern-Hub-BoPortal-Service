package com.fis.boportalservice.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoLogoutRequest {
  @NotBlank(message = "refreshToken is required")
  private String refreshToken;
}
