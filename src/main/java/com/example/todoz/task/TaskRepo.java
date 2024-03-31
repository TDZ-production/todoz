package com.example.todoz.task;

import com.example.todoz.dtos.WeekdayReviewDTO;
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
    List<Task> findAllByUserIdAndDueDateBetweenAndLeftBehindNullOrderByDueDate(Long userId, LocalDateTime previousSaturday, LocalDateTime currentSaturday);
    List<Task> findAllByUserIdAndDueDateAfterAndLeftBehindNullOrderByDueDate(Long userId, LocalDateTime dueDate);
    List<Task> findAllByUserIdAndLeftBehindNotNullOrderByLeftBehindDesc(Long userId);

    @Query("SELECT new com.example.todoz.dtos.WeekdayReviewDTO(t.priority, DATE(t.doneAt), COUNT(*)) " +
            "FROM Task t " +
            "WHERE t.week.id = (SELECT w.id FROM Week w WHERE w.user.id = :userId ORDER BY w.weekNumber DESC LIMIT 1) " +
            "AND t.doneAt IS NOT NULL " +
            "GROUP BY t.priority, DATE(t.doneAt) " +
            "ORDER BY DATE(t.doneAt)")
    List<WeekdayReviewDTO> getWeekdayReview(Long userId);

    void deleteByIdAndUserId(Long taskId, Long userID);
}
