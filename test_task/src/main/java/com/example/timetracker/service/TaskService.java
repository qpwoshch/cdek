package com.example.timetracker.service;

import com.example.timetracker.dto.CreateTaskRequest;
import com.example.timetracker.entity.Task;
import com.example.timetracker.entity.TaskStatus;
import com.example.timetracker.exception.NotFoundException;
import com.example.timetracker.mapper.TaskMapper;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskMapper taskMapper;

    public TaskService(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    public Task createTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.NEW);
        taskMapper.insert(task);
        return task;
    }

    public Task getTaskById(Long id) {
        Task task = taskMapper.findById(id);
        if (task == null) {
            throw new NotFoundException("Task with id=" + id + " not found");
        }
        return task;
    }

    public Task updateTaskStatus(Long id, TaskStatus status) {
        Task task = getTaskById(id);
        taskMapper.updateStatus(id, status);
        task.setStatus(status);
        return task;
    }
}
