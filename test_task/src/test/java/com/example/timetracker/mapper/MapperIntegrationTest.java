package com.example.timetracker.mapper;

import com.example.timetracker.entity.Task;
import com.example.timetracker.entity.TaskStatus;
import com.example.timetracker.entity.TimeRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MybatisTest
@Testcontainers(disabledWithoutDocker = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MapperIntegrationTest {

    static {
        // On some Windows setups this property may include broken quoted path.
        // Explicit value avoids InvalidPathException inside DockerMachine strategy.
        System.setProperty("docker.machine.path", "docker-machine");
    }

    @Container
    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("timetracker")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRESQL_CONTAINER::getDriverClassName);
        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TimeRecordMapper timeRecordMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanTables() {
        jdbcTemplate.update("DELETE FROM time_records");
        jdbcTemplate.update("DELETE FROM tasks");
    }

    @Test
    void shouldInsertAndReadTaskAndTimeRecord() {
        Task task = new Task(null, "New task", "Description", TaskStatus.NEW);
        taskMapper.insert(task);
        assertNotNull(task.getId());

        TimeRecord timeRecord = new TimeRecord(
                null,
                200L,
                task.getId(),
                LocalDateTime.parse("2026-04-01T10:00:00"),
                LocalDateTime.parse("2026-04-01T11:15:00"),
                "Implemented endpoint"
        );
        timeRecordMapper.insert(timeRecord);
        assertNotNull(timeRecord.getId());

        List<TimeRecord> records = timeRecordMapper.findByEmployeeAndPeriod(
                200L,
                LocalDateTime.parse("2026-04-01T00:00:00"),
                LocalDateTime.parse("2026-04-01T23:59:59")
        );

        assertEquals(1, records.size());
        assertEquals(task.getId(), records.get(0).getTaskId());
        assertEquals("Implemented endpoint", records.get(0).getWorkDescription());
    }
}
