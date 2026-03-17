package com.fis.boportalservice.infra.feignclient.dto.template;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TemplateUpsertRequest {
  @NotBlank
  private String code;
  @NotBlank
  private String channel;
  private String locale;
  private String subject;
  @NotBlank
  private String content;
  private String format;
  private Boolean active;
}
