package com.fis.boportalservice.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditItemResponse {

  private String id;
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
