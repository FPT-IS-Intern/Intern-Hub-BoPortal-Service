package com.fis.boportalservice.core.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActionFunctionModel {
    private Long id;
    private String action;
    private String description;
}
