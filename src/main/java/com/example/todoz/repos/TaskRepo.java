package com.example.todoz.repos;

import com.example.todoz.models.Task;
import com.example.todoz.models.Week;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TaskRepo extends ListCrudRepository<Task, Long> {
    List<Task> findAllByPriority(Integer priority);
    List<Task> findByUserId(Long id);

    List<Task> findByWeek(Week week);
}
