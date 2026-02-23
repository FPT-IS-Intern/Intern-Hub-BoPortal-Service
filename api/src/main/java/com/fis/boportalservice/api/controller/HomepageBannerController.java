package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.response.HomepageBannerResponse;
import com.fis.boportalservice.api.mapper.HomepageBannerApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.service.HomepageBannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/banners")
@RequiredArgsConstructor
public class HomepageBannerController {

    private final HomepageBannerService bannerService;
    private final HomepageBannerApiMapper apiMapper;

    @GetMapping
    public ResponseApi<List<HomepageBannerResponse>> getActiveBanners() {
        List<HomepageBannerResponse> responses = bannerService.getActiveBanners().stream()
                .map(apiMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseApi.success(responses);
    }
}
