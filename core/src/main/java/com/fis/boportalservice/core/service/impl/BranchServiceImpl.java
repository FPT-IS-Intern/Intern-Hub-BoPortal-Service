package com.fis.boportalservice.core.service.impl;

import com.fis.boportalservice.core.domain.model.Branch;
import com.fis.boportalservice.core.domain.repository.BranchRepository;
import com.fis.boportalservice.core.service.BranchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

  private final BranchRepository branchRepository;

  @Override
  public List<Branch> getAll() {
    return branchRepository.findAll();
  }

  @Override
  public List<Branch> getAllActive() {
    return branchRepository.findAllActive();
  }

  @Override
  public Branch getById(UUID id) {
    return branchRepository.findById(id)
        .orElseThrow(() -> {
          log.error("Branch not found with id: {}", id);
          return new RuntimeException("Branch not found with id: " + id);
        });
  }

  @Override
  public Branch create(Branch branch) {
    log.info("event=BRANCH_PERSIST_CREATE name={}", branch.getName());
    return branchRepository.save(branch);
  }

  @Override
  public Branch update(UUID id, Branch branch) {
    log.info("event=BRANCH_PERSIST_UPDATE id={}", id);
    Branch existing = getById(id);
    existing.setName(branch.getName());
    existing.setDescription(branch.getDescription());
    existing.setIsActive(branch.getIsActive());
    return branchRepository.save(existing);
  }

  @Override
  public void delete(UUID id) {
    log.info("event=BRANCH_PERSIST_DELETE id={}", id);
    branchRepository.deleteById(id);
  }
}

