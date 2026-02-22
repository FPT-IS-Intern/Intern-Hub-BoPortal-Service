package com.fis.boportalservice.api.dto.request;

import lombok.Data;

@Data
public class AllowedIpRangeRequest {
    private String name;
    private String ipPrefix;
    private Boolean isActive;
    private String description;
}
