package com.fis.boportalservice.infra.feignclient.dto;

import lombok.Data;

@Data
public class ActionFunctionDto {
    private Long id;
    private String action;
    private String description;
}
