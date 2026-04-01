package com.fis.boportalservice.infra.feignclient.dto.ticket;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public record TicketTypeResponse(
    @JsonSerialize(using = ToStringSerializer.class)
    Long ticketTypeId,
    String typeName,
    String description
) {}

