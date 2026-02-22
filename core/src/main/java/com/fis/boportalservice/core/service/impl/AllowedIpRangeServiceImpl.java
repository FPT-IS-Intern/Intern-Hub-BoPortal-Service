package com.fis.boportalservice.core.service.impl;

import com.fis.boportalservice.core.domain.model.AllowedIpRange;
import com.fis.boportalservice.core.domain.repository.AllowedIpRangeRepository;
import com.fis.boportalservice.core.service.AllowedIpRangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
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
                .orElseThrow(() -> new RuntimeException("AllowedIpRange not found with id: " + id));
    }

    @Override
    public AllowedIpRange create(AllowedIpRange ipRange) {
        return allowedIpRangeRepository.save(ipRange);
    }

    @Override
    public AllowedIpRange update(UUID id, AllowedIpRange ipRange) {
        AllowedIpRange existing = getById(id);
        existing.setName(ipRange.getName());
        existing.setIpPrefix(ipRange.getIpPrefix());
        existing.setIsActive(ipRange.getIsActive());
        existing.setDescription(ipRange.getDescription());
        return allowedIpRangeRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        allowedIpRangeRepository.deleteById(id);
    }
}
