package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.AuditQueryRequest;
import com.fis.boportalservice.api.dto.response.AuditHashCheckResponse;
import com.fis.boportalservice.api.dto.response.AuditItemResponse;
import com.fis.boportalservice.api.dto.response.AuditPageResponse;
import com.fis.boportalservice.api.dto.response.AuditRehashResponse;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.domain.model.AuditModel;
import com.fis.boportalservice.core.domain.model.AuditPageResult;
import com.fis.boportalservice.core.exception.ErrorCode;
import com.fis.boportalservice.core.service.AuditServicePort;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO;

@Slf4j
@Tag(name = "7. Admin - Audit (Audit Service)")
@RestController
@RequestMapping("/bo-portal/audit")
@RequiredArgsConstructor
public class AdminAuditController {

  private final AuditServicePort auditServicePort;

  @GetMapping
  public ResponseApi<AuditPageResponse> queryAudits(@ModelAttribute AuditQueryRequest request) {
    log.info("event=AUDIT_QUERY_REQUEST action={} page={} size={}", request.getAction(), request.getPage(), request.getSize());
    log.info("API - Query audits request: action={}, page={}, size={}", request.getAction(), request.getPage(), request.getSize());
    AuditPageResult result = auditServicePort.queryAudits(
        request.getStartDate(),
        request.getEndDate(),
        request.getDay(),
        request.getAction(),
        request.getActorIds(),
        request.getPage() != null ? request.getPage() : 0,
        request.getSize() != null ? request.getSize() : 20,
        request.getSortBy(),
        request.getSortDirection()
    );
    AuditPageResponse response = toPageResponse(result);
    log.info("API - Query audits response: totalElements={}, totalPages={}", response.getTotalElements(), response.getTotalPages());
    return ResponseApi.success(response);
  }

  @GetMapping("/{auditId}/hash/verify")
  public ResponseApi<AuditHashCheckResponse> verifyHashByAuditId(@PathVariable String auditId) {
    log.info("event=AUDIT_HASH_VERIFY_REQUEST auditId={}", auditId);
    log.info("API - Verify audit hash request: auditId={}", auditId);
    Long parsedAuditId = parseLongId(auditId);
    if (parsedAuditId == null) {
      return ResponseApi.error(ErrorCode.BAD_REQUEST.getCode(), "Invalid auditId");
    }
    boolean valid = auditServicePort.verifyHashByAuditId(parsedAuditId);
    AuditHashCheckResponse response = AuditHashCheckResponse.builder()
            .auditId(auditId)
            .valid(valid)
            .build();
    log.info("API - Verify audit hash response: auditId={}, valid={}", auditId, valid);
    return ResponseApi.success(response);
  }

  @PostMapping("/hash/rehash")
  public ResponseApi<AuditRehashResponse> rehashAllBeforeDay(
      @RequestParam("beforeDay") @DateTimeFormat(iso = ISO.DATE) LocalDate beforeDay
  ) {
    log.info("event=AUDIT_REHASH_REQUEST beforeDay={}", beforeDay);
    log.info("API - Rehash audits request: beforeDay={}", beforeDay);
    AuditServicePort.AuditRehashResult result = auditServicePort.rehashAllBeforeDay(beforeDay);
    AuditRehashResponse response = AuditRehashResponse.builder()
            .beforeDay(result.beforeDay())
            .matchedCount(result.matchedCount())
            .updatedCount(result.updatedCount())
            .build();
    log.info("API - Rehash audits response: beforeDay={}, matchedCount={}, updatedCount={}",
            response.getBeforeDay(),
            response.getMatchedCount(),
            response.getUpdatedCount());
    return ResponseApi.success(response);
  }

  @PostMapping("/action-functions")
  public ResponseApi<com.fis.boportalservice.api.dto.response.ActionFunctionResponse> createActionFunction(
          @org.springframework.web.bind.annotation.RequestBody com.fis.boportalservice.api.dto.request.ActionFunctionRequest request) {
      log.info("API - Create action function request: action={}", request.getAction());
    var model = auditServicePort.createActionFunction(request.getAction(), request.getDescription());
    if (model == null) return ResponseApi.error("1", "Failed to create action function");
      var response = com.fis.boportalservice.api.dto.response.ActionFunctionResponse.builder()
        .id(toStringId(model.getId()))
        .action(model.getAction())
        .description(model.getDescription())
        .build();
      log.info("API - Create action function response: id={}, action={}", response.getId(), response.getAction());
      return ResponseApi.success(response);
  }

  @GetMapping("/action-functions")
  public ResponseApi<List<com.fis.boportalservice.api.dto.response.ActionFunctionResponse>> getAllActionFunctions() {
      log.info("API - Get all action functions request");
    var list = auditServicePort.getAllActionFunctions().stream()
            .map(model -> com.fis.boportalservice.api.dto.response.ActionFunctionResponse.builder()
                    .id(toStringId(model.getId()))
                    .action(model.getAction())
                    .description(model.getDescription())
                    .build())
            .toList();
      log.info("API - Get all action functions response: total={}", list.size());
    return ResponseApi.success(list);
  }

  // ─── Mapping helpers ───────────────────────────────────────────────────────

  private AuditPageResponse toPageResponse(AuditPageResult result) {
    List<AuditItemResponse> content = result.getContent().stream()
        .map(this::toItemResponse)
        .toList();
    return AuditPageResponse.builder()
        .content(content)
        .page(result.getPage())
        .size(result.getSize())
        .totalElements(result.getTotalElements())
        .totalPages(result.getTotalPages())
        .build();
  }

  private AuditItemResponse toItemResponse(AuditModel model) {
    return AuditItemResponse.builder()
        .id(toStringId(model.getId()))
        .entity(model.getEntity())
        .actor(model.getActor())
        .actorId(model.getActorId())
        .action(model.getAction())
        .actionDescription(model.getActionDescription())
        .actionStatus(model.getActionStatus())
        .oldValue(model.getOldValue())
        .newValue(model.getNewValue())
        .requestId(model.getRequestId())
        .traceId(model.getTraceId())
        .ipAddress(model.getIpAddress())
        .timeStamp(model.getTimeStamp())
        .hash(model.getHash())
        .build();
  }

  private String toStringId(Long value) {
    return value == null ? null : value.toString();
  }

  private Long parseLongId(String value) {
    try {
      return value == null ? null : Long.parseLong(value);
    } catch (NumberFormatException ex) {
      return null;
    }
  }
}
