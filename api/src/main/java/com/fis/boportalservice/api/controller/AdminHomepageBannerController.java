package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.HomepageBannerRequest;
import com.fis.boportalservice.api.dto.response.HomepageBannerResponse;
import com.fis.boportalservice.api.mapper.HomepageBannerApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.domain.model.HomepageBanner;
import com.fis.boportalservice.core.service.HomepageBannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/banners")
@RequiredArgsConstructor
public class AdminHomepageBannerController {

    private final HomepageBannerService bannerService;
    private final HomepageBannerApiMapper apiMapper;

    @GetMapping
    public ResponseApi<List<HomepageBannerResponse>> getAllBanners() {
        log.info("Request to get all homepage banners");
        List<HomepageBannerResponse> responses = bannerService.getAllBanners().stream()
                .map(apiMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseApi.success(responses);
    }

    @GetMapping("/{id}")
    public ResponseApi<HomepageBannerResponse> getBannerById(@PathVariable UUID id) {
        log.info("Request to get homepage banner by id: {}", id);
        HomepageBannerResponse response = apiMapper.toResponse(bannerService.getBannerById(id));
        return ResponseApi.success(response);
    }

    @PostMapping
    public ResponseApi<HomepageBannerResponse> createBanner(@RequestBody HomepageBannerRequest request) {
        log.info("Request to create homepage banner: {}", request.getTitle());
        HomepageBanner domain = apiMapper.toDomain(request);
        HomepageBanner saved = bannerService.createBanner(domain);
        return ResponseApi.success(apiMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseApi<HomepageBannerResponse> updateBanner(@PathVariable UUID id,
            @RequestBody HomepageBannerRequest request) {
        log.info("Request to update homepage banner: id={}, title={}", id, request.getTitle());
        HomepageBanner domain = apiMapper.toDomain(request);
        HomepageBanner updated = bannerService.updateBanner(id, domain);
        return ResponseApi.success(apiMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseApi<Void> deleteBanner(@PathVariable UUID id) {
        log.info("Request to delete homepage banner: {}", id);
        bannerService.deleteBanner(id);
        return ResponseApi.success(null);
    }
}
