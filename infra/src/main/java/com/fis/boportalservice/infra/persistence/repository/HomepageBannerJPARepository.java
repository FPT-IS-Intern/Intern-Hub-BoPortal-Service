package com.fis.boportalservice.infra.persistence.repository;

import com.fis.boportalservice.infra.persistence.entity.HomepageBannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface HomepageBannerJPARepository extends JpaRepository<HomepageBannerEntity, UUID> {

    @Query("SELECT b FROM HomepageBannerEntity b " +
            "WHERE b.isActive = true " +
            "AND (b.startDate IS NULL OR b.startDate <= :now) " +
            "AND (b.endDate IS NULL OR b.endDate >= :now) " +
            "ORDER BY b.displayOrder ASC")
    List<HomepageBannerEntity> findActiveBanners(@Param("now") LocalDateTime now);
}
