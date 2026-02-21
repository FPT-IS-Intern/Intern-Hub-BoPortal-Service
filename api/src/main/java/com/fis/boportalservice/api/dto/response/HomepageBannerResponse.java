package com.fis.boportalservice.api.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class HomepageBannerResponse {
    private UUID id;
    private String title;
    private String description;
    private Integer displayOrder;
    private Boolean isActive;
    private String desktopImageUrl;
    private String mobileImageUrl;
    private String imageAltText;
    private String actionType;
    private String actionTarget;
    private Boolean openInNewTab;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
