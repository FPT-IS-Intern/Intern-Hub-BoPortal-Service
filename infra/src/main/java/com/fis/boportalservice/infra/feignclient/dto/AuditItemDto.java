package com.fis.boportalservice.infra.feignclient.dto;


import lombok.Data;

@Data
public class AuditItemDto {

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
