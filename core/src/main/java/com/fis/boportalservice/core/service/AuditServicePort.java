package com.fis.boportalservice.core.service;

import com.fis.boportalservice.core.domain.model.AuditModel;
import com.fis.boportalservice.core.domain.model.AuditPageResult;

import java.time.LocalDate;

public interface AuditServicePort {

  AuditPageResult queryAudits(
      LocalDate startDate,
      LocalDate endDate,
      LocalDate day,
      String action,
      int page,
      int size,
      String sortBy,
      String sortDirection
  );

  boolean verifyHashByAuditId(Long auditId);

  AuditRehashResult rehashAllBeforeDay(LocalDate beforeDay);

  com.fis.boportalservice.core.domain.model.ActionFunctionModel createActionFunction(String action, String description);

  java.util.List<com.fis.boportalservice.core.domain.model.ActionFunctionModel> getAllActionFunctions();

  record AuditRehashResult(LocalDate beforeDay, long matchedCount, long updatedCount) {
  }
}
