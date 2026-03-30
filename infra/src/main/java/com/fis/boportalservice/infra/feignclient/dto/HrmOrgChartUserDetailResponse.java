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
public class HrmOrgChartUserDetailResponse {
  private String id;
  private String name;
  private String title;
  private HrmOrgChartDepartmentResponse department;
  private String avatar;
  private String email;
  private String phone;
  private String status;
  private String joinedDate;
  private String location;
  private HrmOrgChartUserLiteResponse manager;
  private List<HrmOrgChartUserLiteResponse> subordinates;
  private List<String> projects;
  private boolean hasChildren;
  private long subordinateCount;
}
