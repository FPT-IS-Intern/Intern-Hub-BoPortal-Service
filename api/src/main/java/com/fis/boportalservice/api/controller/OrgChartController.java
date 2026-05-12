package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.OrgChartBulkManagerUpdateRequest;
import com.fis.boportalservice.api.dto.request.OrgChartInitializeRootRequest;
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
import org.springframework.web.bind.annotation.PostMapping;
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
    log.info("API - Get org chart request: rootId={}, maxDepth={}", rootId, maxDepth);
    OrgChartUserNodeResponse response = orgChartApiMapper.toNodeResponse(orgChartServicePort.getOrgChart(rootId, maxDepth));
    log.info("API - Get org chart response: rootId={}, hasNode={}", rootId, response != null);
    return ResponseApi.success(response);
  }

  @GetMapping("/users/{userId}/subordinates")
  public ResponseApi<OrgChartPageResponse<OrgChartUserNodeResponse>> getSubordinates(
      @PathVariable Long userId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "50") int limit
  ) {
    log.info("event=ORGCHART_SUBORDINATES_BFF_REQUEST userId={} page={} limit={}", userId, page, limit);
    log.info("API - Get subordinates request: userId={}, page={}, limit={}", userId, page, limit);
    OrgChartPageResponse<OrgChartUserNodeResponse> response =
        orgChartApiMapper.toPageResponse(orgChartServicePort.getSubordinates(userId, page, limit));
    log.info("API - Get subordinates response: userId={}, total={}", userId, response.meta() == null ? 0 : response.meta().total());
    return ResponseApi.success(response);
  }

  @GetMapping("/users/{userId}")
  public ResponseApi<OrgChartUserDetailResponse> getUserDetail(@PathVariable Long userId) {
    log.info("event=ORGCHART_DETAIL_BFF_REQUEST userId={}", userId);
    log.info("API - Get user detail request: userId={}", userId);
    OrgChartUserDetailResponse response = orgChartApiMapper.toDetailResponse(orgChartServicePort.getUserDetail(userId));
    log.info("API - Get user detail response: userId={}", userId);
    return ResponseApi.success(response);
  }

  @PostMapping("/root")
  public ResponseApi<OrgChartUserDetailResponse> initializeRoot(@RequestBody OrgChartInitializeRootRequest request) {
    log.info("event=ORGCHART_ROOT_INIT_BFF_REQUEST userId={}", request.userId());
    log.info("API - Initialize org root request: userId={}", request.userId());
    OrgChartUserDetailResponse response = orgChartApiMapper.toDetailResponse(orgChartServicePort.initializeRoot(request.userId()));
    log.info("API - Initialize org root response: userId={}", request.userId());
    return ResponseApi.success(response);
  }

  @PutMapping("/users/{userId}/manager")
  public ResponseApi<OrgChartUserDetailResponse> updateManager(
      @PathVariable Long userId,
      @RequestParam(required = false) Long managerId
  ) {
    log.info("event=ORGCHART_MANAGER_UPDATE_BFF_REQUEST userId={} managerId={}", userId, managerId);
    log.info("API - Update manager request: userId={}, managerId={}", userId, managerId);
    OrgChartUserDetailResponse response = orgChartApiMapper.toDetailResponse(orgChartServicePort.updateManager(userId, managerId));
    log.info("API - Update manager response: userId={}, managerId={}", userId, managerId);
    return ResponseApi.success(response);
  }

  @PutMapping("/users/manager")
  public ResponseApi<OrgChartBulkManagerUpdateResponse> bulkUpdateManager(
      @RequestBody OrgChartBulkManagerUpdateRequest request
  ) {
    log.info("event=ORGCHART_BULK_MANAGER_UPDATE_BFF_REQUEST userIds={} managerId={}", request.userIds(), request.managerId());
    log.info("API - Bulk update manager request: totalUserIds={}, managerId={}", request.userIds() == null ? 0 : request.userIds().size(), request.managerId());
    OrgChartBulkManagerUpdateResponse response = orgChartApiMapper.toBulkManagerUpdateResponse(
      orgChartServicePort.bulkUpdateManager(request.userIds(), request.managerId())
    );
    log.info("API - Bulk update manager response: totalUpdated={}", response.updatedCount());
    return ResponseApi.success(response);
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
    OrgChartPageResponse<OrgChartUserNodeResponse> response =
      orgChartApiMapper.toPageResponse(orgChartServicePort.searchUsers(query, department, status, page, limit));
    log.info("API - Search users response: total={}", response.meta() == null ? 0 : response.meta().total());
    return ResponseApi.success(response);
  }

  @GetMapping("/assignable-users")
  public ResponseApi<OrgChartPageResponse<OrgChartUserLiteResponse>> getAssignableUsers(
      @RequestParam(required = false, name = "q") String query,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int limit
  ) {
    log.info("event=ORGCHART_ASSIGNABLE_USERS_BFF_REQUEST query={} page={} limit={}", query, page, limit);
    OrgChartPageResponse<OrgChartUserLiteResponse> response =
        orgChartApiMapper.toLitePageResponse(orgChartServicePort.getAssignableUsers(query, page, limit));
    log.info("API - Get assignable users response: total={}", response.meta() == null ? 0 : response.meta().total());
    return ResponseApi.success(response);
  }

  @GetMapping("/nodes")
  public ResponseApi<OrgChartPageResponse<OrgChartUserLiteResponse>> getParentCandidates(
      @RequestParam Long userId,
      @RequestParam(required = false, name = "q") String query,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int limit
  ) {
    log.info("event=ORGCHART_PARENT_CANDIDATES_BFF_REQUEST userId={} query={} page={} limit={}", userId, query, page, limit);
    OrgChartPageResponse<OrgChartUserLiteResponse> response =
        orgChartApiMapper.toLitePageResponse(orgChartServicePort.getParentCandidates(userId, query, page, limit));
    log.info("API - Get parent candidates response: userId={}, total={}", userId, response.meta() == null ? 0 : response.meta().total());
    return ResponseApi.success(response);
  }

  @GetMapping("/users/{userId}/path")
  public ResponseApi<OrgChartPathResponse> getPathToRoot(@PathVariable Long userId) {
    log.info("event=ORGCHART_PATH_BFF_REQUEST userId={}", userId);
    log.info("API - Get path to root request: userId={}", userId);
    OrgChartPathResponse response = orgChartApiMapper.toPathResponse(orgChartServicePort.getPathToRoot(userId));
    log.info("API - Get path to root response: userId={}", userId);
    return ResponseApi.success(response);
  }
}
