package com.fis.boportalservice.api.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HomepageBannerRequest {
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
