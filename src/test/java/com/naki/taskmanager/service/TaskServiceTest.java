package com.naki.taskmanager.service;

import com.naki.taskmanager.dto.TaskDTO;
import com.naki.taskmanager.entity.Task;
import com.naki.taskmanager.exception.TaskNotFoundException;
import com.naki.taskmanager.mapper.TaskMapper;
import com.naki.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.util.Assert;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    public TaskServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void patchTaskStatusShouldUpdateAndReturnDTO() {
        Long id = 1L;
        String newStatus = "done";
        Task mockTask = new Task();
        mockTask.setId(id);
        mockTask.setStatus(newStatus);

        TaskDTO mockTaskDTO = new TaskDTO("Title", "Desc", newStatus, null, null);

        when(taskRepository.findById(id)).thenReturn(Optional.of(mockTask));
        when(taskMapper.mapTaskDTO(mockTask)).thenReturn(mockTaskDTO);
        var response = taskService.patchTaskStatus(id, newStatus);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockTaskDTO.status(), response.getBody().status());

        verify(taskRepository).save(mockTask);
    }

    @Test
    public void patchTaskStatusShouldThrowExceptionWhenTaskNotFound() {
        Long invalidId = 99L;
        when(taskRepository.findById(invalidId)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, () -> taskService.patchTaskStatus(invalidId, "done"));
    }
}
