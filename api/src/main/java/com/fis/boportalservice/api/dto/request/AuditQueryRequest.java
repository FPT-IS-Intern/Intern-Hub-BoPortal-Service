package com.fis.boportalservice.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static org.springframework.format.annotation.DateTimeFormat.ISO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditQueryRequest {

  @DateTimeFormat(iso = ISO.DATE)
  private LocalDate startDate;

  @DateTimeFormat(iso = ISO.DATE)
  private LocalDate endDate;

  @DateTimeFormat(iso = ISO.DATE)
  private LocalDate day;

  private String action;

  private java.util.List<String> actorIds;

  @Builder.Default
  private Integer page = 0;

  @Builder.Default
  private Integer size = 20;

  @Builder.Default
  private String sortBy = "timestamp";

  @Builder.Default
  private String sortDirection = "DESC";

}
