package com.example.todoz.repos;

import com.example.todoz.models.Week;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeekRepo extends ListCrudRepository<Week, Long> {
    Week findByWeekNumber (Integer weekNumber);
}
