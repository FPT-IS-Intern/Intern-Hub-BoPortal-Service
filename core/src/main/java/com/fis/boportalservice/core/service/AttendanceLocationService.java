package com.fis.boportalservice.core.service;

import com.fis.boportalservice.core.domain.model.AttendanceLocation;

import java.util.List;
import java.util.UUID;

public interface AttendanceLocationService {
  List<AttendanceLocation> getAll();

  List<AttendanceLocation> getAllActive();

  AttendanceLocation getById(UUID id);

  AttendanceLocation create(AttendanceLocation location);

  AttendanceLocation update(UUID id, AttendanceLocation location);

  void delete(UUID id);
}
