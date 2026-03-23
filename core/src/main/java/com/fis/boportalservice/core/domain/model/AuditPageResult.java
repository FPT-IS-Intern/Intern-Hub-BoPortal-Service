package com.fis.boportalservice.core.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AuditPageResult {

  private List<AuditModel> content;
  private int page;
  private int size;
  private long totalElements;
  private int totalPages;

}
