package com.fis.boportalservice.infra.feignclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HrmInitializeRootRequest {
  private Long userId;
}
