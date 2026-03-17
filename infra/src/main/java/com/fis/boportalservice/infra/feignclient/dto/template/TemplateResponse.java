package com.fis.boportalservice.infra.feignclient.dto.template;

import lombok.Data;

import java.util.Map;

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
  private Long createdAt;
  private Long updatedAt;
  private String description;
  private Map<String, Object> paramsSchema;
}
