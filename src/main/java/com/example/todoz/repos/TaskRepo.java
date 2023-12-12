package com.example.todoz.repos;

import com.example.todoz.models.Task;
import org.springframework.data.repository.ListCrudRepository;

public interface TaskRepo extends ListCrudRepository<Task, Long> {
}
