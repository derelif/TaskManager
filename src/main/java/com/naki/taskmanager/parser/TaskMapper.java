package com.naki.taskmanager.parser;

import com.naki.taskmanager.dto.TaskDTO;
import com.naki.taskmanager.entity.Task;

import java.util.ArrayList;
import java.util.List;

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
}
