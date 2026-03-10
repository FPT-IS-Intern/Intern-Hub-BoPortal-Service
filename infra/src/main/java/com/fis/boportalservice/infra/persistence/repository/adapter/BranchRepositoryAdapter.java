package com.fis.boportalservice.infra.persistence.repository.adapter;

import com.fis.boportalservice.core.domain.model.Branch;
import com.fis.boportalservice.core.domain.repository.BranchRepository;
import com.fis.boportalservice.infra.persistence.entity.BranchEntity;
import com.fis.boportalservice.infra.persistence.mapper.BranchEntityMapper;
import com.fis.boportalservice.infra.persistence.repository.BranchJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BranchRepositoryAdapter implements BranchRepository {

  private final BranchJPARepository jpaRepository;
  private final BranchEntityMapper entityMapper;

  @Override
  public List<Branch> findAll() {
    return jpaRepository.findAll().stream()
        .map(entityMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public List<Branch> findAllActive() {
    return jpaRepository.findByIsActiveTrue().stream()
        .map(entityMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Branch> findById(UUID id) {
    return jpaRepository.findById(id).map(entityMapper::toDomain);
  }

  @Override
  public Branch save(Branch branch) {
    BranchEntity entity = entityMapper.toEntity(branch);
    BranchEntity saved = jpaRepository.save(entity);
    return entityMapper.toDomain(saved);
  }

  @Override
  public void deleteById(UUID id) {
    jpaRepository.deleteById(id);
  }
}
