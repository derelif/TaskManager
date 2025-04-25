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
import java.util.List;
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
    void getTasksShouldReturnAllTasksWhenNoStatusProvided() {
        // given
        List<Task> tasks = List.of(new Task(), new Task());
        List<TaskDTO> taskDTOs = List.of(
                new TaskDTO("Title1", "Desc1", "pending", null, "1"),
                new TaskDTO("Title2", "Desc2", "done", null, "2")
        );

        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.mapTasks(tasks)).thenReturn(taskDTOs);

        // when
        List<TaskDTO> result = taskService.getTasks(null);

        // then
        assertEquals(taskDTOs, result);
        verify(taskRepository).findAll();
        verify(taskMapper).mapTasks(tasks);
    }

    @Test
    void getTasksShouldReturnFilteredTasksByStatus() {
        // given
        String status = "pending";
        List<Task> tasks = List.of(new Task());
        List<TaskDTO> taskDTOs = List.of(new TaskDTO("Title", "Desc", status, null, "1"));

        when(taskRepository.findByStatus(status)).thenReturn(tasks);
        when(taskMapper.mapTasks(tasks)).thenReturn(taskDTOs);

        // when
        List<TaskDTO> result = taskService.getTasks(status);

        // then
        assertEquals(taskDTOs, result);
        verify(taskRepository).findByStatus(status);
        verify(taskMapper).mapTasks(tasks);
    }

    @Test
    void deleteTaskShouldReturnOkWhenTaskExists() {
        Long taskId = 1L;

        when(taskRepository.existsById(taskId)).thenReturn(true);

        ResponseEntity<Void> response = taskService.deleteTask(taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void deleteTaskShouldThrowExceptionWhenTaskNotFound() {
        Long taskId = 42L;

        when(taskRepository.existsById(taskId)).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(taskId));
    }




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
