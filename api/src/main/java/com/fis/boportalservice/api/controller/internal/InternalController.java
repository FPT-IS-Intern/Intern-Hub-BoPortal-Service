package com.fis.boportalservice.api.controller.internal;

import com.fis.boportalservice.api.dto.response.*;
import com.fis.boportalservice.api.mapper.*;
import com.fis.boportalservice.api.util.MenuHierarchyHelper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.domain.model.BoTokenClaims;
import com.fis.boportalservice.core.domain.repository.BoTokenProvider;
import com.fis.boportalservice.core.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
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

  private final HomepageBannerService bannerService;
  private final HomepageBannerApiMapper homepageBannerApiMapper;

  private final SystemConfigService systemConfigService;
  private final SystemConfigApiMapper systemConfigApiMapper;

  private final PortalMenuService menuService;
  private final PortalMenuApiMapper menuApiMapper;
  private final BoTokenProvider boTokenProvider;

  // ─── Allowed IP Ranges ────────────────────────────────────────────────────

  @GetMapping("/allowed-ip-ranges")
  public ResponseApi<List<BoPortalAllowedIpRangeResponse>> getAllowedIpRanges() {
    log.info("Internal request to get all allowed IP ranges");
    List<BoPortalAllowedIpRangeResponse> responses = allowedIpRangeService.getAllowedIpRanges()
        .stream()
        .map(allowedIpRangeApiMapper::toResponse)
        .collect(Collectors.toList());
    return ResponseApi.success(responses);
  }

  // ─── Attendance Locations ─────────────────────────────────────────────────

  @GetMapping("/attendance-locations")
  public ResponseApi<List<AttendanceLocationResponse>> getActiveLocations() {
    log.info("Internal request to get all active attendance locations");
    List<AttendanceLocationResponse> responses = attendanceLocationService.getAllActive()
        .stream()
        .map(attendanceLocationApiMapper::toResponse)
        .collect(Collectors.toList());
    return ResponseApi.success(responses);
  }

  // ─── Homepage Banners ─────────────────────────────────────────────────────

  @GetMapping("/banners")
  public ResponseApi<List<HomepageBannerResponse>> getActiveBanners() {
    log.info("Internal request to get active homepage banners");
    List<HomepageBannerResponse> responses = bannerService.getActiveBanners().stream()
        .map(homepageBannerApiMapper::toResponse)
        .collect(Collectors.toList());
    return ResponseApi.success(responses);
  }

  // ─── System Configuration ─────────────────────────────────────────────────

  @GetMapping("/ui-client-config")
  public ResponseApi<SystemConfigPublicResponse> getUiClientConfig() {
    log.info("Internal request to get UI client configuration");
    return ResponseApi.success(systemConfigApiMapper.toPublicResponse(systemConfigService.getSystemConfig()));
  }

  @GetMapping("/working-time-config")
  public ResponseApi<SystemConfigWorkingTimeResponse> getWorkingTimeConfig() {
    log.info("Internal request to get working time configuration");
    return ResponseApi.success(systemConfigApiMapper.toWorkingTimeResponse(systemConfigService.getSystemConfig()));
  }

  // ─── Portal Init ──────────────────────────────────────────────────────────

  @GetMapping("/init")
  public ResponseApi<PortalInitResponse> getPortalInit(HttpServletRequest request) {
    log.info("Internal request to initialize portal data");
    SystemConfigPublicResponse config = systemConfigApiMapper
        .toPublicResponse(systemConfigService.getSystemConfig());

    List<String> userPermissions = getUserPermissions(request);
    log.info("User permissions: {}", userPermissions);

    List<PortalMenuResponse> flatMenus = menuApiMapper
        .toResponseList(menuService.getAvailableMenus(userPermissions));

    List<PortalMenuResponse> hierarchicalMenus = MenuHierarchyHelper.buildMenuHierarchy(flatMenus);

    return ResponseApi.success(PortalInitResponse.builder()
        .systemConfig(config)
        .menus(hierarchicalMenus)
        .build());
  }

  // ─── Helpers ──────────────────────────────────────────────────────────────

  private List<String> getUserPermissions(HttpServletRequest request) {
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (principal instanceof BoTokenClaims boTokenClaims) {
        if (boTokenClaims.getPermissions() != null && !boTokenClaims.getPermissions().isEmpty()) {
          return boTokenClaims.getPermissions();
        }
      }
    }
    return getPermissionsFromBoToken(request);
  }

  private List<String> getPermissionsFromBoToken(HttpServletRequest request) {
    String authorization = request.getHeader("Authorization");
    if (!StringUtils.hasText(authorization) || !authorization.startsWith("Bearer ")) {
      return Collections.emptyList();
    }
    String token = authorization.substring("Bearer ".length());
    try {
      BoTokenClaims claims = boTokenProvider.parseAccessToken(token);
      return claims.getPermissions() == null ? Collections.emptyList() : claims.getPermissions();
    } catch (Exception ex) {
      log.debug("Cannot parse BO token permission list", ex);
      return Collections.emptyList();
    }
  }
}
