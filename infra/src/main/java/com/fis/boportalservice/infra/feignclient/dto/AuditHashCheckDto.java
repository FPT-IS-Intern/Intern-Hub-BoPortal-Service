package com.fis.boportalservice.infra.feignclient.dto;

import lombok.Data;

@Data
public class AuditHashCheckDto {

  private Long auditId;
  private boolean valid;

}
