package com.fis.boportalservice.core.domain.repository;

import com.fis.boportalservice.core.domain.model.AllowedIpRange;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AllowedIpRangeRepository {
  List<AllowedIpRange> findAllActive();

  List<AllowedIpRange> findAll();

  Optional<AllowedIpRange> findById(UUID id);

  AllowedIpRange save(AllowedIpRange ipRange);

  void deleteById(UUID id);
}
