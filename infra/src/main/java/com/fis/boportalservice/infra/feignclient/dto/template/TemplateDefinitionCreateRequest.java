package com.fis.boportalservice.infra.feignclient.dto.template;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TemplateDefinitionCreateRequest {
  @NotBlank
  private String code;
  private String description;
  private String paramsSchema;
}
