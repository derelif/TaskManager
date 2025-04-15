package com.naki.taskmanager.service;

import com.naki.taskmanager.dto.TaskDTO;
import com.naki.taskmanager.entity.Task;
import com.naki.taskmanager.exception.TaskNotFoundException;
import com.naki.taskmanager.mapper.TaskMapper;
import com.naki.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;



    @Test
    void createTaskShouldMapAndSaveAndReturnDTO() {
        // given
        TaskDTO taskDTO = new TaskDTO(
                "Test Task",
                "descr",
                "pending",
                LocalDate.of(2025, 4, 15),
                "2");

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setStatus("pending");
        task.setDescription("descr");
        task.setDeadline(LocalDate.of(2025, 4, 15));
        task.setPriority("2");

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("Test Task");
        savedTask.setStatus("pending");
        savedTask.setDescription("descr");
        savedTask.setDeadline(LocalDate.of(2025, 4, 15));
        savedTask.setPriority("2");

        TaskDTO returnedDTO = new TaskDTO(
                "Test Task",
                "descr",
                "pending",
                LocalDate.of(2025, 4, 15),
                "2");

        when(taskMapper.mapTaskDTO(taskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(savedTask);
        when(taskMapper.mapTaskDTO(savedTask)).thenReturn(returnedDTO);

        ResponseEntity<TaskDTO> response = taskService.createTask(taskDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(returnedDTO, response.getBody());

        verify(taskMapper).mapTaskDTO(taskDTO);
        verify(taskRepository).save(task);
        verify(taskMapper).mapTaskDTO(savedTask);
    }

    @Test
    public void patchTaskStatusShouldUpdateAndReturnDTO() {
        Long id = 1L;
        String newStatus = "done";
        Task mockTask = new Task();
        mockTask.setId(id);
        mockTask.setStatus("in_progress");

        TaskDTO mockTaskDTO = new TaskDTO("Title", "Desc", newStatus, null, null);

        when(taskRepository.findById(id)).thenReturn(Optional.of(mockTask));
        when(taskMapper.mapTaskDTO(mockTask)).thenReturn(mockTaskDTO);
        var response = taskService.patchTaskStatus(id, newStatus);
        assertEquals(200, response.getStatusCode().value());
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
