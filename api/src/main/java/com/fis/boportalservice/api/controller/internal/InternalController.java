package com.fis.boportalservice.api.controller.internal;

import com.fis.boportalservice.api.dto.request.SidebarMenuRequest;
import com.fis.boportalservice.api.dto.response.AttendanceLocationResponse;
import com.fis.boportalservice.api.dto.response.BoPortalAllowedIpRangeResponse;
import com.fis.boportalservice.api.dto.response.BranchResponse;
import com.fis.boportalservice.api.dto.response.HomepageBannerResponse;
import com.fis.boportalservice.api.dto.response.PortalMenuResponse;
import com.fis.boportalservice.api.dto.response.SystemConfigPublicResponse;
import com.fis.boportalservice.api.dto.response.SystemConfigWorkingTimeResponse;
import com.fis.boportalservice.api.mapper.AllowedIpRangeApiMapper;
import com.fis.boportalservice.api.mapper.AttendanceLocationApiMapper;
import com.fis.boportalservice.api.mapper.BranchApiMapper;
import com.fis.boportalservice.api.mapper.HomepageBannerApiMapper;
import com.fis.boportalservice.api.mapper.PortalMenuApiMapper;
import com.fis.boportalservice.api.mapper.SystemConfigApiMapper;
import com.fis.boportalservice.api.util.MenuHierarchyHelper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.service.AllowedIpRangeService;
import com.fis.boportalservice.core.service.AttendanceLocationService;
import com.fis.boportalservice.core.service.BranchService;
import com.fis.boportalservice.core.service.HomepageBannerService;
import com.fis.boportalservice.core.service.PortalMenuService;
import com.fis.boportalservice.core.service.SystemConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "6. Internal APIs")
@RestController
@RequestMapping("/bo-portal/internal")
@RequiredArgsConstructor
public class InternalController {

  private final AllowedIpRangeService allowedIpRangeService;
  private final AllowedIpRangeApiMapper allowedIpRangeApiMapper;
  private final AttendanceLocationService attendanceLocationService;
  private final AttendanceLocationApiMapper attendanceLocationApiMapper;
  private final BranchService branchService;
  private final BranchApiMapper branchApiMapper;
  private final HomepageBannerService bannerService;
  private final HomepageBannerApiMapper homepageBannerApiMapper;
  private final SystemConfigService systemConfigService;
  private final SystemConfigApiMapper systemConfigApiMapper;
  private final PortalMenuService menuService;
  private final PortalMenuApiMapper menuApiMapper;

  @GetMapping("/allowed-ip-ranges")
  public ResponseApi<List<BoPortalAllowedIpRangeResponse>> getAllowedIpRanges() {
    log.info("event=INTERNAL_ALLOWED_IP_RANGE_LIST_REQUEST");
    List<BoPortalAllowedIpRangeResponse> responses = allowedIpRangeService.getAllowedIpRanges()
        .stream()
        .map(allowedIpRangeApiMapper::toResponse)
        .collect(Collectors.toList());
    return ResponseApi.success(responses);
  }

  @GetMapping("/attendance-locations")
  public ResponseApi<List<AttendanceLocationResponse>> getActiveLocations() {
    log.info("event=INTERNAL_ATTENDANCE_LOCATION_LIST_REQUEST");
    List<AttendanceLocationResponse> responses = attendanceLocationService.getAllActive()
        .stream()
        .map(attendanceLocationApiMapper::toResponse)
        .collect(Collectors.toList());
    return ResponseApi.success(responses);
  }

  @GetMapping("/branches")
  public ResponseApi<List<BranchResponse>> getBranches(
      @RequestParam(name = "activeOnly", defaultValue = "true") boolean activeOnly
  ) {
    log.info("event=INTERNAL_BRANCH_LIST_REQUEST activeOnly={}", activeOnly);
    List<BranchResponse> responses = (activeOnly ? branchService.getAllActive() : branchService.getAll())
        .stream()
        .map(branchApiMapper::toResponse)
        .collect(Collectors.toList());
    return ResponseApi.success(responses);
  }

  @GetMapping("/banners")
  public ResponseApi<List<HomepageBannerResponse>> getActiveBanners() {
    log.info("event=INTERNAL_HOMEPAGE_BANNER_LIST_REQUEST");
    List<HomepageBannerResponse> responses = bannerService.getActiveBanners().stream()
        .map(homepageBannerApiMapper::toResponse)
        .collect(Collectors.toList());
    return ResponseApi.success(responses);
  }

  @GetMapping("/ui-client-config")
  public ResponseApi<SystemConfigPublicResponse> getUiClientConfig() {
    log.info("event=INTERNAL_UI_CLIENT_CONFIG_REQUEST");
    return ResponseApi.success(systemConfigApiMapper.toPublicResponse(systemConfigService.getSystemConfig()));
  }

  @GetMapping("/working-time-config")
  public ResponseApi<SystemConfigWorkingTimeResponse> getWorkingTimeConfig() {
    log.info("event=INTERNAL_WORKING_TIME_CONFIG_REQUEST");
    return ResponseApi.success(systemConfigApiMapper.toWorkingTimeResponse(systemConfigService.getSystemConfig()));
  }

  @PostMapping("/sidebar-menus")
  public ResponseApi<List<PortalMenuResponse>> getSidebarMenus(
      @RequestBody(required = false) SidebarMenuRequest request
  ) {
    List<String> userRoles = request == null ? List.of() : request.resolveRoles();
    log.info("event=INTERNAL_SIDEBAR_MENU_REQUEST roles={}", userRoles);

    List<PortalMenuResponse> flatMenus = menuApiMapper
        .toResponseList(menuService.getAvailableMenus(userRoles));

    return ResponseApi.success(MenuHierarchyHelper.buildMenuHierarchy(flatMenus));
  }
}

