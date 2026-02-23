package com.fis.boportalservice.infra.persistence.repository.adapter;

import com.fis.boportalservice.core.domain.model.PortalMenu;
import com.fis.boportalservice.core.domain.repository.PortalMenuRepository;
import com.fis.boportalservice.infra.persistence.entity.PortalMenuEntity;
import com.fis.boportalservice.infra.persistence.mapper.PortalMenuEntityMapper;
import com.fis.boportalservice.infra.persistence.repository.PortalMenuJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PortalMenuRepositoryAdapter implements PortalMenuRepository {

    private final PortalMenuJPARepository jpaRepository;
    private final PortalMenuEntityMapper entityMapper;

    @Override
    public List<PortalMenu> findActiveMenus() {
        return jpaRepository.findAllActiveMenus().stream()
                .map(entityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PortalMenu> findAll() {
        return jpaRepository.findAll().stream()
                .map(entityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PortalMenu> findById(Integer id) {
        return jpaRepository.findById(id).map(entityMapper::toDomain);
    }

    @Override
    public PortalMenu save(PortalMenu menu) {
        PortalMenuEntity entity = entityMapper.toEntity(menu);
        PortalMenuEntity saved = jpaRepository.save(entity);
        return entityMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }
}
