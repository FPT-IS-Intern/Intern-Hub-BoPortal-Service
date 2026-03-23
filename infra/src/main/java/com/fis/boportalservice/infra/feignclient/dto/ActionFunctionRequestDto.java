package com.fis.boportalservice.infra.feignclient.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActionFunctionRequestDto {
    private String action;
    private String description;
}
