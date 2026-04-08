package com.fis.boportalservice.infra.persistence.repository;

import com.fis.boportalservice.infra.persistence.entity.PortalMenuRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

@Repository
public interface PortalMenuRoleJPARepository extends JpaRepository<PortalMenuRoleEntity, Long> {
  List<PortalMenuRoleEntity> findAllByMenuIdIn(Collection<Integer> menuIds);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("delete from PortalMenuRoleEntity r where r.menuId = :menuId")
  void deleteAllByMenuId(@Param("menuId") Integer menuId);
}
