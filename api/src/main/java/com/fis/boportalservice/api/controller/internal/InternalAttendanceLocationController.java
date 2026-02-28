package com.fis.boportalservice.api.controller.internal;

import com.fis.boportalservice.api.dto.response.AttendanceLocationResponse;
import com.fis.boportalservice.api.mapper.AttendanceLocationApiMapper;
import com.fis.boportalservice.common.dto.ResponseApi;
import com.fis.boportalservice.core.service.AttendanceLocationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "Internal - Attendance Locations")
@RestController
@RequestMapping("/internal/attendance-locations")
@RequiredArgsConstructor
public class InternalAttendanceLocationController {

    private final AttendanceLocationService attendanceLocationService;
    private final AttendanceLocationApiMapper apiMapper;

    @GetMapping
    public ResponseApi<List<AttendanceLocationResponse>> getActiveLocations() {
        log.info("Internal request to get all active attendance locations");
        List<AttendanceLocationResponse> responses = attendanceLocationService.getAllActive()
                .stream()
                .map(apiMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseApi.success(responses);
    }
}
