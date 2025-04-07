package com.naki.taskmanager.dto;

import java.time.LocalDate;

public record TaskDTO(String title, String description, String status, LocalDate deadline, String priority) {
}
