package com.fis.boportalservice.core.service;

import java.util.List;

public interface OrgChartServicePort {

  OrgChartNode getOrgChart(Long rootId, int maxDepth);

  OrgChartPageResult getSubordinates(Long userId, int page, int limit);

  OrgChartUserDetail getUserDetail(Long userId);

  OrgChartPageResult searchUsers(String query, String department, String status, int page, int limit);

  List<OrgChartUserLite> getPathToRoot(Long userId);

  record OrgChartNode(
      String id,
      String name,
      String title,
      String department,
      String avatar,
      String email,
      String phone,
      String status,
      String joinedDate,
      String location,
      String managerId,
      boolean hasChildren,
      long subordinateCount,
      List<OrgChartNode> children
  ) {
  }

  record OrgChartUserLite(
      String id,
      String name,
      String title,
      String avatar
  ) {
  }

  record OrgChartDepartment(
      String id,
      String name,
      String code
  ) {
  }

  record OrgChartUserDetail(
      String id,
      String name,
      String title,
      OrgChartDepartment department,
      String avatar,
      String email,
      String phone,
      String status,
      String joinedDate,
      String location,
      OrgChartUserLite manager,
      List<OrgChartUserLite> subordinates,
      List<String> projects,
      boolean hasChildren,
      long subordinateCount
  ) {
  }

  record OrgChartPageResult(
      List<OrgChartNode> data,
      long total,
      int page,
      int limit,
      int totalPages
  ) {
  }
}
