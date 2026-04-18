package com.example.timetracker.dto;

import java.time.LocalDateTime;
import java.util.List;

public record EmployeeTimeReportResponse(
        Long employeeId,
        LocalDateTime from,
        LocalDateTime to,
        Long totalMinutes,
        List<TimeRecordResponse> records
) {
}
