package com.example.todoz.dtos;

import java.time.LocalDate;

/**
 * used only for editing a task, not to e.g. mark it as (not) done.
 */
public record TaskUpdateDTO(String description, Integer priority, LocalDate maybeDueDate) {
}
