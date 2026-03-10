package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.BranchRequest;
import com.fis.boportalservice.api.dto.response.BranchResponse;
import com.fis.boportalservice.api.mapper.BranchApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.domain.model.Branch;
import com.fis.boportalservice.core.service.BranchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

  @GetMapping
  public ResponseApi<List<BranchResponse>> getAll() {
    log.info("Request to get all branches");
    List<BranchResponse> responses = branchService.getAll().stream()
        .map(apiMapper::toResponse)
        .collect(Collectors.toList());
    return ResponseApi.success(responses);
  }

  @GetMapping("/{id}")
  public ResponseApi<BranchResponse> getById(@PathVariable UUID id) {
    log.info("Request to get branch by id: {}", id);
    return ResponseApi.success(apiMapper.toResponse(branchService.getById(id)));
  }

  @PostMapping
  public ResponseApi<BranchResponse> create(@RequestBody BranchRequest request) {
    log.info("Request to create branch: {}", request.getName());
    Branch domain = apiMapper.toDomain(request);
    Branch saved = branchService.create(domain);
    return ResponseApi.success(apiMapper.toResponse(saved));
  }

  @PutMapping("/{id}")
  public ResponseApi<BranchResponse> update(@PathVariable UUID id,
                                            @RequestBody BranchRequest request) {
    log.info("Request to update branch: id={}, name={}", id, request.getName());
    Branch domain = apiMapper.toDomain(request);
    Branch updated = branchService.update(id, domain);
    return ResponseApi.success(apiMapper.toResponse(updated));
  }

  @DeleteMapping("/{id}")
  public ResponseApi<Void> delete(@PathVariable UUID id) {
    log.info("Request to delete branch: {}", id);
    branchService.delete(id);
    return ResponseApi.success(null);
  }
}
