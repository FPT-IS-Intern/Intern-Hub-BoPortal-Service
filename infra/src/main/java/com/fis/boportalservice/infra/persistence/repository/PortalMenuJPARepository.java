package com.fis.boportalservice.infra.persistence.repository;

import com.fis.boportalservice.infra.persistence.entity.PortalMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortalMenuJPARepository extends JpaRepository<PortalMenuEntity, Integer> {

    @Query("SELECT m FROM PortalMenuEntity m WHERE m.status = 'ACTIVE' ORDER BY m.sortOrder ASC")
    List<PortalMenuEntity> findAllActiveMenus();
}
