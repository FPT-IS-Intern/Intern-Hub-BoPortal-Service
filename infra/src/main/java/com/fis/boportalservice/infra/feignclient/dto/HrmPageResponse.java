package com.fis.boportalservice.infra.feignclient.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HrmPageResponse<T> {
  private List<T> items;
  private long totalItems;
  private int totalPages;
}
