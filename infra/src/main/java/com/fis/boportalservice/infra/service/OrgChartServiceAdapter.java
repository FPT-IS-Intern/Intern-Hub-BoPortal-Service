package com.fis.boportalservice.infra.service;

import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.service.OrgChartServicePort;
import com.fis.boportalservice.infra.feignclient.HrmServiceClient;
import com.fis.boportalservice.infra.feignclient.dto.HrmBulkManagerUpdateRequest;
import com.fis.boportalservice.infra.feignclient.dto.HrmBulkManagerUpdateResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmInitializeRootRequest;
import com.fis.boportalservice.infra.feignclient.dto.HrmOrgChartDepartmentResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmOrgChartPageResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmOrgChartPathResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmOrgChartUserDetailResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmOrgChartUserLiteResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmOrgChartUserNodeResponse;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrgChartServiceAdapter implements OrgChartServicePort {

  private final HrmServiceClient hrmServiceClient;

  @Override
  public OrgChartNode getOrgChart(Long rootId, int maxDepth) {
    log.info("event=ORGCHART_TREE_REQUEST rootId={} maxDepth={}", rootId, maxDepth);
    return toNode(extractData(hrmServiceClient.getOrgChart(rootId, maxDepth)));
  }

  @Override
  public OrgChartPageResult getSubordinates(Long userId, int page, int limit) {
    log.info("event=ORGCHART_SUBORDINATES_REQUEST userId={} page={} limit={}", userId, page, limit);
    return toPageResult(extractData(hrmServiceClient.getOrgChartSubordinates(userId, page, limit)));
  }

  @Override
  public OrgChartUserDetail getUserDetail(Long userId) {
    log.info("event=ORGCHART_DETAIL_REQUEST userId={}", userId);
    return toDetail(extractData(hrmServiceClient.getOrgChartUserDetail(userId)));
  }

  @Override
  public OrgChartUserDetail initializeRoot(Long userId) {
    log.info("event=ORGCHART_ROOT_INIT_REQUEST userId={}", userId);
    return toDetail(extractData(hrmServiceClient.initializeOrgChartRoot(new HrmInitializeRootRequest(userId))));
  }

  @Override
  public OrgChartUserDetail updateManager(Long userId, Long managerId) {
    log.info("event=ORGCHART_MANAGER_UPDATE_REQUEST userId={} managerId={}", userId, managerId);
    hrmServiceClient.bulkUpdateOrgChartManager(
        new HrmBulkManagerUpdateRequest(List.of(userId), managerId)
    );
    return getUserDetail(userId);
  }

  @Override
  public OrgChartBulkManagerUpdateResult bulkUpdateManager(List<Long> userIds, Long managerId) {
    log.info("event=ORGCHART_BULK_MANAGER_UPDATE_REQUEST userIds={} managerId={}", userIds, managerId);
    HrmBulkManagerUpdateResponse response =
        extractData(hrmServiceClient.bulkUpdateOrgChartManager(new HrmBulkManagerUpdateRequest(userIds, managerId)));
    return new OrgChartBulkManagerUpdateResult(
        response != null && response.getUpdatedUserIds() != null
            ? response.getUpdatedUserIds().stream().map(String::valueOf).toList()
            : Collections.emptyList(),
        response != null && response.getManagerId() != null ? String.valueOf(response.getManagerId()) : null,
        response != null ? response.getUpdatedCount() : 0
    );
  }

  @Override
  public OrgChartPageResult searchUsers(String query, String department, String status, int page, int limit) {
    log.info(
        "event=ORGCHART_SEARCH_REQUEST query={} department={} status={} page={} limit={}",
        query,
        department,
        status,
        page,
        limit);
    return toPageResult(extractData(hrmServiceClient.searchOrgChartUsers(query, department, status, page, limit)));
  }

  @Override
  public OrgChartLitePageResult getAssignableUsers(String query, int page, int limit) {
    log.info("event=ORGCHART_ASSIGNABLE_USERS_REQUEST query={} page={} limit={}", query, page, limit);
    HrmOrgChartPageResponse<HrmOrgChartUserLiteResponse> response =
        extractData(hrmServiceClient.getAssignableOrgChartUsers(query, page, limit));
    if (response == null) {
      return new OrgChartLitePageResult(Collections.emptyList(), 0, 1, 0, 0);
    }

    return new OrgChartLitePageResult(
        response.getData() != null ? response.getData().stream().map(this::toLite).toList() : Collections.emptyList(),
        response.getMeta() != null ? response.getMeta().getTotal() : 0,
        response.getMeta() != null ? response.getMeta().getPage() : 1,
        response.getMeta() != null ? response.getMeta().getLimit() : 0,
        response.getMeta() != null ? response.getMeta().getTotalPages() : 0);
  }

  @Override
  public List<OrgChartUserLite> getPathToRoot(Long userId) {
    log.info("event=ORGCHART_PATH_REQUEST userId={}", userId);
    HrmOrgChartPathResponse response = extractData(hrmServiceClient.getOrgChartPath(userId));
    if (response == null || response.getData() == null) {
      return Collections.emptyList();
    }
    return response.getData().stream().map(this::toLite).toList();
  }

  private OrgChartPageResult toPageResult(HrmOrgChartPageResponse<HrmOrgChartUserNodeResponse> response) {
    if (response == null) {
      return new OrgChartPageResult(Collections.emptyList(), 0, 1, 0, 0);
    }

    return new OrgChartPageResult(
        response.getData() != null ? response.getData().stream().map(this::toNode).toList() : Collections.emptyList(),
        response.getMeta() != null ? response.getMeta().getTotal() : 0,
        response.getMeta() != null ? response.getMeta().getPage() : 1,
        response.getMeta() != null ? response.getMeta().getLimit() : 0,
        response.getMeta() != null ? response.getMeta().getTotalPages() : 0);
  }

  private OrgChartNode toNode(HrmOrgChartUserNodeResponse response) {
    if (response == null) {
      return null;
    }

    return new OrgChartNode(
        response.getId(),
        response.getName(),
        response.getTitle(),
        response.getDepartment(),
        response.getAvatar(),
        response.getEmail(),
        response.getPhone(),
        response.getStatus(),
        response.getJoinedDate(),
        response.getLocation(),
        response.getManagerId(),
        response.isHasChildren(),
        response.getSubordinateCount(),
        response.getChildren() != null ? response.getChildren().stream().map(this::toNode).toList() : Collections.emptyList());
  }

  private OrgChartUserDetail toDetail(HrmOrgChartUserDetailResponse response) {
    if (response == null) {
      return null;
    }

    return new OrgChartUserDetail(
        response.getId(),
        response.getName(),
        response.getTitle(),
        toDepartment(response.getDepartment()),
        response.getAvatar(),
        response.getEmail(),
        response.getPhone(),
        response.getStatus(),
        response.getJoinedDate(),
        response.getLocation(),
        toLite(response.getManager()),
        response.getSubordinates() != null ? response.getSubordinates().stream().map(this::toLite).toList() : Collections.emptyList(),
        response.getProjects() != null ? response.getProjects() : Collections.emptyList(),
        response.isHasChildren(),
        response.getSubordinateCount());
  }

  private OrgChartDepartment toDepartment(HrmOrgChartDepartmentResponse response) {
    if (response == null) {
      return null;
    }
    return new OrgChartDepartment(response.getId(), response.getName(), response.getCode());
  }

  private OrgChartUserLite toLite(HrmOrgChartUserLiteResponse response) {
    if (response == null) {
      return null;
    }
    return new OrgChartUserLite(response.getId(), response.getName(), response.getTitle(), response.getAvatar());
  }

  private <T> T extractData(ResponseApi<T> response) {
    return response != null ? response.data() : null;
  }
}
