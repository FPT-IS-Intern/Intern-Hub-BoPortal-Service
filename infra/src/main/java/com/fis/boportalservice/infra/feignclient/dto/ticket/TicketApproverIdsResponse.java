package com.fis.boportalservice.infra.feignclient.dto.ticket;

import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;
import java.util.List;

public record TicketApproverIdsResponse(
    @JsonSerialize(contentUsing = ToStringSerializer.class)
    List<Long> approverIds
) {}

