package com.fis.boportalservice.infra.feignclient;

import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.infra.configuration.FeignClientCommonConfiguration;
import com.fis.boportalservice.infra.feignclient.dto.ticket.TicketApproverIdsResponse;
import com.fis.boportalservice.infra.feignclient.dto.ticket.TicketTypeResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "ticket-service",
    url = "${feign.client.config.ticket-service.url:http://intern-hub-ticket-service:8080}",
    configuration = FeignClientCommonConfiguration.class
)
public interface TicketServiceClient {
  @GetMapping("/ticket/ticket-types/all")
  ResponseApi<List<TicketTypeResponse>> getTicketTypes();

  @GetMapping("/ticket/ticket-types/{ticketTypeId}/approvers")
  ResponseApi<TicketApproverIdsResponse> getTicketTypeApproverIds(
      @PathVariable("ticketTypeId") Long ticketTypeId,
      @RequestParam(value = "level", required = false) Integer level
  );

  @PostMapping("/ticket/ticket-types/{ticketTypeId}/approvers/{approverId}")
  ResponseApi<Void> assignTicketTypeApprover(
      @PathVariable("ticketTypeId") Long ticketTypeId,
      @PathVariable("approverId") Long approverId,
      @RequestParam(value = "level", required = false) Integer level
  );

  @DeleteMapping("/ticket/ticket-types/{ticketTypeId}/approvers/{approverId}")
  ResponseApi<Void> removeTicketTypeApprover(
      @PathVariable("ticketTypeId") Long ticketTypeId,
      @PathVariable("approverId") Long approverId,
      @RequestParam(value = "level", required = false) Integer level
  );

  @GetMapping("/ticket/approvers")
  ResponseApi<TicketApproverIdsResponse> getGlobalApproverIds(
      @RequestParam(value = "level", required = false) Integer level
  );

  @PostMapping("/ticket/approvers/{approverId}")
  ResponseApi<Void> assignGlobalApprover(
      @PathVariable("approverId") Long approverId,
      @RequestParam(value = "level", required = false) Integer level
  );

  @DeleteMapping("/ticket/approvers/{approverId}")
  ResponseApi<Void> removeGlobalApprover(
      @PathVariable("approverId") Long approverId,
      @RequestParam(value = "level", required = false) Integer level
  );
}
