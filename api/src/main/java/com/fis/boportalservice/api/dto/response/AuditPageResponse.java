package com.fis.boportalservice.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditPageResponse {

  private List<AuditItemResponse> content;
  private int page;
  private int size;
  private long totalElements;
  private int totalPages;

}
