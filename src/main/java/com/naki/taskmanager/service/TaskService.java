package com.naki.taskmanager.service;

import com.naki.taskmanager.dto.TaskDTO;
import com.naki.taskmanager.entity.Task;
import com.naki.taskmanager.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public ResponseEntity<Void> createTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.title());
        task.setDescription(taskDTO.description());
        task.setPriority(taskDTO.status());
        task.setDeadline(taskDTO.deadline());
        task.setPriority(taskDTO.priority());
        taskRepository.save(task);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<Void> deleteTask(Long taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Task> updateTask(Long taskId, TaskDTO updateTask) {
        return taskRepository.findById(taskId)
                .map(task -> {
                    task.setTitle(updateTask.title());
                    task.setDescription(updateTask.description());
                    task.setStatus(updateTask.status());
                    task.setDeadline(updateTask.deadline());
                    task.setPriority(updateTask.priority());
                    Task updatedTask = taskRepository.save(task);
                    return ResponseEntity.ok(updatedTask);
                })
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }
}
