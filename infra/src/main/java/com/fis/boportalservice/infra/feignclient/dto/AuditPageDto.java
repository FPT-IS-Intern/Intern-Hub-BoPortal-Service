package com.fis.boportalservice.infra.feignclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditPageDto {

  private List<AuditItemDto> content;
  private int page;
  private int size;
  private long totalElements;
  private int totalPages;

}
