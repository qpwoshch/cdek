package com.example.timetracker.mapper;

import com.example.timetracker.entity.TimeRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TimeRecordMapper {

    @Insert("""
            INSERT INTO time_records (employee_id, task_id, start_time, end_time, work_description)
            VALUES (#{employeeId}, #{taskId}, #{startTime}, #{endTime}, #{workDescription})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(TimeRecord timeRecord);

    @Select("""
            SELECT id, employee_id, task_id, start_time, end_time, work_description
            FROM time_records
            WHERE employee_id = #{employeeId}
              AND start_time >= #{from}
              AND end_time <= #{to}
            ORDER BY start_time
            """)
    List<TimeRecord> findByEmployeeAndPeriod(
            @Param("employeeId") Long employeeId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
}
