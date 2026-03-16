package com.fis.boportalservice.infra.feignclient.dto.template;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TemplateSummaryResponse {
  private String code;
  private String description;
  private List<String> channels;
  private LocalDateTime updatedAt;
  private Boolean isDeletable;
}
