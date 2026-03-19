package com.fis.boportalservice.infra.persistence.repository;

import com.fis.boportalservice.infra.persistence.entity.PortalMenuRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PortalMenuRoleJPARepository extends JpaRepository<PortalMenuRoleEntity, Long> {
  List<PortalMenuRoleEntity> findAllByMenuIdIn(Collection<Integer> menuIds);

  void deleteAllByMenuId(Integer menuId);
}
