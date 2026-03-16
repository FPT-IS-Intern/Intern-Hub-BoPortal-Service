package com.fis.boportalservice.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SystemConfigurationResponse {
  private SystemConfigInternalResponse systemConfig;
  private SecurityConfigResponse securityConfig;
}
