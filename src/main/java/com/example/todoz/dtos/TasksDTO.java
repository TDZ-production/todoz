package com.example.todoz.dtos;

import java.util.List;

public record TasksDTO(List<TaskDTO> delegatedTasks) {
}
