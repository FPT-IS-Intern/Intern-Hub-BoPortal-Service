package com.fis.boportalservice.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomepageBanner {
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
