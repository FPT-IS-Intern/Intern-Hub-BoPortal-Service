package com.fis.boportalservice.infra.feignclient.dto.ticket;

import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

public record TicketTypeResponse(
    @JsonSerialize(using = ToStringSerializer.class)
    Long ticketTypeId,
    String typeName,
    String description
) {}

