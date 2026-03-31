package com.fis.boportalservice.infra.feignclient;

import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.infra.configuration.FeignClientCommonConfiguration;
import com.fis.boportalservice.infra.feignclient.dto.HrmBulkManagerUpdateRequest;
import com.fis.boportalservice.infra.feignclient.dto.HrmBulkManagerUpdateResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmInitializeRootRequest;
import com.fis.boportalservice.infra.feignclient.dto.HrmFilterRequest;
import com.fis.boportalservice.infra.feignclient.dto.HrmFilterResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmOrgChartPageResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmOrgChartPathResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmOrgChartUserDetailResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmOrgChartUserLiteResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmOrgChartUserNodeResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmPageResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmPositionResponse;
import com.fis.boportalservice.infra.feignclient.dto.HrmUpdateProfileRequest;
import com.fis.boportalservice.infra.feignclient.dto.HrmUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "hrm-service",
    url = "${feign.client.config.hrm-service.url:http://hrm-service:8080}",
    configuration = FeignClientCommonConfiguration.class
)
public interface HrmServiceClient {

  @GetMapping("/hrm/internal/users/{userId}")
  ResponseApi<HrmUserResponse> getUserById(@PathVariable("userId") Long userId);

  @GetMapping("/hrm/users/admin/profile/{userId}")
  ResponseApi<HrmUserResponse> getUserAdminProfile(@PathVariable("userId") Long userId);

  @GetMapping("/hrm/internal/positions")
  ResponseApi<java.util.List<HrmPositionResponse>> getPositions();

  @PostMapping("/hrm/internal/users/internal/filter")
  ResponseApi<HrmPageResponse<HrmFilterResponse>> filterUsers(
      @RequestBody HrmFilterRequest request,
      @RequestParam("page") int page,
      @RequestParam("size") int size
  );

  @PatchMapping(value = "/hrm/internal/users/{userId}/profile", consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseApi<Object> updateUserProfile(@PathVariable("userId") Long userId, @RequestBody HrmUpdateProfileRequest request);

  @PutMapping("/hrm/internal/users/{userId}/mentor")
  ResponseApi<Void> assignMentor(@PathVariable("userId") Long userId, @RequestParam(value = "mentorId", required = false) Long mentorId);

  @GetMapping("/hrm/internal/orgchart")
  ResponseApi<HrmOrgChartUserNodeResponse> getOrgChart(
      @RequestParam(value = "rootId", required = false) Long rootId,
      @RequestParam(value = "maxDepth", defaultValue = "1") int maxDepth
  );

  @GetMapping("/hrm/internal/orgchart/users/{userId}/subordinates")
  ResponseApi<HrmOrgChartPageResponse<HrmOrgChartUserNodeResponse>> getOrgChartSubordinates(
      @PathVariable("userId") Long userId,
      @RequestParam("page") int page,
      @RequestParam("limit") int limit
  );

  @GetMapping("/hrm/internal/orgchart/users/{userId}")
  ResponseApi<HrmOrgChartUserDetailResponse> getOrgChartUserDetail(@PathVariable("userId") Long userId);

  @PostMapping("/hrm/internal/orgchart/root")
  ResponseApi<HrmOrgChartUserDetailResponse> initializeOrgChartRoot(@RequestBody HrmInitializeRootRequest request);

  @GetMapping("/hrm/internal/orgchart/users")
  ResponseApi<HrmOrgChartPageResponse<HrmOrgChartUserNodeResponse>> searchOrgChartUsers(
      @RequestParam(value = "q", required = false) String query,
      @RequestParam(value = "department", required = false) String department,
      @RequestParam(value = "status", required = false) String status,
      @RequestParam("page") int page,
      @RequestParam("limit") int limit
  );

  @GetMapping("/hrm/internal/orgchart/assignable-users")
  ResponseApi<HrmOrgChartPageResponse<HrmOrgChartUserLiteResponse>> getAssignableOrgChartUsers(
      @RequestParam(value = "q", required = false) String query,
      @RequestParam("page") int page,
      @RequestParam("limit") int limit
  );

  @PutMapping("/hrm/internal/orgchart/users/manager")
  ResponseApi<HrmBulkManagerUpdateResponse> bulkUpdateOrgChartManager(
      @RequestBody HrmBulkManagerUpdateRequest request
  );

  @GetMapping("/hrm/internal/orgchart/users/{userId}/path")
  ResponseApi<HrmOrgChartPathResponse> getOrgChartPath(@PathVariable("userId") Long userId);
}
