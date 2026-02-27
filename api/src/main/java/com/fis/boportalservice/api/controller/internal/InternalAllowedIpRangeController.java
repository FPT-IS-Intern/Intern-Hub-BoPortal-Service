package com.fis.boportalservice.api.controller.internal;

import com.fis.boportalservice.api.dto.response.BoPortalAllowedIpRangeResponse;
import com.fis.boportalservice.api.mapper.AllowedIpRangeApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.service.AllowedIpRangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "Internal - Allowed IP Ranges")
@RestController
@RequestMapping("/bo-portal/internal")
@RequiredArgsConstructor
public class InternalAllowedIpRangeController {

    private final AllowedIpRangeService allowedIpRangeService;
    private final AllowedIpRangeApiMapper apiMapper;

    @GetMapping("/allowed-ip-ranges")
    public ResponseApi<List<BoPortalAllowedIpRangeResponse>> getAllowedIpRanges() {
        log.info("Internal request to get all allowed IP ranges");
        List<BoPortalAllowedIpRangeResponse> responses = allowedIpRangeService.getAllowedIpRanges()
                .stream()
                .map(apiMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseApi.success(responses);
    }
}
