package com.fis.boportalservice.infra.feignclient.dto.template;

import lombok.Data;
import java.util.Map;

@Data
public class TemplateDefinitionUpdateRequest {
  private String description;
  private Map<String, Object> paramsSchema;
}
