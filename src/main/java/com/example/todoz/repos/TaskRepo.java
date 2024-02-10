package com.example.todoz.repos;

import com.example.todoz.dtos.WeekdayReviewDTO;
import com.example.todoz.models.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT t.priority, DATE(t.doneAt) as doneAt, COUNT(*) as count " +
            "FROM Task t " +
            "WHERE t.week.id = (SELECT w.id FROM Week w WHERE w.user.id = :userId ORDER BY w.weekNumber DESC LIMIT 1) " +
            "AND t.doneAt IS NOT NULL " +
            "GROUP BY t.priority, DATE(t.doneAt) " +
            "ORDER BY DATE(t.doneAt)")
    List<Object[]> getWeekdayReview(@Param("userId") Long userId);

    void deleteByIdAndUserId(Long taskId, Long userID);
}
