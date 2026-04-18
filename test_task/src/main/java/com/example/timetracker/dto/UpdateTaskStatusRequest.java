package com.example.timetracker.dto;

import com.example.timetracker.entity.TaskStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateTaskStatusRequest {
    @NotNull(message = "Status is required")
    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
