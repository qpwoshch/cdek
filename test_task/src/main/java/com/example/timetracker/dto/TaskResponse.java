package com.example.timetracker.dto;

import com.example.timetracker.entity.Task;
import com.example.timetracker.entity.TaskStatus;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status
) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus());
    }
}
