package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.AllowedIpRangeRequest;
import com.fis.boportalservice.api.dto.response.BoPortalAllowedIpRangeResponse;
import com.fis.boportalservice.api.mapper.AllowedIpRangeApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.domain.model.AllowedIpRange;
import com.fis.boportalservice.core.service.AllowedIpRangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admin/allowed-ip-ranges")
@RequiredArgsConstructor
public class AdminAllowedIpRangeController {

    private final AllowedIpRangeService allowedIpRangeService;
    private final AllowedIpRangeApiMapper apiMapper;

    @GetMapping
    public ResponseApi<List<BoPortalAllowedIpRangeResponse>> getAll() {
        log.info("Request to get all allowed IP ranges");
        List<BoPortalAllowedIpRangeResponse> responses = allowedIpRangeService.getAll().stream()
                .map(apiMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseApi.success(responses);
    }

    @GetMapping("/{id}")
    public ResponseApi<BoPortalAllowedIpRangeResponse> getById(@PathVariable UUID id) {
        log.info("Request to get allowed IP range by id: {}", id);
        return ResponseApi.success(apiMapper.toResponse(allowedIpRangeService.getById(id)));
    }

    @PostMapping
    public ResponseApi<BoPortalAllowedIpRangeResponse> create(@RequestBody AllowedIpRangeRequest request) {
        log.info("Request to create allowed IP range: {}", request.getName());
        AllowedIpRange domain = apiMapper.toDomain(request);
        AllowedIpRange saved = allowedIpRangeService.create(domain);
        return ResponseApi.success(apiMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseApi<BoPortalAllowedIpRangeResponse> update(@PathVariable UUID id,
            @RequestBody AllowedIpRangeRequest request) {
        log.info("Request to update allowed IP range: id={}, name={}", id, request.getName());
        AllowedIpRange domain = apiMapper.toDomain(request);
        AllowedIpRange updated = allowedIpRangeService.update(id, domain);
        return ResponseApi.success(apiMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseApi<Void> delete(@PathVariable UUID id) {
        log.info("Request to delete allowed IP range: {}", id);
        allowedIpRangeService.delete(id);
        return ResponseApi.success(null);
    }
}
