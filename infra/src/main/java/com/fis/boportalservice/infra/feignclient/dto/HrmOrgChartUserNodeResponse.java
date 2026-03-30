package com.fis.boportalservice.infra.feignclient.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HrmOrgChartUserNodeResponse {
  private String id;
  private String name;
  private String title;
  private String department;
  private String avatar;
  private String email;
  private String phone;
  private String status;
  private String joinedDate;
  private String location;
  private String managerId;
  private boolean hasChildren;
  private long subordinateCount;
  private List<HrmOrgChartUserNodeResponse> children;
}
