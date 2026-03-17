package com.fis.boportalservice.infra.feignclient.dto.template;

import lombok.Data;

import java.util.List;

@Data
public class TemplateSummaryResponse {
  private String code;
  private String description;
  private List<String> channels;
  private Long updatedAt;
  private Boolean isDeletable;
}
