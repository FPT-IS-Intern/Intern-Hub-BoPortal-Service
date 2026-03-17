package com.fis.boportalservice.infra.feignclient.dto.template;

import lombok.Data;
import java.util.Map;

@Data
public class TemplateDefinitionResponse {
  private String code;
  private Map<String, Object> paramsSchema;
}
