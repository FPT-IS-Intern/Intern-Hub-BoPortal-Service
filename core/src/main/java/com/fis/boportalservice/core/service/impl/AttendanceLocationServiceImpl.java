package com.fis.boportalservice.core.service.impl;

import com.fis.boportalservice.core.domain.model.AttendanceLocation;
import com.fis.boportalservice.core.domain.repository.AttendanceLocationRepository;
import com.fis.boportalservice.core.service.AttendanceLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceLocationServiceImpl implements AttendanceLocationService {

    private final AttendanceLocationRepository attendanceLocationRepository;

    @Override
    public List<AttendanceLocation> getAll() {
        return attendanceLocationRepository.findAll();
    }

    @Override
    public List<AttendanceLocation> getAllActive() {
        return attendanceLocationRepository.findAllActive();
    }

    @Override
    public AttendanceLocation getById(UUID id) {
        return attendanceLocationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance location not found with id: " + id));
    }

    @Override
    public AttendanceLocation create(AttendanceLocation location) {
        log.info("Creating attendance location: {}", location.getName());
        return attendanceLocationRepository.save(location);
    }

    @Override
    public AttendanceLocation update(UUID id, AttendanceLocation location) {
        log.info("Updating attendance location with id: {}", id);
        AttendanceLocation existing = getById(id);
        existing.setName(location.getName());
        existing.setLatitude(location.getLatitude());
        existing.setLongitude(location.getLongitude());
        existing.setRadiusMeters(location.getRadiusMeters());
        existing.setIsActive(location.getIsActive());
        return attendanceLocationRepository.save(existing);
    }

    @Override
    public void delete(UUID id) {
        log.info("Deleting attendance location with id: {}", id);
        attendanceLocationRepository.deleteById(id);
    }
}
