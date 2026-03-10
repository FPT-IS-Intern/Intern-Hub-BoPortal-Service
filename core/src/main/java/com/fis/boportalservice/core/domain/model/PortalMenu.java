package com.fis.boportalservice.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortalMenu {
  private Integer id;
  private String code;
  private String title;
  private String path;
  private String icon;
  private Integer parentId;
  private String permissionCode;
  private Integer sortOrder;
  private String status;
}
