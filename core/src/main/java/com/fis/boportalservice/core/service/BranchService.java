package com.fis.boportalservice.core.service;

import com.fis.boportalservice.core.domain.model.Branch;

import java.util.List;
import java.util.UUID;

public interface BranchService {
  List<Branch> getAll();

  List<Branch> getAllActive();

  Branch getById(UUID id);

  Branch create(Branch branch);

  Branch update(UUID id, Branch branch);

  void delete(UUID id);
}
