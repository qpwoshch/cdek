package com.example.timetracker.service;

import com.example.timetracker.dto.CreateTaskRequest;
import com.example.timetracker.entity.Task;
import com.example.timetracker.entity.TaskStatus;
import com.example.timetracker.exception.NotFoundException;
import com.example.timetracker.mapper.TaskMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTaskShouldSetNewStatusAndReturnTask() {
        when(taskMapper.insert(any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.setId(1L);
            return 1;
        });

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Test title");
        request.setDescription("Test description");

        Task createdTask = taskService.createTask(request);

        assertEquals(1L, createdTask.getId());
        assertEquals(TaskStatus.NEW, createdTask.getStatus());
        assertEquals("Test title", createdTask.getTitle());
        verify(taskMapper).insert(any(Task.class));
    }

    @Test
    void getTaskByIdShouldThrowWhenTaskNotFound() {
        when(taskMapper.findById(999L)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> taskService.getTaskById(999L));
    }

    @Test
    void updateTaskStatusShouldUpdateAndReturnTask() {
        Task existingTask = new Task(10L, "Task", "Desc", TaskStatus.NEW);
        when(taskMapper.findById(10L)).thenReturn(existingTask);

        Task updatedTask = taskService.updateTaskStatus(10L, TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, updatedTask.getStatus());
        verify(taskMapper).updateStatus(10L, TaskStatus.DONE);
    }
}
