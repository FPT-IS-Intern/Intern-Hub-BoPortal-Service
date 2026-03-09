package com.fis.boportalservice.api.controller.internal;

import com.fis.boportalservice.api.dto.LoginUserInfo;
import com.fis.boportalservice.api.dto.response.BoPortalAllowedIpRangeResponse;
import com.fis.boportalservice.api.dto.response.AttendanceLocationResponse;
import com.fis.boportalservice.api.dto.response.HomepageBannerResponse;
import com.fis.boportalservice.api.dto.response.PortalInitResponse;
import com.fis.boportalservice.api.dto.response.PortalMenuResponse;
import com.fis.boportalservice.api.dto.response.SystemConfigInternalResponse;
import com.fis.boportalservice.api.dto.response.SystemConfigPublicResponse;
import com.fis.boportalservice.api.mapper.AllowedIpRangeApiMapper;
import com.fis.boportalservice.api.mapper.AttendanceLocationApiMapper;
import com.fis.boportalservice.api.mapper.HomepageBannerApiMapper;
import com.fis.boportalservice.api.mapper.PortalMenuApiMapper;
import com.fis.boportalservice.api.mapper.SystemConfigApiMapper;
import com.fis.boportalservice.api.util.MenuHierarchyHelper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.domain.model.BoTokenClaims;
import com.fis.boportalservice.core.domain.repository.BoTokenProvider;
import com.fis.boportalservice.core.service.AllowedIpRangeService;
import com.fis.boportalservice.core.service.AttendanceLocationService;
import com.fis.boportalservice.core.service.HomepageBannerService;
import com.fis.boportalservice.core.service.PortalMenuService;
import com.fis.boportalservice.core.service.SystemConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
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

    @GetMapping("/system-config")
    public ResponseApi<SystemConfigInternalResponse> getSystemConfig() {
        log.info("Internal request to get full system configuration");
        return ResponseApi.success(systemConfigApiMapper.toInternalResponse(systemConfigService.getSystemConfig()));
    }

    @GetMapping("/system-config/public")
    public ResponseApi<SystemConfigPublicResponse> getPublicSystemConfig() {
        log.info("Internal request to get public system configuration");
        return ResponseApi.success(systemConfigApiMapper.toPublicResponse(systemConfigService.getSystemConfig()));
    }

    // ─── Portal Init ──────────────────────────────────────────────────────────

    @GetMapping("/init")
    public ResponseApi<PortalInitResponse> getPortalInit(HttpServletRequest request) {
        log.info("Internal request to initialize portal data");
        SystemConfigPublicResponse config = systemConfigApiMapper
                .toPublicResponse(systemConfigService.getSystemConfig ());

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
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return getPermissionsFromBoToken(request);
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof LoginUserInfo) {
            LoginUserInfo userInfo = (LoginUserInfo) principal;
            if (userInfo.INDI != null && StringUtils.hasText(userInfo.INDI.featureList)) {
                return Arrays.asList(userInfo.INDI.featureList.split(","));
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
