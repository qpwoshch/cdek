package com.example.timetracker.controller;

import com.example.timetracker.dto.CreateTimeRecordRequest;
import com.example.timetracker.dto.EmployeeTimeReportResponse;
import com.example.timetracker.dto.TimeRecordResponse;
import com.example.timetracker.entity.TimeRecord;
import com.example.timetracker.service.TimeRecordService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Validated
@RestController
@RequestMapping("/api/time-records")
public class TimeRecordController {
    private final TimeRecordService timeRecordService;

    public TimeRecordController(TimeRecordService timeRecordService) {
        this.timeRecordService = timeRecordService;
    }

    @PostMapping
    public ResponseEntity<TimeRecordResponse> createTimeRecord(@Valid @RequestBody CreateTimeRecordRequest request) {
        TimeRecord createdRecord = timeRecordService.createTimeRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(TimeRecordResponse.from(createdRecord));
    }

    @GetMapping
    public EmployeeTimeReportResponse getEmployeeTimeReport(
            @RequestParam @Positive Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return timeRecordService.getEmployeeTimeReport(employeeId, from, to);
    }
}
