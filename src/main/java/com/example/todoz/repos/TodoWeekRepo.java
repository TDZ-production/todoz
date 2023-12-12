package com.example.todoz.repos;

import com.example.todoz.models.TodoWeek;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoWeekRepo extends ListCrudRepository<TodoWeek, Long> {
}
