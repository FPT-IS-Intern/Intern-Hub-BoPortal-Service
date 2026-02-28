package com.fis.boportalservice.core.domain.repository;

import com.fis.boportalservice.core.domain.model.AttendanceLocation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceLocationRepository {
    List<AttendanceLocation> findAll();

    List<AttendanceLocation> findAllActive();

    Optional<AttendanceLocation> findById(UUID id);

    AttendanceLocation save(AttendanceLocation location);

    void deleteById(UUID id);
}
