package com.naki.taskmanager.service;

import com.naki.taskmanager.dto.TaskDTO;
import com.naki.taskmanager.entity.Task;
import com.naki.taskmanager.exception.TaskNotFoundException;
import com.naki.taskmanager.mapper.TaskMapper;
import com.naki.taskmanager.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = new TaskMapper();
    }

    public List<TaskDTO> getTasks() {
        return taskMapper.mapTasks(taskRepository.findAll());
    }

    public ResponseEntity<TaskDTO> getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return ResponseEntity.ok(taskMapper.mapTaskDTO(task));
    }

    public ResponseEntity<TaskDTO> createTask(TaskDTO taskDTO) {
        Task task = taskMapper.mapTaskDTO(taskDTO);
        taskRepository.save(task);
        return new ResponseEntity<>(taskDTO, HttpStatus.CREATED);
    }

    public ResponseEntity<Void> deleteTask(Long taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<TaskDTO> updateTask(Long taskId, TaskDTO updateTask) {
        return taskRepository.findById(taskId)
                .map(task -> { taskMapper.mapTaskDTO(updateTask);
                    taskRepository.save(task);
                    return new ResponseEntity<>(updateTask, HttpStatus.OK);
                })
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }
}
