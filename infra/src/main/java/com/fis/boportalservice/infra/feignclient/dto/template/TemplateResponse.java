package com.fis.boportalservice.infra.feignclient.dto.template;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TemplateResponse {
  private String id;
  private String code;
  private String channel;
  private String locale;
  private String subject;
  private String content;
  private String format;
  private Boolean active;
  private Integer templateVersion;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String description;
  private String paramsSchema;
}
