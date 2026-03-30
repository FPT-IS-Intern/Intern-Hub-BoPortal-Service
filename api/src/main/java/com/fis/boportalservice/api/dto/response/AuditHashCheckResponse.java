package com.fis.boportalservice.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditHashCheckResponse {

  private String auditId;
  private boolean valid;

}
