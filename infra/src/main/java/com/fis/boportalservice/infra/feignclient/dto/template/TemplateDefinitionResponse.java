package com.fis.boportalservice.infra.feignclient.dto.template;

import lombok.Data;

@Data
public class TemplateDefinitionResponse {
  private String code;
  private String paramsSchema;
}
