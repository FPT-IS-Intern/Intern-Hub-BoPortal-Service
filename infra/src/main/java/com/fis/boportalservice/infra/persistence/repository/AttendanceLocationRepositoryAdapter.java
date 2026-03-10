package com.fis.boportalservice.infra.persistence.repository;

import com.fis.boportalservice.core.domain.model.AttendanceLocation;
import com.fis.boportalservice.core.domain.repository.AttendanceLocationRepository;
import com.fis.boportalservice.infra.persistence.entity.AttendanceLocationEntity;
import com.fis.boportalservice.infra.persistence.mapper.AttendanceLocationEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AttendanceLocationRepositoryAdapter implements AttendanceLocationRepository {

  private final AttendanceLocationJPARepository jpaRepository;
  private final AttendanceLocationEntityMapper entityMapper;

  @Override
  public List<AttendanceLocation> findAll() {
    return jpaRepository.findAll().stream()
        .map(entityMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public List<AttendanceLocation> findAllActive() {
    return jpaRepository.findByIsActiveTrue().stream()
        .map(entityMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<AttendanceLocation> findById(UUID id) {
    return jpaRepository.findById(id).map(entityMapper::toDomain);
  }

  @Override
  public AttendanceLocation save(AttendanceLocation location) {
    AttendanceLocationEntity entity = entityMapper.toEntity(location);
    AttendanceLocationEntity saved = jpaRepository.save(entity);
    return entityMapper.toDomain(saved);
  }

  @Override
  public void deleteById(UUID id) {
    jpaRepository.deleteById(id);
  }
}
