package com.fis.boportalservice.infra.feignclient.dto.template;

import lombok.Data;

import java.util.List;

@Data
public class TemplateSummaryPageResponse {
  private List<TemplateSummaryResponse> items;
  private long total;
  private int page;
  private int size;
}
