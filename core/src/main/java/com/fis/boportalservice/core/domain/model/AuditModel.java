package com.fis.boportalservice.core.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuditModel {

  private Long id;
  private String entity;
  private String actor;
  private String actorId;
  private String action;
  private String actionDescription;
  private String actionStatus;
  private String oldValue;
  private String newValue;
  private String requestId;
  private String traceId;
  private String ipAddress;
  private Long timeStamp;
  private String hash;

}
