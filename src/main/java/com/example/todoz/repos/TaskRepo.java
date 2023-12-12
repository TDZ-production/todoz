package com.example.todoz.repos;

import com.example.todoz.models.Task;
import org.springframework.data.repository.ListCrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepo extends ListCrudRepository<Task, Long> {
    List<Task> findAllByPriority(Integer priorityNumber);
}
