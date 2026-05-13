package com.fis.boportalservice.infra.feignclient.dto;


import lombok.Data;

import java.time.LocalDate;

@Data
public class AuditRehashDto {

  private LocalDate beforeDay;
  private long matchedCount;
  private long updatedCount;

}
