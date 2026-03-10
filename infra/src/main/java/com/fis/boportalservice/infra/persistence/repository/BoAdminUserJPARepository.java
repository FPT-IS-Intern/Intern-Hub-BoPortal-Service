package com.fis.boportalservice.infra.persistence.repository;

import com.fis.boportalservice.infra.persistence.entity.BoAdminUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BoAdminUserJPARepository extends JpaRepository<BoAdminUserEntity, UUID> {
  Optional<BoAdminUserEntity> findByUsernameIgnoreCase(String username);

  @Query(value = """
      SELECT DISTINCT r.code
      FROM ih_bo_portal.bo_user_role ur
      JOIN ih_bo_portal.bo_role r ON r.id = ur.role_id
      WHERE ur.user_id = :userId
        AND r.status = 'ACTIVE'
      ORDER BY r.code
      """, nativeQuery = true)
  List<String> findRoleCodesByUserId(@Param("userId") UUID userId);

  @Query(value = """
      SELECT DISTINCT p.code
      FROM ih_bo_portal.bo_user_role ur
      JOIN ih_bo_portal.bo_role r ON r.id = ur.role_id
      JOIN ih_bo_portal.bo_role_permission rp ON rp.role_id = r.id
      JOIN ih_bo_portal.bo_permission p ON p.id = rp.permission_id
      WHERE ur.user_id = :userId
        AND r.status = 'ACTIVE'
        AND p.status = 'ACTIVE'
      ORDER BY p.code
      """, nativeQuery = true)
  List<String> findPermissionCodesByUserId(@Param("userId") UUID userId);
}
