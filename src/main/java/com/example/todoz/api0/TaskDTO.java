package com.example.todoz.api0;

import com.example.todoz.task.Task;

import java.time.LocalDateTime;

public record TaskDTO(long id, String description, int priority, LocalDateTime createdAt, LocalDateTime dueDate, LocalDateTime doneAt) {

    public TaskDTO(Task t) {
        this(t.getId(), t.getDescription(), t.getPriority(), t.getCreatedAt(), t.getDueDate(), t.getDoneAt());
    }
}
