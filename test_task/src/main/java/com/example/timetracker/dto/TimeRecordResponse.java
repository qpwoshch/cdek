package com.example.timetracker.dto;

import com.example.timetracker.entity.TimeRecord;

import java.time.Duration;
import java.time.LocalDateTime;

public record TimeRecordResponse(
        Long id,
        Long employeeId,
        Long taskId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String workDescription,
        Long durationMinutes
) {
    public static TimeRecordResponse from(TimeRecord timeRecord) {
        long durationMinutes = Duration.between(timeRecord.getStartTime(), timeRecord.getEndTime()).toMinutes();
        return new TimeRecordResponse(
                timeRecord.getId(),
                timeRecord.getEmployeeId(),
                timeRecord.getTaskId(),
                timeRecord.getStartTime(),
                timeRecord.getEndTime(),
                timeRecord.getWorkDescription(),
                durationMinutes
        );
    }
}
