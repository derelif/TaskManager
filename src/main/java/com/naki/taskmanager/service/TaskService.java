package com.naki.taskmanager.service;

import com.naki.taskmanager.dto.TaskDTO;
import com.naki.taskmanager.entity.Task;
import com.naki.taskmanager.exception.TaskNotFoundException;
import com.naki.taskmanager.mapper.TaskMapper;
import com.naki.taskmanager.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public List<TaskDTO> getTasks(@RequestParam(required = false) String status) {

        if (status != null) {
            return taskMapper.mapTasks(taskRepository.findByStatus(status.toLowerCase()));
        }
        return taskMapper.mapTasks(taskRepository.findAll());
    }

    public ResponseEntity<TaskDTO> getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return ResponseEntity.ok(taskMapper.mapTaskDTO(task));
    }

    public ResponseEntity<TaskDTO> createTask(TaskDTO taskDTO) {
        Task task = taskMapper.mapTaskDTO(taskDTO);
        Task savedTask = taskRepository.save(task);
        TaskDTO newTaskDTO = taskMapper.mapTaskDTO(savedTask);
        return new ResponseEntity<>(newTaskDTO, HttpStatus.CREATED);
    }

    public ResponseEntity<Void> deleteTask(Long taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            throw new TaskNotFoundException(taskId);

    }

    public ResponseEntity<TaskDTO> updateTask(Long taskId, TaskDTO updateTask) {
        return taskRepository.findById(taskId)
                .map(task -> { TaskMapper.updateTask(task, updateTask);
                    taskRepository.save(task);
                    return new ResponseEntity<>(updateTask, HttpStatus.OK);
                })
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    public ResponseEntity<TaskDTO> patchTaskStatus(Long id, String status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.setStatus(status.toLowerCase());
        taskRepository.save(task);
        TaskDTO newTaskDTO = taskMapper.mapTaskDTO(task);
        return new ResponseEntity<>(newTaskDTO, HttpStatus.OK);
    }

    public ResponseEntity<List<TaskDTO>> patchTaskStatus(List<Long> ids, String status) {
        List<TaskDTO> updateTasks = ids.stream()
                .map(id -> taskRepository.findById(id)
                        .map(task -> {
                            task.setStatus(status.toLowerCase());
                            taskRepository.save(task);
                            return taskMapper.mapTaskDTO(task);
                        })
                        .orElseThrow(() -> new TaskNotFoundException(id))
                ).toList();
        return new ResponseEntity<>(updateTasks, HttpStatus.OK);
    }
}
