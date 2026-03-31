package com.fis.boportalservice.infra.feignclient.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HrmBulkManagerUpdateRequest {
  private List<Long> userIds;
  private Long managerId;
}
