package com.fis.boportalservice.api.mapper;

import com.fis.boportalservice.api.dto.response.OrgChartDepartmentResponse;
import com.fis.boportalservice.api.dto.response.OrgChartBulkManagerUpdateResponse;
import com.fis.boportalservice.api.dto.response.OrgChartMetaResponse;
import com.fis.boportalservice.api.dto.response.OrgChartPageResponse;
import com.fis.boportalservice.api.dto.response.OrgChartPathResponse;
import com.fis.boportalservice.api.dto.response.OrgChartUserDetailResponse;
import com.fis.boportalservice.api.dto.response.OrgChartUserLiteResponse;
import com.fis.boportalservice.api.dto.response.OrgChartUserNodeResponse;
import com.fis.boportalservice.core.service.OrgChartServicePort;
import org.springframework.stereotype.Component;

@Component
public class OrgChartApiMapper {

  public OrgChartUserNodeResponse toNodeResponse(OrgChartServicePort.OrgChartNode node) {
    if (node == null) {
      return null;
    }

    return new OrgChartUserNodeResponse(
        node.id(),
        node.name(),
        node.title(),
        node.department(),
        node.avatar(),
        node.email(),
        node.phone(),
        node.status(),
        node.joinedDate(),
        node.location(),
        node.managerId(),
        node.hasChildren(),
        node.subordinateCount(),
        node.children() != null ? node.children().stream().map(this::toNodeResponse).toList() : java.util.List.of()
    );
  }

  public OrgChartPageResponse<OrgChartUserNodeResponse> toPageResponse(OrgChartServicePort.OrgChartPageResult pageResult) {
    return new OrgChartPageResponse<>(
        pageResult.data().stream().map(this::toNodeResponse).toList(),
        new OrgChartMetaResponse(pageResult.total(), pageResult.page(), pageResult.limit(), pageResult.totalPages())
    );
  }

  public OrgChartPageResponse<OrgChartUserLiteResponse> toLitePageResponse(OrgChartServicePort.OrgChartLitePageResult pageResult) {
    return new OrgChartPageResponse<>(
        pageResult.data().stream().map(this::toLite).toList(),
        new OrgChartMetaResponse(pageResult.total(), pageResult.page(), pageResult.limit(), pageResult.totalPages())
    );
  }

  public OrgChartUserDetailResponse toDetailResponse(OrgChartServicePort.OrgChartUserDetail detail) {
    if (detail == null) {
      return null;
    }

    return new OrgChartUserDetailResponse(
        detail.id(),
        detail.name(),
        detail.title(),
        toDepartment(detail.department()),
        detail.avatar(),
        detail.email(),
        detail.phone(),
        detail.status(),
        detail.joinedDate(),
        detail.location(),
        toLite(detail.manager()),
        detail.subordinates() != null ? detail.subordinates().stream().map(this::toLite).toList() : java.util.List.of(),
        detail.projects() != null ? detail.projects() : java.util.List.of(),
        detail.hasChildren(),
        detail.subordinateCount()
    );
  }

  public OrgChartPathResponse toPathResponse(java.util.List<OrgChartServicePort.OrgChartUserLite> items) {
    return new OrgChartPathResponse(items.stream().map(this::toLite).toList());
  }

  public OrgChartBulkManagerUpdateResponse toBulkManagerUpdateResponse(
      OrgChartServicePort.OrgChartBulkManagerUpdateResult result) {
    return new OrgChartBulkManagerUpdateResponse(result.updatedUserIds(), result.managerId(), result.updatedCount());
  }

  private OrgChartDepartmentResponse toDepartment(OrgChartServicePort.OrgChartDepartment department) {
    if (department == null) {
      return null;
    }
    return new OrgChartDepartmentResponse(department.id(), department.name(), department.code());
  }

  private OrgChartUserLiteResponse toLite(OrgChartServicePort.OrgChartUserLite lite) {
    if (lite == null) {
      return null;
    }
    return new OrgChartUserLiteResponse(lite.id(), lite.name(), lite.title(), lite.avatar());
  }
}
