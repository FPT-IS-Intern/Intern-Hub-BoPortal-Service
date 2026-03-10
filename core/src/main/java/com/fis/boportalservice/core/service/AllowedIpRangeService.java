package com.fis.boportalservice.core.service;

import com.fis.boportalservice.core.domain.model.AllowedIpRange;

import java.util.List;
import java.util.UUID;

public interface AllowedIpRangeService {
  List<AllowedIpRange> getAllowedIpRanges();

  List<AllowedIpRange> getAll();

  AllowedIpRange getById(UUID id);

  AllowedIpRange create(AllowedIpRange ipRange);

  AllowedIpRange update(UUID id, AllowedIpRange ipRange);

  void delete(UUID id);
}
