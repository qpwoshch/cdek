package com.example.timetracker.controller;

import com.example.timetracker.dto.CreateTaskRequest;
import com.example.timetracker.dto.TaskResponse;
import com.example.timetracker.dto.UpdateTaskStatusRequest;
import com.example.timetracker.entity.Task;
import com.example.timetracker.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        Task createdTask = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskResponse.from(createdTask));
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return TaskResponse.from(taskService.getTaskById(id));
    }

    @PatchMapping("/{id}/status")
    public TaskResponse updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateTaskStatusRequest request) {
        Task updatedTask = taskService.updateTaskStatus(id, request.getStatus());
        return TaskResponse.from(updatedTask);
    }
}
