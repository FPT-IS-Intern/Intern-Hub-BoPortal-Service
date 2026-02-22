package com.fis.boportalservice.core.domain.model;

import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemConfig {
    private String appName;
    private String logoUrl;
    private String defaultLanguage;
    private LocalTime workStartTime;
    private LocalTime workEndTime;
    private LocalTime autoCheckoutTime;
    private UUID updatedBy;
}
