package com.fis.boportalservice.infra.feignclient.dto.template;

import lombok.Data;

@Data
public class TemplateDefinitionUpdateRequest {
  private String description;
  private String paramsSchema;
}
