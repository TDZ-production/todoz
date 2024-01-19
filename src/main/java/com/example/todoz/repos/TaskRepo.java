package com.example.todoz.repos;

import com.example.todoz.models.Task;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepo extends ListCrudRepository<Task, Long> {
    Optional<Task> findByIdAndUserId(Long taskId, Long userId);
    List<Task> findAllByUserIdAndDueDateWeekNumberGreaterThanAndDueDateWeekNumberLessThanEqual(Long userId, Integer previousWeek, Integer currentWeek);
    List<Task> findAllByUserIdAndDueDateWeekNumberGreaterThan(Long userId, Integer currentWeek);
    List<Task> findAllByUserIdAndDoneIsFalseAndWeekIdLessThanOrWeekIdNullAndDueDateWeekNumberLessThanEqual(Long userId, Long weekId, Integer currentWeek);
}
