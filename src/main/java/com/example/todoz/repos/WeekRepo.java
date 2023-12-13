package com.example.todoz.repos;

import com.example.todoz.models.Week;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeekRepo extends ListCrudRepository<Week, Long> {

    @Query("SELECT w FROM Week w WHERE w.id = (SELECT MAX(w2.id) FROM Week w2)")
    Week findWeekWithHighestId();

}
