package com.example.timetracker.service;

import com.example.timetracker.dto.CreateTimeRecordRequest;
import com.example.timetracker.dto.EmployeeTimeReportResponse;
import com.example.timetracker.entity.Task;
import com.example.timetracker.entity.TaskStatus;
import com.example.timetracker.entity.TimeRecord;
import com.example.timetracker.exception.BadRequestException;
import com.example.timetracker.mapper.TimeRecordMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeRecordServiceTest {

    @Mock
    private TimeRecordMapper timeRecordMapper;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TimeRecordService timeRecordService;

    @Test
    void createTimeRecordShouldInsertAndReturnRecord() {
        when(taskService.getTaskById(1L)).thenReturn(new Task(1L, "Task", "Desc", TaskStatus.NEW));
        when(timeRecordMapper.insert(any(TimeRecord.class))).thenAnswer(invocation -> {
            TimeRecord record = invocation.getArgument(0);
            record.setId(5L);
            return 1;
        });

        CreateTimeRecordRequest request = new CreateTimeRecordRequest();
        request.setEmployeeId(100L);
        request.setTaskId(1L);
        request.setStartTime(LocalDateTime.parse("2026-04-01T10:00:00"));
        request.setEndTime(LocalDateTime.parse("2026-04-01T11:30:00"));
        request.setWorkDescription("Backend work");

        TimeRecord createdRecord = timeRecordService.createTimeRecord(request);

        assertEquals(5L, createdRecord.getId());
        assertEquals(100L, createdRecord.getEmployeeId());
        verify(taskService).getTaskById(1L);
        verify(timeRecordMapper).insert(any(TimeRecord.class));
    }

    @Test
    void getEmployeeTimeReportShouldReturnCorrectTotalMinutes() {
        LocalDateTime from = LocalDateTime.parse("2026-04-01T00:00:00");
        LocalDateTime to = LocalDateTime.parse("2026-04-01T23:59:59");
        List<TimeRecord> records = List.of(
                new TimeRecord(1L, 100L, 1L, LocalDateTime.parse("2026-04-01T10:00:00"), LocalDateTime.parse("2026-04-01T11:00:00"), "Task 1"),
                new TimeRecord(2L, 100L, 2L, LocalDateTime.parse("2026-04-01T12:00:00"), LocalDateTime.parse("2026-04-01T12:30:00"), "Task 2")
        );
        when(timeRecordMapper.findByEmployeeAndPeriod(100L, from, to)).thenReturn(records);

        EmployeeTimeReportResponse report = timeRecordService.getEmployeeTimeReport(100L, from, to);

        assertEquals(90L, report.totalMinutes());
        assertEquals(2, report.records().size());
    }

    @Test
    void getEmployeeTimeReportShouldThrowWhenPeriodInvalid() {
        LocalDateTime from = LocalDateTime.parse("2026-04-02T00:00:00");
        LocalDateTime to = LocalDateTime.parse("2026-04-01T00:00:00");
        assertThrows(BadRequestException.class, () -> timeRecordService.getEmployeeTimeReport(1L, from, to));
    }
}
