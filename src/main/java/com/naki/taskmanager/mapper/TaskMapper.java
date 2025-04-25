package com.naki.taskmanager.mapper;

import com.naki.taskmanager.dto.TaskDTO;
import com.naki.taskmanager.entity.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMapper {

    public List<TaskDTO> mapTasks(List<Task> tasks) {
        List<TaskDTO> taskDTOs = new ArrayList<>();
        for (Task task : tasks) {
            taskDTOs.add(new TaskDTO(
                    task.getTitle(),
                    task.getDescription(),
                    task.getStatus(),
                    task.getDeadline(),
                    task.getPriority()));
        }
        return taskDTOs;
    }

    public Task mapTaskDTO(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.title());
        task.setDescription(taskDTO.description());
        task.setStatus(taskDTO.status());
        task.setDeadline(taskDTO.deadline());
        task.setPriority(taskDTO.priority());
        return task;
    }

    public TaskDTO mapTaskDTO(Task task) {
        return new TaskDTO(
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDeadline(),
                task.getPriority());
    }

    public static void updateTask(Task task, TaskDTO taskDTO) {
        task.setTitle(taskDTO.title());
        task.setDescription(taskDTO.description());
        task.setDeadline(taskDTO.deadline());
        task.setStatus(taskDTO.status());
        task.setPriority(taskDTO.priority());
    }
}
