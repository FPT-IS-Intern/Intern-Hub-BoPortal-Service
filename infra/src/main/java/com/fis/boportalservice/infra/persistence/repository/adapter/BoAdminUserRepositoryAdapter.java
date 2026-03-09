package com.fis.boportalservice.infra.persistence.repository.adapter;

import com.fis.boportalservice.core.domain.model.BoAdminUser;
import com.fis.boportalservice.core.domain.repository.BoAdminUserRepository;
import com.fis.boportalservice.infra.persistence.entity.BoAdminUserEntity;
import com.fis.boportalservice.infra.persistence.mapper.BoAdminUserEntityMapper;
import com.fis.boportalservice.infra.persistence.repository.BoAdminUserJPARepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoAdminUserRepositoryAdapter implements BoAdminUserRepository {

    private final BoAdminUserJPARepository jpaRepository;
    private final BoAdminUserEntityMapper entityMapper;

    @Override
    public Optional<BoAdminUser> findByUsername(String username) {
        return jpaRepository.findByUsernameIgnoreCase(username).map(entityMapper::toDomain);
    }

    @Override
    public Optional<BoAdminUser> findById(UUID id) {
        return jpaRepository.findById(id).map(entityMapper::toDomain);
    }

    @Override
    public List<String> findRoleCodes(UUID userId) {
        return jpaRepository.findRoleCodesByUserId(userId);
    }

    @Override
    public List<String> findPermissionCodes(UUID userId) {
        return jpaRepository.findPermissionCodesByUserId(userId);
    }

    @Override
    public BoAdminUser save(BoAdminUser user) {
        BoAdminUserEntity saved = jpaRepository.save(entityMapper.toEntity(user));
        return entityMapper.toDomain(saved);
    }
}
