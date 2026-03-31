package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.OrgChartBulkManagerUpdateRequest;
import com.fis.boportalservice.api.dto.response.OrgChartBulkManagerUpdateResponse;
import com.fis.boportalservice.api.dto.response.OrgChartPageResponse;
import com.fis.boportalservice.api.dto.response.OrgChartPathResponse;
import com.fis.boportalservice.api.dto.response.OrgChartUserDetailResponse;
import com.fis.boportalservice.api.dto.response.OrgChartUserLiteResponse;
import com.fis.boportalservice.api.dto.response.OrgChartUserNodeResponse;
import com.fis.boportalservice.api.mapper.OrgChartApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.service.OrgChartServicePort;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bo-portal/orgchart")
@Tag(name = "7. Admin - Org Chart")
public class OrgChartController {

  private final OrgChartServicePort orgChartServicePort;
  private final OrgChartApiMapper orgChartApiMapper;

  @GetMapping
  public ResponseApi<OrgChartUserNodeResponse> getOrgChart(
      @RequestParam(required = false) Long rootId,
      @RequestParam(defaultValue = "1") int maxDepth
  ) {
    log.info("event=ORGCHART_TREE_BFF_REQUEST rootId={} maxDepth={}", rootId, maxDepth);
    return ResponseApi.success(orgChartApiMapper.toNodeResponse(orgChartServicePort.getOrgChart(rootId, maxDepth)));
  }

  @GetMapping("/users/{userId}/subordinates")
  public ResponseApi<OrgChartPageResponse<OrgChartUserNodeResponse>> getSubordinates(
      @PathVariable Long userId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "50") int limit
  ) {
    log.info("event=ORGCHART_SUBORDINATES_BFF_REQUEST userId={} page={} limit={}", userId, page, limit);
    return ResponseApi.success(
        orgChartApiMapper.toPageResponse(orgChartServicePort.getSubordinates(userId, page, limit))
    );
  }

  @GetMapping("/users/{userId}")
  public ResponseApi<OrgChartUserDetailResponse> getUserDetail(@PathVariable Long userId) {
    log.info("event=ORGCHART_DETAIL_BFF_REQUEST userId={}", userId);
    return ResponseApi.success(orgChartApiMapper.toDetailResponse(orgChartServicePort.getUserDetail(userId)));
  }

  @PutMapping("/users/{userId}/manager")
  public ResponseApi<OrgChartUserDetailResponse> updateManager(
      @PathVariable Long userId,
      @RequestParam(required = false) Long managerId
  ) {
    log.info("event=ORGCHART_MANAGER_UPDATE_BFF_REQUEST userId={} managerId={}", userId, managerId);
    return ResponseApi.success(
        orgChartApiMapper.toDetailResponse(orgChartServicePort.updateManager(userId, managerId))
    );
  }

  @PutMapping("/users/manager")
  public ResponseApi<OrgChartBulkManagerUpdateResponse> bulkUpdateManager(
      @RequestBody OrgChartBulkManagerUpdateRequest request
  ) {
    log.info("event=ORGCHART_BULK_MANAGER_UPDATE_BFF_REQUEST userIds={} managerId={}", request.userIds(), request.managerId());
    return ResponseApi.success(
        orgChartApiMapper.toBulkManagerUpdateResponse(
            orgChartServicePort.bulkUpdateManager(request.userIds(), request.managerId())
        )
    );
  }

  @GetMapping("/users")
  public ResponseApi<OrgChartPageResponse<OrgChartUserNodeResponse>> searchUsers(
      @RequestParam(required = false, name = "q") String query,
      @RequestParam(required = false) String department,
      @RequestParam(required = false) String status,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int limit
  ) {
    log.info(
        "event=ORGCHART_SEARCH_BFF_REQUEST query={} department={} status={} page={} limit={}",
        query,
        department,
        status,
        page,
        limit
    );
    return ResponseApi.success(
        orgChartApiMapper.toPageResponse(orgChartServicePort.searchUsers(query, department, status, page, limit))
    );
  }

  @GetMapping("/assignable-users")
  public ResponseApi<OrgChartPageResponse<OrgChartUserLiteResponse>> getAssignableUsers(
      @RequestParam(required = false, name = "q") String query,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int limit
  ) {
    log.info("event=ORGCHART_ASSIGNABLE_USERS_BFF_REQUEST query={} page={} limit={}", query, page, limit);
    return ResponseApi.success(
        orgChartApiMapper.toLitePageResponse(orgChartServicePort.getAssignableUsers(query, page, limit))
    );
  }

  @GetMapping("/users/{userId}/path")
  public ResponseApi<OrgChartPathResponse> getPathToRoot(@PathVariable Long userId) {
    log.info("event=ORGCHART_PATH_BFF_REQUEST userId={}", userId);
    return ResponseApi.success(orgChartApiMapper.toPathResponse(orgChartServicePort.getPathToRoot(userId)));
  }
}
