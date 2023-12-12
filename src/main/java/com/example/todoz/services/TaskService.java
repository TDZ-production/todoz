package com.example.todoz.services;

import com.example.todoz.models.Task;
import com.example.todoz.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepo taskRepo;

    @Autowired
    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    public List<Task> findByRating(Integer priority) {
        if (priority == 0 || priority > 4) {
            throw new RuntimeException("Input Integer must have value between 1 and 4.");
        } else {
            return taskRepo.findAllByPriority(priority).stream()
                    .filter(t -> t.getDueDate() == null)
                    .toList();
        }
    }

    public List<Task> findByRatingWithDates(Integer priority) {
        if (priority == 0 || priority > 4) {
            throw new RuntimeException("Input Integer must have value between 1 and 4.");
        } else {
            return taskRepo.findAllByPriority(priority).stream()
                    .filter(t -> t.getDueDate() != null)
                    .toList();
        }
    }
}
