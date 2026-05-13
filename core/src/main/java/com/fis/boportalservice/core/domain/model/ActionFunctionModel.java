package com.fis.boportalservice.core.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActionFunctionModel {
    private Long id;
    private String action;
    private String description;
}
