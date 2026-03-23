package com.fis.boportalservice.infra.feignclient;

import com.fis.boportalservice.infra.configuration.FeignClientCommonConfiguration;
import com.fis.boportalservice.infra.feignclient.dto.AuditHashCheckDto;
import com.fis.boportalservice.infra.feignclient.dto.AuditPageDto;
import com.fis.boportalservice.infra.feignclient.dto.AuditRehashDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

import static org.springframework.format.annotation.DateTimeFormat.ISO;

@FeignClient(name = "audit-service", url = "${feign.client.config.audit-service.url:http://audit-service:8080}", configuration = FeignClientCommonConfiguration.class)
public interface AuditServiceClient {

    @GetMapping("/audit/internal")
    ResponseFeignClient<AuditPageDto> queryAudits(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate endDate,
            @RequestParam(value = "day", required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate day,
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "actorIds", required = false) java.util.List<String> actorIds,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortDirection", required = false) String sortDirection);

    @GetMapping("/audit/internal/{auditId}/hash/verify")
    ResponseFeignClient<AuditHashCheckDto> verifyHashByAuditId(@PathVariable("auditId") Long auditId);

    @PostMapping("/audit/internal/hash/rehash")
    ResponseFeignClient<AuditRehashDto> rehashAllBeforeDay(
            @RequestParam("beforeDay") @DateTimeFormat(iso = ISO.DATE) LocalDate beforeDay);

    @PostMapping("/audit/internal/action-functions")
    ResponseFeignClient<com.fis.boportalservice.infra.feignclient.dto.ActionFunctionDto> createActionFunction(
            @org.springframework.web.bind.annotation.RequestBody com.fis.boportalservice.infra.feignclient.dto.ActionFunctionRequestDto request);

    @GetMapping("/audit/internal/action-functions")
    ResponseFeignClient<java.util.List<com.fis.boportalservice.infra.feignclient.dto.ActionFunctionDto>> getAllActionFunctions();

}
