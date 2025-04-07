package com.naki.taskmanager.service;

import com.naki.taskmanager.entity.Task;
import com.naki.taskmanager.repository.TaskRepository;
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

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public ResponseEntity<Void> deleteTask(Long taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Task> updateTask(Long taskId, Task updateTask) {
        return taskRepository.findById(taskId)
                .map(task -> {
                    task.setTitle(updateTask.getTitle());
                    task.setDescription(updateTask.getDescription());
                    task.setStatus(updateTask.getStatus());
                    task.setDeadline(updateTask.getDeadline());
                    task.setPriority(updateTask.getPriority());
                    Task updatedTask = taskRepository.save(task);
                    return ResponseEntity.ok(updatedTask);
                })
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }
}
