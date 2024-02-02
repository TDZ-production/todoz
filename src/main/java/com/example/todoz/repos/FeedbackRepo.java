package com.example.todoz.repos;

import com.example.todoz.models.Feedback;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface FeedbackRepo extends ListCrudRepository<Feedback, Long> {
    List<Feedback> findAllByResolvedFalse();
}