package com.fis.boportalservice.infra.feignclient.dto.template;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TemplateRestoreRequest {
  @NotBlank
  private String channel;
  private String lang;
  @NotNull
  private Integer version;
}
