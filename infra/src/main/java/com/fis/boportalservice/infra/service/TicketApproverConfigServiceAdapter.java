package com.fis.boportalservice.infra.service;

import com.fis.boportalservice.core.service.TicketApproverConfigServicePort;
import com.fis.boportalservice.core.model.TicketTypeItem;
import com.fis.boportalservice.infra.feignclient.TicketServiceClient;
import com.fis.boportalservice.infra.feignclient.dto.ticket.TicketApproverIdsResponse;
import com.fis.boportalservice.infra.feignclient.dto.ticket.TicketTypeResponse;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketApproverConfigServiceAdapter implements TicketApproverConfigServicePort {

  private final TicketServiceClient ticketServiceClient;

  @Override
  public List<TicketTypeItem> getTicketTypes() {
    log.info("event=TICKET_TYPES_REQUEST");
    List<TicketTypeResponse> response = extractData(ticketServiceClient.getTicketTypes());
    if (response == null) return Collections.emptyList();
    return response.stream()
        .map(t -> new TicketTypeItem(
            t.ticketTypeId() != null ? String.valueOf(t.ticketTypeId()) : null,
            t.typeName(),
            t.description()
        ))
        .toList();
  }

  @Override
  public List<String> getApproverIds(Long ticketTypeId, Integer level) {
    log.info("event=TICKET_TYPE_APPROVER_IDS_REQUEST ticketTypeId={} level={}", ticketTypeId, level);
    TicketApproverIdsResponse response = extractData(ticketServiceClient.getTicketTypeApproverIds(ticketTypeId, level));
    if (response == null || response.approverIds() == null) return Collections.emptyList();
    return response.approverIds().stream().map(String::valueOf).toList();
  }

  @Override
  public void assignApprover(Long ticketTypeId, Long approverId, Integer level) {
    log.info("event=TICKET_TYPE_APPROVER_ASSIGN_REQUEST ticketTypeId={} approverId={} level={}", ticketTypeId, approverId, level);
    ticketServiceClient.assignTicketTypeApprover(ticketTypeId, approverId, level);
  }

  @Override
  public void removeApprover(Long ticketTypeId, Long approverId, Integer level) {
    log.info("event=TICKET_TYPE_APPROVER_REMOVE_REQUEST ticketTypeId={} approverId={} level={}", ticketTypeId, approverId, level);
    ticketServiceClient.removeTicketTypeApprover(ticketTypeId, approverId, level);
  }

  private <T> T extractData(com.fis.boportalservice.common.dto.ResponseApi<T> response) {
    return response != null ? response.data() : null;
  }
}
