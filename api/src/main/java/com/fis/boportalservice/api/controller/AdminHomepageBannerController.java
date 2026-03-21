package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.HomepageBannerRequest;
import com.fis.boportalservice.api.dto.response.HomepageBannerResponse;
import com.fis.boportalservice.api.mapper.HomepageBannerApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.domain.model.HomepageBanner;
import com.fis.boportalservice.core.service.HomepageBannerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "3. Admin - Homepage Banners")
@RestController
@RequestMapping("/bo-portal/banners")
@RequiredArgsConstructor
public class AdminHomepageBannerController {

  private final HomepageBannerService bannerService;
  private final HomepageBannerApiMapper apiMapper;

  @GetMapping
  public ResponseApi<List<HomepageBannerResponse>> getAllBanners() {
    log.info("event=HOMEPAGE_BANNER_LIST_REQUEST");
    List<HomepageBannerResponse> responses = bannerService.getAllBanners().stream()
        .map(apiMapper::toResponse)
        .collect(Collectors.toList());
    return ResponseApi.success(responses);
  }

  @GetMapping("/{id}")
  public ResponseApi<HomepageBannerResponse> getBannerById(@PathVariable UUID id) {
    log.info("event=HOMEPAGE_BANNER_DETAIL_REQUEST id={}", id);
    HomepageBannerResponse response = apiMapper.toResponse(bannerService.getBannerById(id));
    return ResponseApi.success(response);
  }

  @PostMapping
  public ResponseApi<HomepageBannerResponse> createBanner(@RequestBody HomepageBannerRequest request) {
    log.info("event=HOMEPAGE_BANNER_CREATE_REQUEST title={}", request.getTitle());
    HomepageBanner domain = apiMapper.toDomain(request);
    HomepageBanner saved = bannerService.createBanner(domain);
    return ResponseApi.success(apiMapper.toResponse(saved));
  }

  @PutMapping("/{id}")
  public ResponseApi<HomepageBannerResponse> updateBanner(@PathVariable UUID id,
                                                          @RequestBody HomepageBannerRequest request) {
    log.info("event=HOMEPAGE_BANNER_UPDATE_REQUEST id={} title={}", id, request.getTitle());
    HomepageBanner domain = apiMapper.toDomain(request);
    HomepageBanner updated = bannerService.updateBanner(id, domain);
    return ResponseApi.success(apiMapper.toResponse(updated));
  }

  @DeleteMapping("/{id}")
  public ResponseApi<Void> deleteBanner(@PathVariable UUID id) {
    log.info("event=HOMEPAGE_BANNER_DELETE_REQUEST id={}", id);
    bannerService.deleteBanner(id);
    return ResponseApi.success(null);
  }
}

