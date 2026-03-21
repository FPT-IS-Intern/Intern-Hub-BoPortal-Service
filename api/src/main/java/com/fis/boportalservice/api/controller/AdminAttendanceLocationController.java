package com.fis.boportalservice.api.controller;

import com.fis.boportalservice.api.dto.request.AttendanceLocationRequest;
import com.fis.boportalservice.api.dto.response.AttendanceLocationResponse;
import com.fis.boportalservice.api.mapper.AttendanceLocationApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.domain.model.AttendanceLocation;
import com.fis.boportalservice.core.service.AttendanceLocationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "2. Admin - Attendance Locations")
@RestController
@RequestMapping("/bo-portal/attendance-locations")
@RequiredArgsConstructor
public class AdminAttendanceLocationController {

  private final AttendanceLocationService attendanceLocationService;
  private final AttendanceLocationApiMapper apiMapper;

  @GetMapping
  public ResponseApi<List<AttendanceLocationResponse>> getAll() {
    log.info("event=ATTENDANCE_LOCATION_LIST_REQUEST");
    List<AttendanceLocationResponse> responses = attendanceLocationService.getAll().stream()
        .map(apiMapper::toResponse)
        .collect(Collectors.toList());
    return ResponseApi.success(responses);
  }

  @GetMapping("/{id}")
  public ResponseApi<AttendanceLocationResponse> getById(@PathVariable UUID id) {
    log.info("event=ATTENDANCE_LOCATION_DETAIL_REQUEST id={}", id);
    return ResponseApi.success(apiMapper.toResponse(attendanceLocationService.getById(id)));
  }

  @PostMapping
  public ResponseApi<AttendanceLocationResponse> create(@RequestBody AttendanceLocationRequest request) {
    log.info("event=ATTENDANCE_LOCATION_CREATE_REQUEST name={}", request.getName());
    AttendanceLocation domain = apiMapper.toDomain(request);
    AttendanceLocation saved = attendanceLocationService.create(domain);
    return ResponseApi.success(apiMapper.toResponse(saved));
  }

  @PutMapping("/{id}")
  public ResponseApi<AttendanceLocationResponse> update(@PathVariable UUID id,
                                                        @RequestBody AttendanceLocationRequest request) {
    log.info("event=ATTENDANCE_LOCATION_UPDATE_REQUEST id={} name={}", id, request.getName());
    AttendanceLocation domain = apiMapper.toDomain(request);
    AttendanceLocation updated = attendanceLocationService.update(id, domain);
    return ResponseApi.success(apiMapper.toResponse(updated));
  }

  @DeleteMapping("/{id}")
  public ResponseApi<Void> delete(@PathVariable UUID id) {
    log.info("event=ATTENDANCE_LOCATION_DELETE_REQUEST id={}", id);
    attendanceLocationService.delete(id);
    return ResponseApi.success(null);
  }
}

