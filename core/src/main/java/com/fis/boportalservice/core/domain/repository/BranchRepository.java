package com.fis.boportalservice.core.domain.repository;

import com.fis.boportalservice.core.domain.model.Branch;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BranchRepository {
    List<Branch> findAll();

    List<Branch> findAllActive();

    Optional<Branch> findById(UUID id);

    Branch save(Branch branch);

    void deleteById(UUID id);
}
