package com.example.todoz.week;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeekRepo extends ListCrudRepository<Week, Long> {
    Optional<Week> findByWeekNumberAndUserId(Integer currentWeekNumber, Long id);
    Optional<Week> findTopByUserIdOrderByWeekNumberDesc(Long id);
}
