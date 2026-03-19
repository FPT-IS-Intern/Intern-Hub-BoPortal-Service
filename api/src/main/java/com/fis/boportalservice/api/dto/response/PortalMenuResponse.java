package com.fis.boportalservice.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortalMenuResponse {
  private Integer id;
  private String code;
  private String title;
  private String path;
  private String icon;
  private Integer parentId;
  private List<String> roleCodes;
  private Integer sortOrder;
  private String status;
  private List<PortalMenuResponse> children;
}
