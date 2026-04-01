package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.service.TicketApproverConfigServicePort;
import com.fis.boportalservice.core.model.TicketTypeItem;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "6. Admin - Ticket Approver Config (Ticket Service)")
@RestController
@RequestMapping("/bo-portal/tickets")
@RequiredArgsConstructor
public class TicketApproverConfigController {

  private final TicketApproverConfigServicePort servicePort;

  @GetMapping("/ticket-types")
  public ResponseApi<List<TicketTypeItem>> getTicketTypes() {
    return ResponseApi.success(servicePort.getTicketTypes());
  }

  @GetMapping("/ticket-types/{ticketTypeId}/approvers")
  public ResponseApi<List<String>> getApproverIds(
      @PathVariable Long ticketTypeId,
      @RequestParam(value = "level", required = false) Integer level
  ) {
    return ResponseApi.success(servicePort.getApproverIds(ticketTypeId, level));
  }

  @PostMapping("/ticket-types/{ticketTypeId}/approvers/{approverId}")
  public ResponseApi<Void> assignApprover(
      @PathVariable Long ticketTypeId,
      @PathVariable Long approverId,
      @RequestParam(value = "level", required = false) Integer level
  ) {
    servicePort.assignApprover(ticketTypeId, approverId, level);
    return ResponseApi.success();
  }

  @DeleteMapping("/ticket-types/{ticketTypeId}/approvers/{approverId}")
  public ResponseApi<Void> removeApprover(
      @PathVariable Long ticketTypeId,
      @PathVariable Long approverId,
      @RequestParam(value = "level", required = false) Integer level
  ) {
    servicePort.removeApprover(ticketTypeId, approverId, level);
    return ResponseApi.success();
  }

  // Global approver config (applies to all ticket types)
  @GetMapping("/approvers")
  public ResponseApi<List<String>> getGlobalApproverIds(
      @RequestParam(value = "level", required = false) Integer level
  ) {
    return ResponseApi.success(servicePort.getGlobalApproverIds(level));
  }

  @PostMapping("/approvers/{approverId}")
  public ResponseApi<Void> assignGlobalApprover(
      @PathVariable Long approverId,
      @RequestParam(value = "level", required = false) Integer level
  ) {
    servicePort.assignGlobalApprover(approverId, level);
    return ResponseApi.success();
  }

  @DeleteMapping("/approvers/{approverId}")
  public ResponseApi<Void> removeGlobalApprover(
      @PathVariable Long approverId,
      @RequestParam(value = "level", required = false) Integer level
  ) {
    servicePort.removeGlobalApprover(approverId, level);
    return ResponseApi.success();
  }
}
