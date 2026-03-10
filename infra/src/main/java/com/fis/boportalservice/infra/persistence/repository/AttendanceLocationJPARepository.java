package com.fis.boportalservice.infra.persistence.repository;

import com.fis.boportalservice.infra.persistence.entity.AttendanceLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AttendanceLocationJPARepository extends JpaRepository<AttendanceLocationEntity, UUID> {
  List<AttendanceLocationEntity> findByIsActiveTrue();
}
