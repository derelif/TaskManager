package com.naki.taskmanager.controller;

import com.naki.taskmanager.dto.TaskDTO;
import com.naki.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public List<TaskDTO> getTasks(@RequestParam(required = false) String status) {
        return taskService.getTasks(status);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskDTO> createTask(@RequestBody @Valid TaskDTO taskDTO) {
        return taskService.createTask(taskDTO);
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        return taskService.deleteTask(id);
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody @Valid TaskDTO updateTask) {
        return taskService.updateTask(id, updateTask);
    }

    @PatchMapping("/tasks/{id}/status")
    public ResponseEntity<TaskDTO> patchTaskStatus(@PathVariable Long id, @RequestParam String status) {
        return taskService.patchTaskStatus(id, status);
    }

    @PatchMapping("/tasks/status")
    public ResponseEntity<List<TaskDTO>> patchTaskStatus(@RequestBody List<Long> ids, @RequestParam String status) {
        return taskService.patchTaskStatus(ids, status);
    }
}
