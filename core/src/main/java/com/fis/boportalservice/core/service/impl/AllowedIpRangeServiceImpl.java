package com.fis.boportalservice.core.service.impl;

import com.fis.boportalservice.core.domain.model.AllowedIpRange;
import com.fis.boportalservice.core.domain.repository.AllowedIpRangeRepository;
import com.fis.boportalservice.core.exception.ClientSideException;
import com.fis.boportalservice.core.exception.ErrorCode;
import com.fis.boportalservice.core.service.AllowedIpRangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class AllowedIpRangeServiceImpl implements AllowedIpRangeService {

  private final AllowedIpRangeRepository allowedIpRangeRepository;

  @Override
  public List<AllowedIpRange> getAllowedIpRanges() {
    return allowedIpRangeRepository.findAllActive();
  }

  @Override
  public List<AllowedIpRange> getAll() {
    return allowedIpRangeRepository.findAll();
  }

  @Override
  public AllowedIpRange getById(UUID id) {
    return allowedIpRangeRepository.findById(id)
        .orElseThrow(() -> {
          log.error("Allowed IP range not found with id: {}", id);
          return new ClientSideException(ErrorCode.NOT_FOUND, "Allowed IP range not found");
        });
  }

  @Override
  public AllowedIpRange create(AllowedIpRange ipRange) {
    return allowedIpRangeRepository.save(ipRange);
  }

  @Override
  public AllowedIpRange update(UUID id, AllowedIpRange ipRange) {
    log.info("event=ALLOWED_IP_RANGE_PERSIST_UPDATE id={}", id);
    AllowedIpRange existing = getById(id);
    existing.setName(ipRange.getName());
    existing.setIpPrefix(ipRange.getIpPrefix());
    existing.setIsActive(ipRange.getIsActive());
    existing.setDescription(ipRange.getDescription());
    return allowedIpRangeRepository.save(existing);
  }

  @Override
  public void delete(UUID id) {
    log.info("event=ALLOWED_IP_RANGE_PERSIST_DELETE id={}", id);
    allowedIpRangeRepository.deleteById(id);
  }
}

