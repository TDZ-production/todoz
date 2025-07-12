package com.example.todoz.feedback;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepo repo;

    public FeedbackService(FeedbackRepo repo) {
        this.repo = repo;
    }

    public List<Feedback> getFeedbacks() {
        return repo.findAllByResolvedFalse();
    }

    public void save(Feedback feedback) {
        Feedback feed = new Feedback(feedback);
        repo.save(feed);
    }

    public Feedback findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Feedback id not found"));
    }
}