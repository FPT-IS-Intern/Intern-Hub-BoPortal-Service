package com.fis.boportalservice.infra.feignclient.dto.template;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.Map;

@Data
public class TemplateDefinitionCreateRequest {
  @NotBlank
  private String code;
  private String description;
  private Map<String, Object> paramsSchema;
}
