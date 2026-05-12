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
    log.info("API - Get all branches request");
    List<BranchResponse> responses = branchService.getAll().stream()
        .map(apiMapper::toResponse)
        .collect(Collectors.toList());
    log.info("API - Get all branches response: total={}", responses.size());
    return ResponseApi.success(responses);
  }

  @GetMapping("/with-checkin-rules")
  public ResponseApi<List<BranchCheckinRulesResponse>> getAllWithCheckinRules() {
    log.info("event=BRANCH_WITH_CHECKIN_RULE_LIST_REQUEST");
    log.info("API - Get all branches with checkin rules request");
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
      log.info("API - Get all branches with checkin rules response: total={}", responses.size());
    return ResponseApi.success(responses);
  }

  @GetMapping("/{id}")
  public ResponseApi<BranchResponse> getById(@PathVariable UUID id) {
    log.info("event=BRANCH_DETAIL_REQUEST id={}", id);
    log.info("API - Get branch by id request: id={}", id);
    BranchResponse response = apiMapper.toResponse(branchService.getById(id));
    log.info("API - Get branch by id response: id={}, name={}", id, response.getName());
    return ResponseApi.success(response);
  }

  @PostMapping
  public ResponseApi<BranchResponse> create(@RequestBody BranchRequest request) {
    log.info("event=BRANCH_CREATE_REQUEST name={}", request.getName());
    log.info("API - Create branch request: name={}", request.getName());
    Branch domain = apiMapper.toDomain(request);
    Branch saved = branchService.create(domain);
    BranchResponse response = apiMapper.toResponse(saved);
    log.info("API - Create branch response: id={}, name={}", response.getId(), response.getName());
    return ResponseApi.success(response);
  }

  @PutMapping("/{id}")
  public ResponseApi<BranchResponse> update(@PathVariable UUID id,
                                            @RequestBody BranchRequest request) {
    log.info("event=BRANCH_UPDATE_REQUEST id={} name={}", id, request.getName());
    log.info("API - Update branch request: id={}, name={}", id, request.getName());
    Branch domain = apiMapper.toDomain(request);
    Branch updated = branchService.update(id, domain);
    BranchResponse response = apiMapper.toResponse(updated);
    log.info("API - Update branch response: id={}, name={}", id, response.getName());
    return ResponseApi.success(response);
  }

  @DeleteMapping("/{id}")
  public ResponseApi<Void> delete(@PathVariable UUID id) {
    log.info("event=BRANCH_DELETE_REQUEST id={}", id);
    log.info("API - Delete branch request: id={}", id);
    branchService.delete(id);
    log.info("API - Delete branch response: id={}, result=success", id);
    return ResponseApi.success(null);
  }
}

