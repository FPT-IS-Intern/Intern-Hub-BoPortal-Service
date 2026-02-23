package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.LoginUserInfo;
import com.fis.boportalservice.api.dto.response.PortalInitResponse;
import com.fis.boportalservice.api.dto.response.PortalMenuResponse;
import com.fis.boportalservice.api.dto.response.SystemConfigPublicResponse;
import com.fis.boportalservice.api.mapper.PortalMenuApiMapper;
import com.fis.boportalservice.api.mapper.SystemConfigApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.service.PortalMenuService;
import com.fis.boportalservice.core.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/portal")
@RequiredArgsConstructor
public class PortalController {

    private final SystemConfigService systemConfigService;
    private final PortalMenuService menuService;
    private final SystemConfigApiMapper systemConfigApiMapper;
    private final PortalMenuApiMapper menuApiMapper;

    @GetMapping("/init")
    public ResponseApi<PortalInitResponse> getPortalInit() {
        // 1. Get Public System Config
        SystemConfigPublicResponse config = systemConfigApiMapper
                .toPublicResponse(systemConfigService.getSystemConfig());

        // 2. Get User Permissions from Security Context
        List<String> userPermissions = getUserPermissions();

        // 3. Get Filtered Menus
        List<PortalMenuResponse> menus = menuApiMapper.toResponseList(menuService.getAvailableMenus(userPermissions));

        return ResponseApi.success(PortalInitResponse.builder()
                .systemConfig(config)
                .menus(menus)
                .build());
    }

    private List<String> getUserPermissions() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof LoginUserInfo) {
            LoginUserInfo userInfo = (LoginUserInfo) principal;
            if (userInfo.INDI != null && StringUtils.hasText(userInfo.INDI.featureList)) {
                return Arrays.asList(userInfo.INDI.featureList.split(","));
            }
        }
        return Collections.emptyList();
    }
}
