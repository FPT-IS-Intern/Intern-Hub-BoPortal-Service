package com.fis.boportalservice.infra.persistence.repository.adapter;

import com.fis.boportalservice.core.domain.model.HomepageBanner;
import com.fis.boportalservice.core.domain.repository.HomepageBannerRepository;
import com.fis.boportalservice.infra.persistence.entity.HomepageBannerEntity;
import com.fis.boportalservice.infra.persistence.mapper.HomepageBannerEntityMapper;
import com.fis.boportalservice.infra.persistence.repository.HomepageBannerJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HomepageBannerRepositoryAdapter implements HomepageBannerRepository {

    private final HomepageBannerJPARepository jpaRepository;
    private final HomepageBannerEntityMapper entityMapper;

    @Override
    public List<HomepageBanner> findActiveBanners() {
        return jpaRepository.findActiveBanners(LocalDateTime.now()).stream()
                .map(entityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<HomepageBanner> findAll() {
        return jpaRepository.findAll().stream()
                .map(entityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<HomepageBanner> findById(UUID id) {
        return jpaRepository.findById(id).map(entityMapper::toDomain);
    }

    @Override
    public HomepageBanner save(HomepageBanner banner) {
        HomepageBannerEntity entity = entityMapper.toEntity(banner);
        HomepageBannerEntity saved = jpaRepository.save(entity);
        return entityMapper.toDomain(saved);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
