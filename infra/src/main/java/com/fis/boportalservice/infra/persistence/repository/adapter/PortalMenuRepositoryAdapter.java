package com.fis.boportalservice.infra.persistence.repository.adapter;

import com.fis.boportalservice.core.domain.model.PortalMenu;
import com.fis.boportalservice.core.domain.repository.PortalMenuRepository;
import com.fis.boportalservice.infra.persistence.entity.PortalMenuEntity;
import com.fis.boportalservice.infra.persistence.entity.PortalMenuRoleEntity;
import com.fis.boportalservice.infra.persistence.mapper.PortalMenuEntityMapper;
import com.fis.boportalservice.infra.persistence.repository.PortalMenuJPARepository;
import com.fis.boportalservice.infra.persistence.repository.PortalMenuRoleJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PortalMenuRepositoryAdapter implements PortalMenuRepository {

  private final PortalMenuJPARepository jpaRepository;
  private final PortalMenuRoleJPARepository roleJPARepository;
  private final PortalMenuEntityMapper entityMapper;

  @Override
  public List<PortalMenu> findActiveMenus() {
    return toDomainList(jpaRepository.findAllActiveMenus());
  }

  @Override
  public List<PortalMenu> findActiveMenusByRoles(List<String> roleCodes) {
    return toDomainList(jpaRepository.findAllActiveMenus()).stream()
        .filter(menu -> menu.getRoleCodes() == null
            || menu.getRoleCodes().isEmpty()
            || roleCodes != null && roleCodes.stream().anyMatch(menu.getRoleCodes()::contains))
        .collect(Collectors.toList());
  }

  @Override
  public List<PortalMenu> findAll() {
    return toDomainList(jpaRepository.findAll());
  }

  @Override
  public Optional<PortalMenu> findById(Integer id) {
    return jpaRepository.findById(id).map(entity -> {
      PortalMenu menu = entityMapper.toDomain(entity);
      menu.setRoleCodes(findRoleCodesByMenuIds(List.of(entity.getId())).getOrDefault(entity.getId(), Collections.emptyList()));
      return menu;
    });
  }

  @Override
  @Transactional
  public PortalMenu save(PortalMenu menu) {
    PortalMenuEntity entity = entityMapper.toEntity(menu);
    PortalMenuEntity saved = jpaRepository.save(entity);
    roleJPARepository.deleteAllByMenuId(saved.getId());
    List<String> normalizedRoleCodes = normalizeRoleCodes(menu.getRoleCodes());
    if (!normalizedRoleCodes.isEmpty()) {
      List<PortalMenuRoleEntity> roleEntities = normalizedRoleCodes.stream()
          .map(roleCode -> PortalMenuRoleEntity.builder()
              .menuId(saved.getId())
              .roleCode(roleCode)
              .build())
          .collect(Collectors.toList());
      roleJPARepository.saveAll(roleEntities);
    }
    PortalMenu result = entityMapper.toDomain(saved);
    result.setRoleCodes(normalizedRoleCodes);
    return result;
  }

  @Override
  @Transactional
  public void deleteById(Integer id) {
    roleJPARepository.deleteAllByMenuId(id);
    jpaRepository.deleteById(id);
  }

  private List<PortalMenu> toDomainList(List<PortalMenuEntity> entities) {
    if (entities.isEmpty()) {
      return Collections.emptyList();
    }
    Map<Integer, List<String>> roleCodesByMenuId = findRoleCodesByMenuIds(
        entities.stream().map(PortalMenuEntity::getId).collect(Collectors.toList()));
    return entities.stream()
        .map(entity -> {
          PortalMenu menu = entityMapper.toDomain(entity);
          menu.setRoleCodes(roleCodesByMenuId.getOrDefault(entity.getId(), Collections.emptyList()));
          return menu;
        })
        .collect(Collectors.toList());
  }

  private Map<Integer, List<String>> findRoleCodesByMenuIds(List<Integer> menuIds) {
    if (menuIds.isEmpty()) {
      return Collections.emptyMap();
    }
    return roleJPARepository.findAllByMenuIdIn(menuIds).stream()
        .collect(Collectors.groupingBy(
            PortalMenuRoleEntity::getMenuId,
            Collectors.mapping(PortalMenuRoleEntity::getRoleCode, Collectors.collectingAndThen(Collectors.toList(), list ->
                list.stream().distinct().collect(Collectors.toList())))));
  }

  private List<String> normalizeRoleCodes(List<String> roleCodes) {
    if (roleCodes == null || roleCodes.isEmpty()) {
      return Collections.emptyList();
    }
    Set<String> unique = new LinkedHashSet<>();
    for (String code : roleCodes) {
      if (code == null) {
        continue;
      }
      String trimmed = code.trim();
      if (!trimmed.isEmpty()) {
        unique.add(trimmed);
      }
    }
    return List.copyOf(unique);
  }
}
