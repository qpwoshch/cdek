package com.example.timetracker.service;

import com.example.timetracker.dto.CreateTimeRecordRequest;
import com.example.timetracker.dto.EmployeeTimeReportResponse;
import com.example.timetracker.dto.TimeRecordResponse;
import com.example.timetracker.entity.TimeRecord;
import com.example.timetracker.exception.BadRequestException;
import com.example.timetracker.mapper.TimeRecordMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TimeRecordService {
    private final TimeRecordMapper timeRecordMapper;
    private final TaskService taskService;

    public TimeRecordService(TimeRecordMapper timeRecordMapper, TaskService taskService) {
        this.timeRecordMapper = timeRecordMapper;
        this.taskService = taskService;
    }

    public TimeRecord createTimeRecord(CreateTimeRecordRequest request) {
        taskService.getTaskById(request.getTaskId());

        TimeRecord timeRecord = new TimeRecord();
        timeRecord.setEmployeeId(request.getEmployeeId());
        timeRecord.setTaskId(request.getTaskId());
        timeRecord.setStartTime(request.getStartTime());
        timeRecord.setEndTime(request.getEndTime());
        timeRecord.setWorkDescription(request.getWorkDescription());

        timeRecordMapper.insert(timeRecord);
        return timeRecord;
    }

    public EmployeeTimeReportResponse getEmployeeTimeReport(Long employeeId, LocalDateTime from, LocalDateTime to) {
        if (from.isAfter(to)) {
            throw new BadRequestException("from must be before to");
        }

        List<TimeRecord> records = timeRecordMapper.findByEmployeeAndPeriod(employeeId, from, to);
        List<TimeRecordResponse> responseRecords = records.stream().map(TimeRecordResponse::from).toList();

        long totalMinutes = records.stream()
                .mapToLong(record -> Duration.between(record.getStartTime(), record.getEndTime()).toMinutes())
                .sum();

        return new EmployeeTimeReportResponse(employeeId, from, to, totalMinutes, responseRecords);
    }
}
