package com.example.todoz.repos;

import com.example.todoz.models.Task;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepo extends ListCrudRepository<Task, Long> {
    Optional<Task> findByIdAndUserId(Long taskId, Long userId);
    List<Task> findAllByUserIdAndDueDateBetweenOrderByDueDate(Long userId, LocalDateTime previousSaturday, LocalDateTime currentSaturday);
    List<Task> findAllByUserIdAndDueDateAfterOrderByDueDate(Long userId, LocalDateTime dueDate);
    List<Task> findAllByUserIdAndLeftBehindNotNullOrderByLeftBehindDesc(Long userId);
    void deleteByIdAndUserId(Long taskId, Long userID);
}
