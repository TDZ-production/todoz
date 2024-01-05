package com.example.todoz.dtos;

import java.time.LocalDateTime;

public record TaskUpdateDTO(Long id, String description, Integer priority, LocalDateTime dueDate) {
}
