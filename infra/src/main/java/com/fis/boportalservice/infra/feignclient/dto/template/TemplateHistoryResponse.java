package com.fis.boportalservice.infra.feignclient.dto.template;

import lombok.Data;

@Data
public class TemplateHistoryResponse {
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
}
