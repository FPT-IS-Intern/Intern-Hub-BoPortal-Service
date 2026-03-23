package com.fis.boportalservice.infra.feignclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditRehashDto {

  private LocalDate beforeDay;
  private long matchedCount;
  private long updatedCount;

}
