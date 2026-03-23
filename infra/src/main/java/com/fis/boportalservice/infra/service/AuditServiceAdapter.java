package com.fis.boportalservice.infra.service;

import com.fis.boportalservice.core.domain.model.AuditModel;
import com.fis.boportalservice.core.domain.model.AuditPageResult;
import com.fis.boportalservice.core.service.AuditServicePort;
import com.fis.boportalservice.infra.feignclient.AuditServiceClient;
import com.fis.boportalservice.infra.feignclient.ResponseFeignClient;
import com.fis.boportalservice.infra.feignclient.dto.AuditHashCheckDto;
import com.fis.boportalservice.infra.feignclient.dto.AuditItemDto;
import com.fis.boportalservice.infra.feignclient.dto.AuditPageDto;
import com.fis.boportalservice.infra.feignclient.dto.AuditRehashDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditServiceAdapter implements AuditServicePort {

  private final AuditServiceClient auditServiceClient;

  @Override
  public AuditPageResult queryAudits(
      LocalDate startDate,
      LocalDate endDate,
      LocalDate day,
      String action,
      List<String> actorIds,
      int page,
      int size,
      String sortBy,
      String sortDirection
  ) {
    log.info("event=AUDIT_SERVICE_QUERY_REQUEST page={} size={} action={}", page, size, action);
    AuditPageDto dto = extractPayload(
        auditServiceClient.queryAudits(startDate, endDate, day, action, actorIds, page, size, sortBy, sortDirection)
    );
    if (dto == null) {
      log.warn("No data returned from audit-service queryAudits");
      return AuditPageResult.builder()
          .content(Collections.emptyList())
          .page(page)
          .size(size)
          .totalElements(0)
          .totalPages(0)
          .build();
    }
    List<AuditModel> content = dto.getContent() == null
        ? Collections.emptyList()
        : dto.getContent().stream().map(this::toModel).toList();

    return AuditPageResult.builder()
        .content(content)
        .page(dto.getPage())
        .size(dto.getSize())
        .totalElements(dto.getTotalElements())
        .totalPages(dto.getTotalPages())
        .build();
  }

  @Override
  public boolean verifyHashByAuditId(Long auditId) {
    log.info("event=AUDIT_SERVICE_HASH_VERIFY_REQUEST auditId={}", auditId);
    AuditHashCheckDto dto = extractPayload(auditServiceClient.verifyHashByAuditId(auditId));
    if (dto == null) {
      log.warn("No data returned from audit-service verifyHashByAuditId auditId={}", auditId);
      return false;
    }
    return dto.isValid();
  }

  @Override
  public AuditRehashResult rehashAllBeforeDay(LocalDate beforeDay) {
    log.info("event=AUDIT_SERVICE_REHASH_REQUEST beforeDay={}", beforeDay);
    AuditRehashDto dto = extractPayload(auditServiceClient.rehashAllBeforeDay(beforeDay));
    if (dto == null) {
      log.warn("No data returned from audit-service rehashAllBeforeDay beforeDay={}", beforeDay);
      return new AuditRehashResult(beforeDay, 0, 0);
    }
    return new AuditRehashResult(dto.getBeforeDay(), dto.getMatchedCount(), dto.getUpdatedCount());
  }

  @Override
  public com.fis.boportalservice.core.domain.model.ActionFunctionModel createActionFunction(String action, String description) {
    log.info("event=AUDIT_SERVICE_CREATE_ACTION_FUNCTION action={}", action);
    var request = com.fis.boportalservice.infra.feignclient.dto.ActionFunctionRequestDto.builder()
            .action(action)
            .description(description)
            .build();
    var dto = extractPayload(auditServiceClient.createActionFunction(request));
    if (dto == null) return null;
    return com.fis.boportalservice.core.domain.model.ActionFunctionModel.builder()
            .id(dto.getId())
            .action(dto.getAction())
            .description(dto.getDescription())
            .build();
  }

  @Override
  public List<com.fis.boportalservice.core.domain.model.ActionFunctionModel> getAllActionFunctions() {
    log.info("event=AUDIT_SERVICE_GET_ALL_ACTION_FUNCTIONS");
    var dto = extractPayload(auditServiceClient.getAllActionFunctions());
    if (dto == null) return Collections.emptyList();
    return dto.stream()
            .map(d -> com.fis.boportalservice.core.domain.model.ActionFunctionModel.builder()
                    .id(d.getId())
                    .action(d.getAction())
                    .description(d.getDescription())
                    .build())
            .toList();
  }

  private AuditModel toModel(AuditItemDto dto) {
    return AuditModel.builder()
        .id(dto.getId())
        .entity(dto.getEntity())
        .actor(dto.getActor())
        .actorId(dto.getActorId())
        .action(dto.getAction())
        .actionDescription(dto.getActionDescription())
        .actionStatus(dto.getActionStatus())
        .oldValue(dto.getOldValue())
        .newValue(dto.getNewValue())
        .requestId(dto.getRequestId())
        .traceId(dto.getTraceId())
        .ipAddress(dto.getIpAddress())
        .timeStamp(dto.getTimeStamp())
        .hash(dto.getHash())
        .build();
  }

  private <T> T extractPayload(ResponseFeignClient<T> response) {
    if (response == null) {
      return null;
    }
    return response.getData() != null ? response.getData() : response.getResult();
  }
}
