package com.example.todoz.feedback;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface FeedbackRepo extends ListCrudRepository<Feedback, Long> {
    List<Feedback> findAllByResolvedFalse();
}