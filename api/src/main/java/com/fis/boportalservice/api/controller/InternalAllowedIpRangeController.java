package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.response.BoPortalAllowedIpRangeResponse;
import com.fis.boportalservice.api.mapper.AllowedIpRangeApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.service.AllowedIpRangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalAllowedIpRangeController {

    private final AllowedIpRangeService allowedIpRangeService;
    private final AllowedIpRangeApiMapper apiMapper;

    @GetMapping("/allowed-ip-ranges")
    public ResponseApi<List<BoPortalAllowedIpRangeResponse>> getAllowedIpRanges() {
        List<BoPortalAllowedIpRangeResponse> responses = allowedIpRangeService.getAllowedIpRanges()
                .stream()
                .map(apiMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseApi.success(responses);
    }
}
