package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.BranchRequest;
import com.fis.boportalservice.api.dto.response.AttendanceLocationResponse;
import com.fis.boportalservice.api.dto.response.BoPortalAllowedIpRangeResponse;
import com.fis.boportalservice.api.dto.response.BranchCheckinRulesResponse;
import com.fis.boportalservice.api.dto.response.BranchResponse;
import com.fis.boportalservice.api.mapper.AllowedIpRangeApiMapper;
import com.fis.boportalservice.api.mapper.AttendanceLocationApiMapper;
import com.fis.boportalservice.api.mapper.BranchApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.domain.model.Branch;
import com.fis.boportalservice.core.service.AllowedIpRangeService;
import com.fis.boportalservice.core.service.AttendanceLocationService;
import com.fis.boportalservice.core.service.BranchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "5. Admin - Branches")
@RestController
@RequestMapping("/bo-portal/branches")
@RequiredArgsConstructor
public class AdminBranchController {

  private final BranchService branchService;
  private final BranchApiMapper apiMapper;
  private final AllowedIpRangeService allowedIpRangeService;
  private final AllowedIpRangeApiMapper allowedIpRangeApiMapper;
  private final AttendanceLocationService attendanceLocationService;
  private final AttendanceLocationApiMapper attendanceLocationApiMapper;

  @GetMapping
  public ResponseApi<List<BranchResponse>> getAll() {
    log.info("event=BRANCH_LIST_REQUEST");
    List<BranchResponse> responses = branchService.getAll().stream()
        .map(apiMapper::toResponse)
        .collect(Collectors.toList());
    return ResponseApi.success(responses);
  }

  @GetMapping("/with-checkin-rules")
  public ResponseApi<List<BranchCheckinRulesResponse>> getAllWithCheckinRules() {
    log.info("event=BRANCH_WITH_CHECKIN_RULE_LIST_REQUEST");
    Map<UUID, List<BoPortalAllowedIpRangeResponse>> ipRangesByBranch = allowedIpRangeService.getAll().stream()
        .map(allowedIpRangeApiMapper::toResponse)
        .filter(response -> response.getBranchId() != null)
        .collect(Collectors.groupingBy(BoPortalAllowedIpRangeResponse::getBranchId));

    Map<UUID, List<AttendanceLocationResponse>> locationsByBranch = attendanceLocationService.getAll().stream()
        .map(attendanceLocationApiMapper::toResponse)
        .filter(response -> response.getBranchId() != null)
        .collect(Collectors.groupingBy(AttendanceLocationResponse::getBranchId));

    List<BranchCheckinRulesResponse> responses = branchService.getAll().stream()
        .map(branch -> BranchCheckinRulesResponse.builder()
            .id(branch.getId())
            .name(branch.getName())
            .description(branch.getDescription())
            .isActive(branch.getIsActive())
            .allowedIpRanges(ipRangesByBranch.getOrDefault(branch.getId(), Collections.emptyList()))
            .attendanceLocations(locationsByBranch.getOrDefault(branch.getId(), Collections.emptyList()))
            .build())
        .collect(Collectors.toList());

    return ResponseApi.success(responses);
  }

  @GetMapping("/{id}")
  public ResponseApi<BranchResponse> getById(@PathVariable UUID id) {
    log.info("event=BRANCH_DETAIL_REQUEST id={}", id);
    return ResponseApi.success(apiMapper.toResponse(branchService.getById(id)));
  }

  @PostMapping
  public ResponseApi<BranchResponse> create(@RequestBody BranchRequest request) {
    log.info("event=BRANCH_CREATE_REQUEST name={}", request.getName());
    Branch domain = apiMapper.toDomain(request);
    Branch saved = branchService.create(domain);
    return ResponseApi.success(apiMapper.toResponse(saved));
  }

  @PutMapping("/{id}")
  public ResponseApi<BranchResponse> update(@PathVariable UUID id,
                                            @RequestBody BranchRequest request) {
    log.info("event=BRANCH_UPDATE_REQUEST id={} name={}", id, request.getName());
    Branch domain = apiMapper.toDomain(request);
    Branch updated = branchService.update(id, domain);
    return ResponseApi.success(apiMapper.toResponse(updated));
  }

  @DeleteMapping("/{id}")
  public ResponseApi<Void> delete(@PathVariable UUID id) {
    log.info("event=BRANCH_DELETE_REQUEST id={}", id);
    branchService.delete(id);
    return ResponseApi.success(null);
  }
}

