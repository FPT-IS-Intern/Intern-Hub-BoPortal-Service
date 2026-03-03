package com.fis.boportalservice.core.domain.model;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllowedIpRange {
    private UUID id;
    private String name;
    private String ipPrefix;
    private Boolean isActive;
    private String description;
    private UUID branchId;
}
