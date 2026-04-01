package com.fis.boportalservice.infra.feignclient.dto.ticket;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.List;

public record TicketApproverIdsResponse(
    @JsonSerialize(contentUsing = ToStringSerializer.class)
    List<Long> approverIds
) {}

