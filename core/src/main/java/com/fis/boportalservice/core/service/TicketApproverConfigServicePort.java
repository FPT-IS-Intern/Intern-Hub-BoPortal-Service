package com.fis.boportalservice.core.service;

import com.fis.boportalservice.core.model.TicketTypeItem;
import java.util.List;

public interface TicketApproverConfigServicePort {
  List<TicketTypeItem> getTicketTypes();
  List<String> getApproverIds(Long ticketTypeId, Integer level);
  void assignApprover(Long ticketTypeId, Long approverId, Integer level);
  void removeApprover(Long ticketTypeId, Long approverId, Integer level);

  List<String> getGlobalApproverIds(Integer level);
  void assignGlobalApprover(Long approverId, Integer level);
  void removeGlobalApprover(Long approverId, Integer level);
}
