package com.fis.boportalservice.infra.persistence.repository;

import com.fis.boportalservice.infra.persistence.entity.AttendanceLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import com.fis.boportalservice.core.domain.model.AttendanceLocation;
import com.fis.boportalservice.core.domain.repository.AttendanceLocationRepository;
import com.fis.boportalservice.infra.persistence.entity.AttendanceLocationEntity;
import com.fis.boportalservice.infra.persistence.repository.AttendanceLocationJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AttendanceLocationRepositoryAdapter implements AttendanceLocationRepository {

    private final AttendanceLocationJPARepository jpaRepository;

    @Override
    public List<AttendanceLocation> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceLocation> findAllActive() {
        return jpaRepository.findByIsActiveTrue().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AttendanceLocation> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public AttendanceLocation save(AttendanceLocation location) {
        AttendanceLocationEntity entity = toEntity(location);
        AttendanceLocationEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    private AttendanceLocation toDomain(AttendanceLocationEntity entity) {
        return AttendanceLocation.builder()
                .id(entity.getId())
                .name(entity.getName())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .radiusMeters(entity.getRadiusMeters())
                .isActive(entity.getIsActive())
                .build();
    }

    private AttendanceLocationEntity toEntity(AttendanceLocation domain) {
        return AttendanceLocationEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .latitude(domain.getLatitude())
                .longitude(domain.getLongitude())
                .radiusMeters(domain.getRadiusMeters())
                .isActive(domain.getIsActive())
                .build();
    }
}
