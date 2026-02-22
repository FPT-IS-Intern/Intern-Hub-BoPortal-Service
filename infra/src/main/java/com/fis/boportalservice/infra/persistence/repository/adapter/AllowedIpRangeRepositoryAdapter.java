package com.fis.boportalservice.infra.persistence.repository.adapter;

import com.fis.boportalservice.core.domain.model.AllowedIpRange;
import com.fis.boportalservice.core.domain.repository.AllowedIpRangeRepository;
import com.fis.boportalservice.infra.persistence.entity.AllowedIpRangeEntity;
import com.fis.boportalservice.infra.persistence.mapper.AllowedIpRangeEntityMapper;
import com.fis.boportalservice.infra.persistence.repository.AllowedIpRangeJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AllowedIpRangeRepositoryAdapter implements AllowedIpRangeRepository {

    private final AllowedIpRangeJPARepository jpaRepository;
    private final AllowedIpRangeEntityMapper entityMapper;

    @Override
    public List<AllowedIpRange> findAllActive() {
        return jpaRepository.findByIsActiveTrue().stream()
                .map(entityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AllowedIpRange> findAll() {
        return jpaRepository.findAll().stream()
                .map(entityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AllowedIpRange> findById(UUID id) {
        return jpaRepository.findById(id).map(entityMapper::toDomain);
    }

    @Override
    public AllowedIpRange save(AllowedIpRange ipRange) {
        AllowedIpRangeEntity entity = entityMapper.toEntity(ipRange);
        AllowedIpRangeEntity saved = jpaRepository.save(entity);
        return entityMapper.toDomain(saved);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
