package com.fis.boportalservice.api.controller.internal;

import com.fis.boportalservice.api.dto.response.HomepageBannerResponse;
import com.fis.boportalservice.api.mapper.HomepageBannerApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.service.HomepageBannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "Internal - Homepage Banners")
@RestController
@RequestMapping("/bo-portal/internal/banners")
@RequiredArgsConstructor
public class InternalHomepageBannerController {

    private final HomepageBannerService bannerService;
    private final HomepageBannerApiMapper apiMapper;

    @GetMapping
    public ResponseApi<List<HomepageBannerResponse>> getActiveBanners() {
        log.info("Internal request to get active homepage banners");
        List<HomepageBannerResponse> responses = bannerService.getActiveBanners().stream()
                .map(apiMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseApi.success(responses);
    }
}
