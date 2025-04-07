package com.naki.taskmanager.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record TaskDTO(
        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title can't be longer than 100 characters")
        String title,

        @Size(max = 500, message = "Description is too long")
        String description,

        String status,

        @Future(message = "Deadline must be in the future")
        LocalDate deadline,

        @Min(1)
        @Max(5)
        String priority) {}